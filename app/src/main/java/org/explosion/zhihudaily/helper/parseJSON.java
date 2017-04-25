package org.explosion.zhihudaily.helper;

import com.google.gson.Gson;

import org.explosion.zhihudaily.bean.DailyStory;

/**
 * Created by dianlujitao on 17-4-25.
 */

public final class parseJSON {
    public static DailyStory parseDailyNews(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, DailyStory.class);
    }
}
