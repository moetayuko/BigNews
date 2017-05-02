package org.explosion.zhihudaily.helper;

import com.google.gson.Gson;

import org.explosion.zhihudaily.bean.DailyStory;
import org.explosion.zhihudaily.bean.StoryContent;

/**
 * Created by dianlujitao on 17-4-25.
 */

public final class parseJSON {
    public static DailyStory parseDailyNews(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, DailyStory.class);
    }

    public static StoryContent parseStoryContent(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, StoryContent.class);
    }
}
