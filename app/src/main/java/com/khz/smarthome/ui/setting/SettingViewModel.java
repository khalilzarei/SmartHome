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
    int nightIcon;
    int ipStatus;
    int counter = 0;

    public SettingViewModel(Activity activity, int nightIcon) {
        this.activity  = activity;
        this.nightIcon = nightIcon;
        uiModeManager  = (UiModeManager) activity.getSystemService(UI_MODE_SERVICE);
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

    @BindingAdapter("setImage")
    public static void setImage(ImageView imageView, int res) {
        imageView.setImageResource(res);
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
        AlertDialog.Builder builder    = new AlertDialog.Builder(activity);
        ViewGroup           viewGroup  = activity.findViewById(android.R.id.content);
        View                dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_set_ip, viewGroup, false);
        EditText            etIP       = dialogView.findViewById(R.id.etIP);
        EditText            etTopic    = dialogView.findViewById(R.id.etTopic);
        etTopic.setText(SessionManager.getProjectId());
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
            if (etTopic.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter Topic", Toast.LENGTH_SHORT).show();
                return;
            }
            String ip = etIP.getText().toString();
            if (!ip.contains("tcp"))
                ip = "tcp://" + ip + ":1883";
            String topic = etTopic.getText().toString();
            SessionManager.setIsLoggedIn(true);
            SessionManager.setIp(ip);
            SessionManager.setProjectId(topic);
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

    private void reOpenActivity() {
        Intent intent = activity.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
        App.getInstance().onCreate();
    }
}
