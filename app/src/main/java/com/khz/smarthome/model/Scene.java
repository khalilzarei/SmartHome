package com.khz.smarthome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scene {

    public Scene(String name) {
        this.name = name;
    }


    @SerializedName("id")
    private String  id;
    @SerializedName("name")
    private String  name;
    @SerializedName("mi")
    private Integer mi;
    @SerializedName("si")
    private Integer si;

    public Scene(String name, Integer mi, Integer si) {
        this.name = name;
        this.mi   = mi;
        this.si   = si;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMi() {
        return mi;
    }

    public void setMi(Integer mi) {
        this.mi = mi;
    }

    public Integer getSi() {
        return si;
    }

    public void setSi(Integer si) {
        this.si = si;
    }

}
