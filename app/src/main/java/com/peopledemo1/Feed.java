package com.peopledemo1;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Feed {
    private String feedid;
    private String userid;
    private String image;
    private String profile;
    private Clothes clothes;

    public Feed(){

    }

    public Feed(String userid, String image, String profile, Clothes clothes) {
        this.feedid = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.userid = userid;
        this.image = image;
        this.profile = profile;
        this.clothes = clothes;
    }

    public String getFeedid() {
        return feedid;
    }

    public void setFeedid(String feedid) {
        this.feedid = feedid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }
}
