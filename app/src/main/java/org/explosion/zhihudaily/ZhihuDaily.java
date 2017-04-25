package org.explosion.zhihudaily;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class ZhihuDaily extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
    }
}
