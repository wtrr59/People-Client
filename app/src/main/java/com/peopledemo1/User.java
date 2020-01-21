package com.peopledemo1;

import java.util.List;

public class User {
    private String userid;
    private String password;
    private String email;
    private List<SimilarPeople> prediction;

    public User(String userid, String password,  String email) {
        this.userid = userid;
        this.password = password;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SimilarPeople> getPrediction() {
        return prediction;
    }

    public void setPrediction(List<SimilarPeople> prediction) {
        this.prediction = prediction;
    }
}
