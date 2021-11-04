package com.khz.smarthome.model;

import com.google.gson.annotations.SerializedName;

public class ScanLight {

    @SerializedName("id")
    private String id;

    @SerializedName("dim")
    private String dim;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }

}
