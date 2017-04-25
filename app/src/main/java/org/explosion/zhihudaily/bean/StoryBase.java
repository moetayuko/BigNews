package org.explosion.zhihudaily.bean;

import java.util.List;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class StoryBase {
    private String title;
    private String ga_prefix;
    private int type;
    private int id;

    public String getTitle() {
        return title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }
}
