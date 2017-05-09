package org.explosion.zhihudaily.bean;

import java.util.ArrayList;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class DailyStory {
    private String date;
    private ArrayList<Story> stories;
    private ArrayList<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public ArrayList<TopStory> getTopStories() {
        return top_stories;
    }
}
