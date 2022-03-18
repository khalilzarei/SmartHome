package com.khz.smarthome.ui.setting;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.khz.smarthome.BR;
import com.khz.smarthome.R;
import com.khz.smarthome.application.App;
import com.khz.smarthome.helper.Constants;
import com.khz.smarthome.helper.SessionManager;
import com.khz.smarthome.helper.TypefaceUtil;

public class SettingViewModel extends BaseObservable {
    Activity activity;

    private final UiModeManager uiModeManager;
    int   nightIcon;
    int   ipStatus;
    int   counter  = 0;
    int   resId;
    int[] iconList;
    int   iconSize = 60;

    public SettingViewModel(Activity activity, int nightIcon) {
        this.activity  = activity;
        this.nightIcon = nightIcon;
        this.resId     = SessionManager.getIconResID();
        uiModeManager  = (UiModeManager) activity.getSystemService(UI_MODE_SERVICE);
        iconList       = new int[]{
                R.drawable.ic_lamp_off,
                R.drawable.ic_lamp_off_one,
                R.drawable.ic_lamp_off_two,
                R.drawable.ic_lamp_off_tree,
                R.drawable.ic_lamp_off_four,
                R.drawable.ic_lamp_off_five,
                R.drawable.ic_lamp_off_six,
                R.drawable.ic_lamp_off_seven,
                R.drawable.ic_lamp_off_eaght,
                R.drawable.ic_lamp_off_nine,
        };
    }


    @Bindable
    public int getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(int nightIcon) {
        this.nightIcon = nightIcon;
        notifyPropertyChanged(BR.nightIcon);
    }

    @Bindable
    public int getIpStatus() {
        return ipStatus;
    }

    public void setIpStatus(int ipStatus) {
        this.ipStatus = ipStatus;
        notifyPropertyChanged(BR.ipStatus);
    }

    @BindingAdapter("setIconImage")
    public static void setIconImage(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter("setImageNightDayIcon")
    public static void setImageNightDayIcon(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    @Bindable
    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
        notifyPropertyChanged(BR.resId);
    }

    public void showIpButton(View view) {
        if (counter == 9) {
            setIpStatus(View.VISIBLE);
        }
        counter++;
        Log.e("showIpButton", "Counter => " + counter);
    }

    public void chooseFont(View view) {
        AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
        b.setTitle("Choose Font:");
        final String[] types = {
                "Alger", "Bernhc", "Brushsci", "Chiller", "Serif", "Default"
        };
        b.setItems(types, (dialogInterface, which) -> {
            dialogInterface.dismiss();
            String font;
            switch (which) {
                case 0:
                    font = "alger";
                    break;
                case 1:
                    font = "bernhc";
                    break;
                case 2:
                    font = "brushsci";
                    break;
                case 3:
                    font = "chiller";
                    break;
                case 4:
                    font = "serif";
                    break;
                case 5:
                    font = "roboto";
                    break;
                default:
                    font = "";
                    break;
            }
            SessionManager.setFont(font);
            SessionManager.setChangeFont(true);
            TypefaceUtil.overrideFont(activity, "fonts/" + font + ".TTF");
            reOpenActivity();
        });
        b.show();
    }

    //visible => 0
    //Gone    => 8

    public void setIp(View v) {
        AlertDialog.Builder builder     = new AlertDialog.Builder(activity);
        ViewGroup           viewGroup   = activity.findViewById(android.R.id.content);
        View                dialogView  = LayoutInflater.from(activity).inflate(R.layout.dialog_set_ip, viewGroup, false);
        EditText            etIP        = dialogView.findViewById(R.id.etIP);
        EditText            etProjectId = dialogView.findViewById(R.id.etProjectId);
        etProjectId.setText(SessionManager.getProjectId());
        if (!SessionManager.getIP().isEmpty())
            etIP.setText(SessionManager.getIP());
        else
            etIP.setText(Constants.MQTT_SERVER_URI);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
        dialogView.findViewById(R.id.btnSend).setOnClickListener(view -> {
            if (etIP.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter IP", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etProjectId.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter Topic", Toast.LENGTH_SHORT).show();
                return;
            }

            String ip = etIP.getText().toString();
            if (!ip.contains("tcp"))
                ip = "tcp://" + ip + ":1883";

            SessionManager.setIsLoggedIn(true);
            SessionManager.setIp(ip);
            String projectId = etProjectId.getText().toString();
            SessionManager.setProjectId(projectId);
            SessionManager.setIpStatus(View.GONE);
            setIpStatus(View.GONE);
            counter = 0;
            alertDialog.dismiss();
        });
    }

    public void dayNight(View view) {
        if (SessionManager.isNightModeOn()) {
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
            SessionManager.setIsNightModeOn(false);
            setNightIcon(R.drawable.ic_day);
        } else {
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
            setNightIcon(R.drawable.ic_night);
            SessionManager.setIsNightModeOn(true);
        }
        reOpenActivity();
    }

    @BindingAdapter("setImageSize")
    public static void setImageSize(ImageView imageView, int size) {
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        layoutParams.width  = size;
        layoutParams.height = size;
        imageView.setLayoutParams(layoutParams);
    }

    public void increaseIconSize(View view) {
        if (iconSize < 100)
            setIconSize(iconSize += 10);
        else
            setIconSize(100);

        SessionManager.setIconSize(getIconSize());
    }

    public void decreaseIconSize(View view) {
        if (iconSize > 40)
            setIconSize(iconSize -= 10);
        else
            setIconSize(40);

        SessionManager.setIconSize(getIconSize());
    }

    public void changeIconLeft(View view) {
        int resId = SessionManager.getIconResID(), index = 0;
        for (int i = 0; i < iconList.length; i++) {
            int id = iconList[i];
            if (id == resId)
                index = i;
        }
        index++;
        if (index >= iconList.length)
            index = 0;
        setResId(iconList[index]);
        SessionManager.setIconResID(iconList[index]);
    }

    public void changeIconRight(View view) {
        int resId = SessionManager.getIconResID(), index = 0;
        for (int i = 0; i < iconList.length; i++) {
            int id = iconList[i];
            if (id == resId)
                index = i;
        }
        index--;
        if (index < 0)
            index = iconList.length - 1;
        setResId(iconList[index]);
        SessionManager.setIconResID(iconList[index]);
    }

    private void reOpenActivity() {
        Intent intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
        App.getInstance().onCreate();
    }

    @Bindable
    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        notifyPropertyChanged(BR.iconSize);
    }
}
