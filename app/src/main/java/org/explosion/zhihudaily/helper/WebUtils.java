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

import java.util.List;

import static org.explosion.zhihudaily.support.Constants.URL.API_PREFIX;
import static org.explosion.zhihudaily.support.Constants.URL.LATEST_STORY_SUFFIX;
import static org.explosion.zhihudaily.support.Constants.URL.OLD_STORY_PREFIX;
import static org.explosion.zhihudaily.support.Constants.URL.STORY_PREFIX;
import static org.explosion.zhihudaily.support.Constants.URL.THEMES_SUFFIX;
import static org.explosion.zhihudaily.support.Constants.URL.THEME_PREFIX;

/**
 * Created by dianlujitao on 17-5-2.
 */

public final class WebUtils {
    private static final String CSS_LINK_PATTERN = " <link href=\"%s\" type=\"text/css\" rel=\"stylesheet\" />";
    public static final String MIME_TYPE = "text/html";
    public static final String ENCODING = "utf-8";

    private static final String DIV_HEADLINE = "class=\"headline\"";
    private static final String DIV_HEADLINE_IGNORED = "class=\"headline-ignored\"";
    private static final String NIGHT_DIV_TAG_START = "<div class=\"night\">";
    private static final String NIGHT_DIV_TAG_END = "</div>";

    public static String buildHtmlWithCss(String html, List<String> cssUrls) {
        StringBuilder buf = new StringBuilder();
        for (String cssUrl : cssUrls) {
            buf.append(String.format(CSS_LINK_PATTERN, cssUrl));
        }
        boolean isNightMode = ThemeHelper.isNightModeEnabled();
        if (isNightMode) {
            buf.append(NIGHT_DIV_TAG_START);
        }
        // Hack: 去掉HTML中为顶部图片预留的空间
        buf.append(html.replace(DIV_HEADLINE, DIV_HEADLINE_IGNORED));
        if (isNightMode) {
            buf.append(NIGHT_DIV_TAG_END);
        }
        return buf.toString();
    }

    public static String getLatestStoryListURL() {
        return API_PREFIX + STORY_PREFIX + LATEST_STORY_SUFFIX;
    }

    public static String getStoryURL(int id) {
        return API_PREFIX + STORY_PREFIX + Integer.toString(id);
    }

    public static String getThemeListURL() {
        return API_PREFIX + THEMES_SUFFIX;
    }

    public static String getThemeDescURL(int id) {
        return API_PREFIX + THEME_PREFIX + Integer.toString(id);
    }

    public static String getDailyStoryByDate(String date) {
        return API_PREFIX + STORY_PREFIX + OLD_STORY_PREFIX + date;
    }
}
