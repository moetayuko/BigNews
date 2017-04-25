package org.explosion.zhihudaily.bean;

import java.util.List;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class DailyStory {
    private String date;
    private List<Story> stories;
    private List<TopStory> topStories;

    public String getDate() {
        return date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public List<TopStory> getTopStories() {
        return topStories;
    }
}
