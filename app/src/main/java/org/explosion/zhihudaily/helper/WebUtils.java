package org.explosion.zhihudaily.helper;

import java.util.List;
import java.util.StringTokenizer;

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
}
