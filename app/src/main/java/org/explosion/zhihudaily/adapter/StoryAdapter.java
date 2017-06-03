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

import com.bumptech.glide.Glide;

import org.explosion.zhihudaily.R;
import org.explosion.zhihudaily.bean.Story;
import org.explosion.zhihudaily.support.Constants;
import org.explosion.zhihudaily.ui.activity.StoryActivity;

import java.util.List;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private static final String TAG = "StoryAdapter";

    private Context mContext;

    private List<Story> mStoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView storyImage;
        TextView storyTitle;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            storyImage = (ImageView) view.findViewById(R.id.story_image);
            storyTitle = (TextView) view.findViewById(R.id.story_title);
        }
    }

    public StoryAdapter(List<Story> storyList) {
        mStoryList = storyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.story_list_item,
                parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Story story = mStoryList.get(position);
                Intent intent = new Intent(mContext, StoryActivity.class);
                intent.putExtra(Constants.KEY.STORY_ID, story.getId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Story story = mStoryList.get(position);
        holder.storyTitle.setText(story.getTitle());
        if (story.getImages() == null) {
            holder.storyImage.setVisibility(View.GONE);
            Log.d(TAG, "onBindViewHolder: Remove image for: " + story.getTitle());
        } else {
            holder.storyImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(story.getImages().get(0)).into(holder.storyImage);
        }
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }
}
