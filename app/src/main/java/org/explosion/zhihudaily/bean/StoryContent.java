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

package org.explosion.zhihudaily.bean;

import java.util.List;

/**
 * Created by dianlujitao on 17-5-2.
 */

public class StoryContent extends StoryBase {
    private String body;
    private String image_source;
    private String image;
    private String share_url;
    private List<String> js;
    private List<String> images;
    private List<String> css;

    public StoryContent() {
        super();
    }

    public StoryContent(StoryContent m) {
        super(m);
        body = m.body;
        image_source = m.image_source;
        image = m.image;
        share_url = m.share_url;
        js = m.js;
        images = m.images;
        css = m.css;
    }

    public String getBody() {
        return body;
    }

    public String getImageSource() {
        return image_source;
    }

    public String getImage() {
        return image;
    }

    public String getShareUrl() {
        return share_url;
    }

    public String getJs() {
        return js.get(0);
    }

    public List<String> getCss() {
        return css;
    }
}
