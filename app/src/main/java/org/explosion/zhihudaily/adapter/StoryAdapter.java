package org.explosion.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
        Glide.with(mContext).load(story.getImage()).into(holder.storyImage);
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }
}
