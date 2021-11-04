package com.khz.smarthome.ui.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khz.smarthome.model.LoginData;

public class LoginResponse {

    @SerializedName("message")
    @Expose
    private String    message;
    @SerializedName("data")
    @Expose
    private LoginData loginData;
    @SerializedName("status")
    @Expose
    private Boolean   status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData data) {
        this.loginData = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
