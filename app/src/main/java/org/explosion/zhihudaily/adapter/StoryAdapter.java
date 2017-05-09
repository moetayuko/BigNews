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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.explosion.zhihudaily.R;
import org.explosion.zhihudaily.bean.BannerItem;
import org.explosion.zhihudaily.bean.Story;
import org.explosion.zhihudaily.bean.TopStory;
import org.explosion.zhihudaily.support.Constants;
import org.explosion.zhihudaily.ui.activity.StoryActivity;

import java.util.ArrayList;
import java.util.List;

import ezy.ui.view.BannerView;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "StoryAdapter";

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private ArrayList<Story> mStoryList;
    private ArrayList<TopStory> mTopStoryList;

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
        BannerView banner;

        BannerHolder(View view) {
            super(view);
            banner = (BannerView) view.findViewById(R.id.banner);
        }
    }

    public StoryAdapter(ArrayList<Story> storyList, ArrayList<TopStory> topStoryList) {
        mStoryList = storyList;
        mTopStoryList = topStoryList;
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
            ((BannerHolder)holder).banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    TopStory topStory = mTopStoryList.get(position);
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra(Constants.KEY.STORY_ID, topStory.getId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.story_list_item,
                    parent, false);
            holder = new ViewHolder(view);
            ((ViewHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition() - 1;
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
        if (position == 0)
            return TYPE_BANNER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_BANNER:
                List<BannerItem> list = new ArrayList<>();
                for (TopStory i : mTopStoryList) {
                    String title = i.getTitle(), image = i.getImage();
                    if (title != null && image != null) {
                        list.add(new BannerItem(image, title));
                        Log.d(TAG, "onBindViewHolder: image: " + image + " title: " + title);
                    }
                }

                BannerHolder bannerHolder = (BannerHolder) holder;
                bannerHolder.banner.setViewFactory(new BannerView.ViewFactory<BannerItem>() {
                    @Override
                    public View create(BannerItem bannerItem, int position, ViewGroup container) {
                        ImageView iv = new ImageView(container.getContext());
                        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA);
                        Glide.with(container.getContext().getApplicationContext()).load(bannerItem.getImage()).apply(options).into(iv);
                        return iv;
                    }
                });
                bannerHolder.banner.setDataList(list);
                bannerHolder.banner.start();
                break;
            case TYPE_NORMAL:
                int pos = position - 1;
                Story story = mStoryList.get(pos);
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.storyTitle.setText(story.getTitle());
                Glide.with(mContext).load(story.getImage()).into(viewHolder.storyImage);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }
}
