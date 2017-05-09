package org.explosion.zhihudaily.bean;

import java.util.ArrayList;

/**
 * Created by dianlujitao on 17-5-2.
 */

public class StoryContent extends StoryBase {
    private String body;
    private String image_source;
    private String image;
    private String share_url;
    private ArrayList<String> js;
    private ArrayList<String> images;
    private ArrayList<String> css;

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

    public ArrayList<String> getCss() {
        return css;
    }
}
