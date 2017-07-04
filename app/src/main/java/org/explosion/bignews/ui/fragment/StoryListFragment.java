/*
 * MIT License
 *
 * Copyright (c) 2017 dianlujitao <dianlujitao@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.explosion.bignews.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.explosion.bignews.R;
import org.explosion.bignews.adapter.EndlessRecyclerViewScrollListener;
import org.explosion.bignews.adapter.StoryAdapter;
import org.explosion.bignews.bean.DailyStory;
import org.explosion.bignews.bean.Story;
import org.explosion.bignews.helper.ParseJSON;
import org.explosion.bignews.support.Constants;
import org.explosion.bignews.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static org.explosion.bignews.helper.UIUtils.notifyNetworkError;
import static org.explosion.bignews.helper.WebUtils.getDailyStoryByDate;
import static org.explosion.bignews.support.Constants.KEY.STORY_LIST_TYPE;

public class StoryListFragment extends Fragment {

    private static final String TAG = "StoryListFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton scrollToTop;
    private RecyclerView storyListView;
    private StoryAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<Story> storyList = new ArrayList<>();
    private DailyStory dailyStory;

    private Context mContext;

    private String storyListURL;

    public StoryListFragment() {
        // Required empty public constructor
    }

    public static StoryListFragment newInstance(String url, boolean isTheme) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY.STORY_LIST_URL, url);
        args.putBoolean(STORY_LIST_TYPE, isTheme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mContext = getContext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story_list, container, false);

        setupScrollToTop(rootView);
        setupSwipeRefresh();

        setupStoryListView(rootView);
        retrieveStoryList(true);

        return rootView;
    }

    // 设置回到顶部按钮
    private void setupScrollToTop(View view) {
        scrollToTop = view.findViewById(R.id.scroll_to_top);
        scrollToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = layoutManager.getPosition(layoutManager.getChildAt(0));
                // 若当前位置大于20则直接回到20后再缓慢回到顶部，以解决动画过长的问题
                if (position > 20) {
                    storyListView.scrollToPosition(20);
                }
                storyListView.smoothScrollToPosition(0);
                scrollToTop.hide();
            }
        });
    }

    // 设置下拉刷新
    private void setupSwipeRefresh() {
        swipeRefreshLayout = getActivity().findViewById(R.id.story_list_swipe_refresh);
        // 进度条颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    if (activity.isThemesEmpty()) {
                        Message msg = new Message();
                        msg.what = MainActivity.RETRIEVE_DRAWER_MENU;
                        activity.handler.sendMessage(msg);
                    }
                }
                retrieveStoryList(true);
            }
        });
    }

    private void setupStoryListView(View view) {
        storyListView = view.findViewById(R.id.story_list);
        layoutManager = new LinearLayoutManager(getActivity());
        storyListView.setLayoutManager(layoutManager);

        adapter = new StoryAdapter(storyList);
        storyListView.setAdapter(adapter);
        storyListView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 已经在顶部或滑动时隐藏回到顶部按钮
                if (dy > 0 || (dy < 0 && scrollToTop.isShown()))
                    scrollToTop.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // 滑动停止且没有回到顶部时显示回到顶部按钮
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        storyListView.canScrollVertically(-1))
                    scrollToTop.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // 上拉加载更多
                loadMoreStories();
            }
        });
    }

    // 获取故事列表
    private void retrieveStoryList(final boolean isRefreshing) {
        if (isRefreshing) {
            // 获取URL
            storyListURL = getArguments().getString(Constants.KEY.STORY_LIST_URL);
        }
        OkGo.get(storyListURL)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // 获取成功，更新UI
                        dailyStory = ParseJSON.getDailyStories(s);
                        if (dailyStory != null) {
                            updateStoryList(isRefreshing);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        // 获取失败，提示网络错误
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        notifyNetworkError(storyListView, mContext);
                        super.onError(call, response, e);
                    }
                });
    }

    private void updateStoryList(boolean isRefreshing) {
        // 如果是刷新则清空当前数据
        if (isRefreshing) {
            storyList.clear();
        }
        // 加载更多数据
        storyList.addAll(dailyStory.getStories());
        adapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadMoreStories() {
        // 上拉加载仅首页可用
        if (!getArguments().getBoolean(STORY_LIST_TYPE)) {
            storyListURL = getDailyStoryByDate(dailyStory.getDate());
            retrieveStoryList(false);
        }
    }
}
