package com.khz.smarthome.model;

import com.google.gson.annotations.SerializedName;

public class LightClick {

    @SerializedName("masterid")
    private Integer masterId;

    @SerializedName("command")
    private String command;

    @SerializedName("attributes")
    private Attributes attributes;

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
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