package com.khz.smarthome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class Token {
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;

    private Date date;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

}