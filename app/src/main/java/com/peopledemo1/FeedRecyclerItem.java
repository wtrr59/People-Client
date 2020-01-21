package com.peopledemo1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class FeedRecyclerItem {

    private Drawable profileimg ;
    private String userID;
    private Drawable peoplePost;


    public Drawable getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(Drawable profileimg) {
        this.profileimg = profileimg;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Drawable getPeoplePost() {
        return peoplePost;
    }

    public void setPeoplePost(Drawable peoplePost) {
        this.peoplePost = peoplePost;
    }

}
