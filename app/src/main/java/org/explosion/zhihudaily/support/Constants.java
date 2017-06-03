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

package org.explosion.zhihudaily.support;

/**
 * Created by dianlujitao on 17-5-2.
 */

public final class Constants {
    public static final class URL {
        public static final String API_PREFIX = "http://news-at.zhihu.com/api/4/";
        public static final String STORY_PREFIX = "news/";
        public static final String OLD_STORY_PREFIX = "before/";
        public static final String THEME_PREFIX = "theme/";
        public static final String THEMES_SUFFIX = "themes";
        public static final String LATEST_STORY_SUFFIX = "latest";
    }

    public static final class KEY {
        public static final String STORY_ID = "story_id";
        public static final String STORY_LIST_URL = "story_list_url";
        public static final String STORY_LIST_TYPE = "story_list_type";
    }
}
