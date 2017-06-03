package org.explosion.zhihudaily.bean;

import java.util.List;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class Story extends StoryBase {
    private List<String> images;
    private boolean multipic;

    public List<String> getImages() {
        return images;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }
}
