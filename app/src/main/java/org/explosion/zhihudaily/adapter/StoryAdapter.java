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

package org.explosion.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bannerlayout.Interface.OnBannerClickListener;
import com.bannerlayout.widget.BannerLayout;
import com.bumptech.glide.Glide;

import org.explosion.zhihudaily.R;
import org.explosion.zhihudaily.bean.BannerItem;
import org.explosion.zhihudaily.bean.Story;
import org.explosion.zhihudaily.bean.TopStory;
import org.explosion.zhihudaily.helper.WebUtils;
import org.explosion.zhihudaily.support.Constants;
import org.explosion.zhihudaily.ui.activity.StoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "StoryAdapter";

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private List<Story> mStoryList;
    private List<TopStory> mTopStoryList;

    private boolean showBanner;

    private static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView storyImage;
        TextView storyTitle;

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            storyImage = (ImageView) view.findViewById(R.id.story_image);
            storyTitle = (TextView) view.findViewById(R.id.story_title);
        }
    }

    private static class BannerHolder extends RecyclerView.ViewHolder {
        BannerLayout banner;

        BannerHolder(View view) {
            super(view);
            banner = (BannerLayout) view.findViewById(R.id.banner);
        }
    }

    public StoryAdapter(List<Story> storyList, List<TopStory> topStoryList) {
        mStoryList = storyList;
        mTopStoryList = topStoryList;
        showBanner = (mTopStoryList != null) &&
                !WebUtils.isCellularDataLessModeEnabled();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view;
        final RecyclerView.ViewHolder holder;

        if (viewType == TYPE_BANNER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.banner, parent, false);
            holder = new BannerHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.story_list_item,
                    parent, false);
            holder = new ViewHolder(view);
            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getRealPosition(holder);
                    Story story = mStoryList.get(position);
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra(Constants.KEY.STORY_ID, story.getId());
                    mContext.startActivity(intent);
                }
            });
        }

        return holder;
    }

    public int getItemViewType(int position) {
        if (showBanner && position == 0)
            return TYPE_BANNER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        showBanner = (mTopStoryList != null) &&
                !WebUtils.isCellularDataLessModeEnabled();

        switch (holder.getItemViewType()) {
            case TYPE_BANNER:
                List<BannerItem> bannerItemList = new ArrayList<>();
                for (TopStory i : mTopStoryList) {
                    String title = i.getTitle(), image = i.getImage();
                    if (title != null && image != null) {
                        bannerItemList.add(new BannerItem(image, title));
                    }
                }

                ((BannerHolder) holder).banner
                        .initTips(false, true, true)
                        .initListResources(bannerItemList)
                        .setOnBannerClickListener(new OnBannerClickListener() {
                            @Override
                            public void onBannerClick(View view, int position, Object model) {
                                TopStory story = mTopStoryList.get(position);
                                Intent intent = new Intent(mContext, StoryActivity.class);
                                intent.putExtra(Constants.KEY.STORY_ID, story.getId());
                                mContext.startActivity(intent);
                            }
                        })
                        .switchBanner(true);
                break;
            case TYPE_NORMAL:
                int pos = getRealPosition(holder);
                if (pos < 0) {
                    break;
                }
                Story story = mStoryList.get(pos);
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.storyTitle.setText(story.getTitle());
                if (story.getImages() == null || WebUtils.isCellularDataLessModeEnabled()) {
                    viewHolder.storyImage.setVisibility(View.GONE);
                    Log.d(TAG, "onBindViewHolder: Remove image for: " + story.getTitle());
                } else {
                    viewHolder.storyImage.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(story.getImages().get(0)).into(viewHolder.storyImage);
                }
                break;
            default:
                break;
        }
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return showBanner ? position - 1 : position;
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }
}
