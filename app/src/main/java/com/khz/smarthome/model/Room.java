package com.khz.smarthome.model;

import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String  name;

    public Room(Integer id, String name) {
        this.id   = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
