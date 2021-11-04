package com.khz.smarthome.model;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khz.smarthome.helper.Constants;

public class UserImage {
    @SerializedName("url")
    @Expose
    private String url;

    public UserImage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
