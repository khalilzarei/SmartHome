package com.khz.smarthome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("lightID")
    @Expose
    private Integer lightID;
    @SerializedName("type")
    @Expose
    private String  type;
    @SerializedName("dimLevel")
    @Expose
    private String  dimLevel;

    public Integer getLightID() {
        return lightID;
    }

    public void setLightID(Integer lightID) {
        this.lightID = lightID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimLevel() {
        return dimLevel;
    }

    public void setDimLevel(String dimLevel) {
        this.dimLevel = dimLevel;
    }

}
