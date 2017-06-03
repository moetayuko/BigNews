package org.explosion.zhihudaily.bean;

import com.bannerlayout.Interface.BannerModelCallBack;

/**
 * Created by dianlujitao on 17-5-10.
 */

public class BannerItem implements BannerModelCallBack {
    private String image;
    private String title;

    public BannerItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBannerUrl() {
        return image;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getBannerTitle() {
        return title;
    }
}
