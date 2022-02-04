package com.khz.smarthome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LightClick {

    @SerializedName("masterId")
    @Expose
    private String masterId;
    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

}