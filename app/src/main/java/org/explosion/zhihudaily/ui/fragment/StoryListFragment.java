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

package org.explosion.zhihudaily.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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

import org.explosion.zhihudaily.R;
import org.explosion.zhihudaily.adapter.EndlessRecyclerViewScrollListener;
import org.explosion.zhihudaily.adapter.StoryAdapter;
import org.explosion.zhihudaily.bean.DailyStory;
import org.explosion.zhihudaily.bean.Story;
import org.explosion.zhihudaily.bean.TopStory;
import org.explosion.zhihudaily.helper.ParseJSON;
import org.explosion.zhihudaily.support.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static org.explosion.zhihudaily.helper.UIUtils.notifyNetworkError;
import static org.explosion.zhihudaily.helper.WebUtils.getDailyStoryByDate;
import static org.explosion.zhihudaily.support.Constants.KEY.STORY_LIST_TYPE;

public class StoryListFragment extends Fragment {

    private static final String TAG = "StoryListFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton scrollToTop;
    private RecyclerView storyListView;
    private StoryAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<Story> storyList = new ArrayList<>();
    private List<TopStory> topStoryList;
    private DailyStory dailyStory;

    private Context mContext;

    private String storyListURL;

    private Boolean isTheme;

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
            isTheme = getArguments().getBoolean(STORY_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story_list, container, false);

        setupScrollToTop();
        setupSwipeRefresh();

        setupStoryListView(rootView);
        retrieveStoryList(true);

        return rootView;
    }

    private void setupScrollToTop() {
        scrollToTop = (FloatingActionButton) getActivity().findViewById(R.id.scroll_to_top);
        scrollToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = layoutManager.getPosition(layoutManager.getChildAt(0));
                if (position > 20) {
                    storyListView.scrollToPosition(20);
                }
                storyListView.smoothScrollToPosition(0);
                scrollToTop.hide();
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.story_list_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveStoryList(true);
            }
        });
    }

    private void setupStoryListView(View view) {
        storyListView = (RecyclerView) view.findViewById(R.id.story_list);
        layoutManager = new LinearLayoutManager(getActivity());
        storyListView.setLayoutManager(layoutManager);

        if (!isTheme) {
            topStoryList = new ArrayList<>();
        }
        adapter = new StoryAdapter(storyList, topStoryList);
        storyListView.setAdapter(adapter);
        storyListView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 || (dy < 0 && scrollToTop.isShown()))
                    scrollToTop.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        storyListView.canScrollVertically(-1))
                    scrollToTop.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreStories();
            }
        });
    }

    private void retrieveStoryList(final boolean isRefreshing) {
        if (isRefreshing) {
            storyListURL = getArguments().getString(Constants.KEY.STORY_LIST_URL);
        }
        OkGo.get(storyListURL)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dailyStory = ParseJSON.getDailyStories(s);
                        if (dailyStory != null) {
                            updateStoryList(isRefreshing);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        notifyNetworkError(storyListView, mContext);
                        super.onError(call, response, e);
                    }
                });
    }

    private void updateStoryList(boolean isRefreshing) {
        if (isRefreshing) {
            storyList.clear();
            if (!isTheme) {
                topStoryList.clear();
                topStoryList.addAll(dailyStory.getTopStories());
            }
        }
        storyList.addAll(dailyStory.getStories());
        adapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadMoreStories() {
        if (!isTheme) {
            storyListURL = getDailyStoryByDate(dailyStory.getDate());
            retrieveStoryList(false);
        }
    }
}
