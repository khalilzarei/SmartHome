package com.khz.smarthome.model;

public class TaskModel {
    String startTime;
    String endTime;
    String task;

    public TaskModel(String startTime, String endTime, String task) {
        this.startTime = startTime;
        this.endTime   = endTime;
        this.task      = task;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
