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

    public static String buildHtmlWithCss(String html, List<String> cssUrls) {
        StringBuilder buf = new StringBuilder();
        for (String cssUrl : cssUrls) {
            buf.append(String.format(CSS_LINK_PATTERN, cssUrl));
        }
        // Hack: 去掉HTML中为顶部图片预留的空间
        buf.append(html.replace(DIV_HEADLINE, DIV_HEADLINE_IGNORED));
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
