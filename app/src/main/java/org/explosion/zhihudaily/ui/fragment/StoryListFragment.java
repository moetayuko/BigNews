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
import org.explosion.zhihudaily.adapter.StoryAdapter;
import org.explosion.zhihudaily.bean.DailyStory;
import org.explosion.zhihudaily.bean.Story;
import org.explosion.zhihudaily.helper.parseJSON;
import org.explosion.zhihudaily.support.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static org.explosion.zhihudaily.helper.UIUtils.notifyNetworkError;

public class StoryListFragment extends Fragment {

    private static final String TAG = "StoryListFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton scrollToTop;
    private RecyclerView storyListView;
    private StoryAdapter adapter;

    private List<Story> storyList = new ArrayList<>();
    private DailyStory dailyStory;

    private Context mContext;

    private String storyListURL;

    public StoryListFragment() {
        // Required empty public constructor
    }

    public static StoryListFragment newInstance(String url) {
        StoryListFragment fragment = new StoryListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY.STORY_LIST_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mContext = getContext();

            swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.story_list_swipe_refresh);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    retrieveStoryList();
                }
            });

            storyListURL = getArguments().getString(Constants.KEY.STORY_LIST_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_story_list, container, false);

        scrollToTop = (FloatingActionButton) getActivity().findViewById(R.id.scroll_to_top);
        scrollToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storyListView.smoothScrollToPosition(0);
                scrollToTop.hide();
            }
        });

        setupStoryListView(rootView);
        retrieveStoryList();

        return rootView;
    }

    private void setupStoryListView(View view) {
        storyListView = (RecyclerView) view.findViewById(R.id.story_list);
        storyListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new StoryAdapter(storyList);
        storyListView.setAdapter(adapter);
        storyListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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
        });
    }

    private void retrieveStoryList() {
        OkGo.get(storyListURL)
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dailyStory = parseJSON.getDailyStories(s);
                        if (dailyStory != null)
                            updateStoryList();
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

    private void updateStoryList() {
        storyList.clear();
        storyList.addAll(dailyStory.getStories());
        adapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
