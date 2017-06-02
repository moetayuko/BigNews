package org.explosion.zhihudaily.helper;

import com.google.gson.Gson;

import org.explosion.zhihudaily.bean.DailyStory;
import org.explosion.zhihudaily.bean.StoryContent;

/**
 * Created by dianlujitao on 17-4-25.
 */

public final class parseJSON {
    public static DailyStory getDailyStories(String jsonData) {
        return new Gson().fromJson(jsonData, DailyStory.class);
    }

    public static StoryContent getStoryContent(String jsonData) {
        return new Gson().fromJson(jsonData, StoryContent.class);
    }
}
