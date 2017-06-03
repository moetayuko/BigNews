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

public final class ParseJSON {
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
                new TypeToken<List<Theme>>() {
                }.getType());
    }
}
