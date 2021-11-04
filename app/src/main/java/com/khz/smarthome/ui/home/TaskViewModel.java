package com.khz.smarthome.ui.home;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.android.material.snackbar.Snackbar;
import com.khz.smarthome.BR;
import com.khz.smarthome.model.TaskModel;

public class TaskViewModel extends BaseObservable {
    TaskModel taskModel;
    String    currentTime;

    public TaskViewModel(TaskModel taskModel) {
        this.taskModel   = taskModel;
        this.currentTime = getTime();
    }

    @Bindable
    public TaskModel getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
        notifyPropertyChanged(BR.taskModel);
    }

    @Bindable
    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        notifyPropertyChanged(BR.currentTime);
    }

    public String getTime() {
        return taskModel.getStartTime() + " - " + taskModel.getEndTime();
    }

    public void onTaskClick(View view) {
        Snackbar.make(view, getCurrentTime() + " " + taskModel.getTask(), Snackbar.LENGTH_LONG).show();
    }
}
