package org.explosion.zhihudaily.helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.explosion.zhihudaily.bean.DailyStory;
import org.explosion.zhihudaily.bean.StoryContent;
import org.explosion.zhihudaily.bean.Theme;

import java.util.List;

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

    public static List<Theme> getThemesList(String jsonData) {
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(jsonData);
        return gson.fromJson(root.getAsJsonObject().get("others"),
                new TypeToken<List<Theme>>(){}.getType());
    }
}
