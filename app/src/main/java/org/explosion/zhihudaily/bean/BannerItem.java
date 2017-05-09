package org.explosion.zhihudaily.bean;

/**
 * Created by dianlujitao on 17-5-10.
 */

public class BannerItem {
    private String image;
    private String title;

    public BannerItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
