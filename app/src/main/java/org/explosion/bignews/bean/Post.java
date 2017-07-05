package org.explosion.bignews.bean;

/**
 * Created by dianlujitao on 17-7-4.
 */

public class Post {
    private class Title {
        private String rendered;

        String getRendered() {
            return rendered;
        }

        void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    private Title title;
    private int id;

    public String getTitle() {
        return title.getRendered();
    }

    public void setTitle(String title) {
        this.title.setRendered(title);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
