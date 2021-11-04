package com.khz.smarthome.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.khz.smarthome.BR;
import com.khz.smarthome.application.App;
import com.khz.smarthome.model.Food;
import com.khz.smarthome.model.TaskModel;
import com.khz.smarthome.ui.main.MainActivity;
import com.khz.smarthome.ui.setting.SettingActivity;

import java.util.Date;
import java.util.List;

public class HomeViewModel extends BaseObservable {
    String          currentDate;
    List<Food>      foods;
    List<TaskModel> taskModels;
    Activity        activity;

    public HomeViewModel(Activity activity) {
        this.activity    = activity;
        this.currentDate = DateFormat.format("EEE, d MMM yyyy", new Date()).toString();
    }

    @Bindable
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        notifyPropertyChanged(BR.currentDate);
    }

    @Bindable
    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyPropertyChanged(BR.foods);
    }

    @Bindable
    public List<TaskModel> getTaskModels() {
        return taskModels;
    }

    public void setTaskModels(List<TaskModel> taskModels) {
        this.taskModels = taskModels;
        notifyPropertyChanged(BR.taskModels);
    }

    public void openSetting(View view) {
        openActivity(SettingActivity.class);
    }

    public void openMain(View view) {
        openActivity(MainActivity.class);
    }

    public void openBrowser(View view) {
        if (App.isNetworkConnected()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            activity.startActivity(browserIntent);
        } else
            showSnack(view, "Connection lose");
    }

    public void openStorage(View view) {
        showSnack(view, "THIS SERVICE NOT AVAILABLE");
//        openActivity(StorageActivity.class);
    }

    void openActivity(Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
    }

    void showSnack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @BindingAdapter("initFoodItems")
    public static void initFoodItems(RecyclerView recyclerView, List<Food> foods) {
        FoodsAdapter adapter = new FoodsAdapter(foods);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("initTasksItems")
    public static void initTasksItems(RecyclerView recyclerView, List<TaskModel> taskModels) {
        TaskAdapter adapter = new TaskAdapter(taskModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
