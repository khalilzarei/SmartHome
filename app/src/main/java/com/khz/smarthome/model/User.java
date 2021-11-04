package com.khz.smarthome.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khz.smarthome.helper.Constants;

import java.util.Date;

//@Entity(tableName = Constants.TABLE_USER)
public class User {
    private Integer id;
    @SerializedName("name")
    @Expose
    private String  name;
    @SerializedName("email")
    @Expose
    private String  email;
    @SerializedName("phone_number")
    @Expose
    private String  phoneNumber;
    @SerializedName("projects_count")
    @Expose
    private Integer projectsCount;

    @SerializedName("image")
    @Expose
    private UserImage userImage;

    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }

    public UserImage getUserImage() {
        return userImage;
    }

    public void setUserImage(UserImage userImage) {
        this.userImage = userImage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
