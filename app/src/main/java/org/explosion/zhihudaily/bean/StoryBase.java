package org.explosion.zhihudaily.bean;

/**
 * Created by dianlujitao on 17-4-25.
 */

class StoryBase {
    protected String title;
    protected String ga_prefix;
    protected int type;
    protected int id;

    public StoryBase() { }

    public StoryBase(StoryBase m) {
        title = m.title;
        ga_prefix = m.ga_prefix;
        type = m.type;
        id = m.id;
    }

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
