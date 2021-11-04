package com.khz.smarthome.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.khz.smarthome.R;
import com.khz.smarthome.application.App;
import com.khz.smarthome.application.BaseActivity;
import com.khz.smarthome.databinding.ActivityHomeBinding;
import com.khz.smarthome.helper.SessionManager;
import com.khz.smarthome.model.Food;
import com.khz.smarthome.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    HomeViewModel       viewModel;
    List<Food>          foods;
    List<TaskModel>     taskModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.setChangeFont(false);
        binding   = DataBindingUtil.setContentView(this, R.layout.activity_home);
        viewModel = new HomeViewModel(this);
        Log.e(TAG, "IP => " + SessionManager.getIP());
        putData();
    }

    void putData() {
        foods      = new ArrayList<>();
        taskModels = new ArrayList<>();


        foods.add(new Food("Milk"));
        foods.add(new Food("Bread & cheese"));
        foods.add(new Food("Beer & wine"));

        taskModels.add(new TaskModel("8:00", "9:00", "Shopping"));
        taskModels.add(new TaskModel("10:00", "12:00", "Meet Tom"));
        taskModels.add(new TaskModel("18:00", "20:00", "Go to cinema"));


        viewModel.setFoods(foods);
        viewModel.setTaskModels(taskModels);
        binding.setViewModel(viewModel);
        wifiStrength();
    }

    /*
    Excellent >-50 dBm

    Good -50 to -60 dBm

    Fair -60 to -70 dBm

    Weak < -70 dBm

    int level = result.level;

    if (level >= -50) {
        //Best signal
    } else if (level >= -70) {
        //Good signal
    } else if (level >= -80) {
        //Low signal
    } else if (level >= -100) {
       //Very weak signal
    } else {
       //Too low signal
    }
     */
    void wifiStrength() {
        ConnectivityManager cm   = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         Info = cm.getActiveNetworkInfo();
        if (Info == null || !Info.isConnectedOrConnecting()) {
            Log.e(TAG, "No connection");
        } else {
            int netType    = Info.getType();
            int netSubtype = Info.getSubtype();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                Log.e(TAG, "Wifi connection");
                WifiManager      wifiManager = (WifiManager) getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResult  = wifiManager.getScanResults();
                for (int i = 0; i < scanResult.size(); i++) {
                    Log.e("scanResult", "Speed of wifi" + scanResult.get(i).level);//The db level of signal
                }
                // Need to get wifi strength
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                Log.e(TAG, "GPRS/3G connection");
                // Need to get differentiate between 3G/GPRS
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (SessionManager.getChangeFont())
            reOpenActivity();
        Log.e("Home Activity", "onRestart");
    }

    private void reOpenActivity() {
        Intent intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
        App.getInstance().onCreate();
    }
}