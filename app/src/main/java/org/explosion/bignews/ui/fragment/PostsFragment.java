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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.explosion.bignews.R;
import org.explosion.bignews.adapter.EndlessRecyclerViewScrollListener;
import org.explosion.bignews.adapter.PostAdapter;
import org.explosion.bignews.bean.Post;
import org.explosion.bignews.helper.JSONParser;
import org.explosion.bignews.helper.WebUtils;
import org.explosion.bignews.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static org.explosion.bignews.helper.UIUtils.notifyNetworkError;

public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton scrollToTop;
    private RecyclerView postsView;
    private PostAdapter adapter;
    private LinearLayoutManager layoutManager;

    private Context mContext;

    private static String key_category_id = "KEY_CATEGORY_ID";
    private int category;
    private int page;
    private List<Post> curPosts;
    private List<Post> postList = new ArrayList<>();

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(int categoryId) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putInt(key_category_id, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if (getArguments() != null) {
            category = getArguments().getInt(key_category_id, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_list, container, false);

        setupScrollToTop(rootView);
        setupSwipeRefresh();

        setupPostsView(rootView);
        retrievePosts(true);

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
                    postsView.scrollToPosition(20);
                }
                postsView.smoothScrollToPosition(0);
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
                    if (activity.isCategoriesEmpty()) {
                        Message msg = new Message();
                        msg.what = MainActivity.RETRIEVE_DRAWER_MENU;
                        activity.handler.sendMessage(msg);
                    }
                }
                retrievePosts(true);
            }
        });
    }

    private void setupPostsView(View view) {
        postsView = view.findViewById(R.id.story_list);
        layoutManager = new LinearLayoutManager(getActivity());
        postsView.setLayoutManager(layoutManager);

        adapter = new PostAdapter(postList);
        postsView.setAdapter(adapter);
        postsView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
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
                        postsView.canScrollVertically(-1))
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
    private void retrievePosts(final boolean isRefreshing) {
        if (isRefreshing) {
            page = 1;
        }
        Log.d(TAG, "retrievePosts: URL: " + WebUtils.getPostsURL(page, category));
        OkGo.get(WebUtils.getPostsURL(page, category))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        curPosts = JSONParser.parsePostList(s);
                        if (curPosts != null) {
                            // 获取成功，更新UI
                            updatePostList(isRefreshing);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        // 获取失败，提示网络错误
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        notifyNetworkError(postsView, mContext);
                        super.onError(call, response, e);
                    }
                });
    }

    private void updatePostList(boolean isRefreshing) {
        // 如果是刷新则清空当前数据
        if (isRefreshing) {
            postList.clear();
        }
        // 加载更多数据
        postList.addAll(curPosts);
        adapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadMoreStories() {
        page++;
        retrievePosts(false);
    }
}
