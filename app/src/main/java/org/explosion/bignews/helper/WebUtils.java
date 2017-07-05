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

package org.explosion.bignews.helper;

/**
 * Created by dianlujitao on 17-5-2.
 */

public final class WebUtils {
    private static String API_PREFIX = "http://www.explosion.ml/wp-json/wp/v2/";

    public static String getCategoriesURL() {
        return API_PREFIX + "categories?context=embed&per_page=100";
    }

    public static String getPostsURL(int page, int category) {
        String rc =  API_PREFIX + "posts?context=embed&page=" + Integer.toString(page);
        if (category != -1) {
            rc += "&categories=" + Integer.toString(category);
        }
        return rc;
    }

    public static String getPostURL(int id) {
        return "http://www.explosion.ml/?p=" + Integer.toString(id);
    }
}
