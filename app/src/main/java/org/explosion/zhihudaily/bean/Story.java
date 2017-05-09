package org.explosion.zhihudaily.bean;

import java.util.ArrayList;

/**
 * Created by dianlujitao on 17-4-25.
 */

public class Story extends StoryBase {
    private ArrayList<String> images;
    private boolean multipic;

    public ArrayList<String> getImages() {
        return images;
    }

    public String getImage() {
        return images.get(0);
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }
}
