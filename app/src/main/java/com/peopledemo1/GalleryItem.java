package com.peopledemo1;

public class GalleryItem {
    private String feed_img;
    private String feed_id;

    public GalleryItem(String feed_img, String feed_id) {
        this.feed_img = feed_img;
        this.feed_id = feed_id;
    }

    public String getFeed_img() {
        return feed_img;
    }

    public void setFeed_img(String feed_img) {
        this.feed_img = feed_img;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }
}
