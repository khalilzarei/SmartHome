package com.khz.smarthome.ui.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.khz.smarthome.R;
import com.khz.smarthome.application.App;
import com.khz.smarthome.application.BaseActivity;
import com.khz.smarthome.databinding.ActivityMainBinding;
import com.khz.smarthome.helper.Constants;
import com.khz.smarthome.helper.SessionManager;
import com.khz.smarthome.model.Device;
import com.khz.smarthome.model.LightClick;
import com.khz.smarthome.model.LightsStatus;
import com.khz.smarthome.model.Room;
import com.khz.smarthome.model.ScanLight;
import com.khz.smarthome.model.Scene;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements RoomAdapter.RoomClickListener, SceneAdapter.SceneClickListener {

    ActivityMainBinding binding;
    MainViewModel       viewModel;
    MqttAndroidClient   mqttAndroidClient;
    private final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout   container;
    ConstraintLayout rootLayout;
    public int dim = -1;

    List<Device> devices = new ArrayList<>();
    List<Scene>  scenes  = new ArrayList<>();
    List<Room>   rooms   = new ArrayList<>();
    Gson         gson    = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        scenes     = new ArrayList<>();
        container  = binding.container;
        rootLayout = binding.rootLayout;
        viewModel  = new MainViewModel("", rooms, scenes);
        binding.setViewModel(viewModel);
        rooms.clear();
        showLog("MainActivity :  " + "HeightSize : " + getHeightSize() + " WidthSize : " + getWidthSize());
        initMQTT();

    }

    private void addView(Device device) {
        final ImageView               imageView    = new ImageView(MainActivity.this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(getRealLeft(device.getL()));
        if (device.getL() == 100)
            layoutParams.setMarginStart(getRealLeft(device.getL() - 5));
        layoutParams.topMargin = getRealTop(device.getT());

        showLog(" MQTT(L:" + device.getL() + ",T:" + device.getT() + ") REAL(L:" + getRealLeft(device.getL() - 5) + ",T:" + getRealTop(device.getT()) + ")");
//        layoutParams.setMargins(getRealLeft(device.getL()), getRealTop(device.getT()), 0, 0);
        layoutParams.width  = 90;
        layoutParams.height = 90;

        imageView.setLayoutParams(layoutParams);
        int imgRes = R.drawable.ic_room;
        if (device.getdType().equalsIgnoreCase("Dali Light"))
            if (device.getDim().equals("0"))
                imgRes = R.drawable.ic_lamp_off;
            else {
                imgRes = R.drawable.ic_lamp_on;
                imageView.setColorFilter(Color.argb(Integer.parseInt(device.getDim()), 255, 255, 0));
            }
        else if (device.getdType().equalsIgnoreCase("Room"))
            imgRes = R.drawable.ic_room;
        else if (device.getdType().equalsIgnoreCase("curtain"))
            imgRes = R.drawable.ic_curtain;

        imageView.setImageResource(imgRes);
        container.addView(imageView);

        imageView.setOnClickListener(v -> {
            String onOffMSG = "";
            if (device.getdType().equalsIgnoreCase("Dali Light")) {
                if (device.getDim().equals("0")) {
                    showLog("off");
                    imageView.setImageResource((R.drawable.ic_lamp_on));
                    imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                } else {
                    showLog("on");
                    imageView.setImageResource((R.drawable.ic_lamp_off));
                    imageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                }
                String ty = "ID";
                if (device.getdA2().equalsIgnoreCase("single"))
                    ty = "ID";
                else if (device.getdA2().equalsIgnoreCase("group"))
                    ty = "GP";

                onOffMSG = "{\"masterId\":\"" + device.getdA1() + "\"," +
                        "\"command\":\"setDim\"," +
                        "\"attributes\":{" +
                        "\"lightID\":\"" + device.getdA0() + "\"," +
                        "\"type\":\"" + ty + "\"," +
                        "\"dimLevel\":\"" + (device.getDim().equals("0") ? "254" : "0") + "\"" +
                        "}" +
                        "}";
            } else if (device.getdType().equalsIgnoreCase("Room")) {
                onOffMSG = "GetRoom" + device.getdA0();
            } else if (device.getdType().equalsIgnoreCase("curtain")) {
                if (device.getDim().equalsIgnoreCase("open"))
                    onOffMSG = "{\"id\":\"" + device.getdA0() + "\",\"command\":\"close\"}";
                else if (device.getDim().equalsIgnoreCase("close"))
                    onOffMSG = "{\"id\":\"" + device.getdA0() + "\",\"command\":\"open\"}";
//                topic = "Curtain/In";
            }
            publishMessage(onOffMSG, Constants.DALI_IN);
            vibration();
            playSound();

            Toast.makeText(MainActivity.this, device.getdType() + " - " + device.getDim(), Toast.LENGTH_SHORT)
                    .show();
        });

        imageView.setOnLongClickListener(view -> {
            if (device.getdType().equalsIgnoreCase("Dali Light"))
                showDimDialog(device, (ImageView) view);
            else if (device.getdType().equalsIgnoreCase("curtain")) {
                showCurtainDialog(device);
            }
            return false;
        });
        showLog("Add");
    }

    private int getRealLeft(double left) {
//        return pxToDp((int) (((getWidthSize() * left) / 100)));
        return (int) (((getWidthSize() * left) / 100));
    }

    private int getRealTop(double top) {
//        return pxToDp((int) (((getHeightSize() * top) / 100)));
        return (int) (((getHeightSize() * top) / 100));
    }

    public void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (SessionManager.getVibration())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(500);
            }
    }

    public void playSound() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.lamp_sound);
        if (SessionManager.getPlaySound())
            mp.start();
    }

    /*
    1024   100
    x       15
     */

    public int getHeightSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getWidthSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void showDimDialog(Device device, ImageView imageView) {
        AlertDialog.Builder builder    = new AlertDialog.Builder(MainActivity.this);
        ViewGroup           viewGroup  = findViewById(android.R.id.content);
        View                dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_dim, viewGroup, false);
        CardView            cardView   = dialogView.findViewById(R.id.cardView);
        SeekBar             seekBar    = dialogView.findViewById(R.id.seekBar);
        seekBar.setProgress(Integer.parseInt(device.getDim()));
        cardView.setCardBackgroundColor(Color.argb(Integer.parseInt(device.getDim()), 255, 255, 0));
        dim = seekBar.getProgress();
        TextView tvResult = dialogView.findViewById(R.id.tvDim);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tvResult.setText(String.valueOf(progress));
                dim = progress;
                cardView.setCardBackgroundColor(Color.argb(progress, 255, 255, 0));
                cardView.setPreventCornerOverlap(false);
                cardView.setRadius(App.getInstance().dpToPx(30));
//                Toast.makeText(activity, "seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(activity, "seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(activity, "seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
        dialogView.findViewById(R.id.btnSend).setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, device.getdType() + " DIM " + dim, Toast.LENGTH_SHORT).show();

            imageView.setColorFilter(Color.argb(dim, 255, 255, 0));
            String message = "{\"command\":\"setDim\",\"ID\":\"" + device.getdId() + "\",\"dim\":\"" + dim + "\",\"projectId\":\"" + device.getpId() + "\",\"roomId\":\"" + device.getrId() + "\"}";
            message = "{\"masterId\":" + device.getdA1()
                    + ",\"command\":\"setDim\",\"attributes\":{\"lightID\":\"" + device.getdA0()
                    + "\",\"type\":\"" + device.getdA2()
                    + "\",\"dimLevel\":\"" + dim + "\"}}";
            publishMessage(message, Constants.DALI_IN);
            alertDialog.dismiss();
        });
    }


    public void showCurtainDialog(Device device) {
        AlertDialog.Builder builder    = new AlertDialog.Builder(MainActivity.this);
        ViewGroup           viewGroup  = findViewById(android.R.id.content);
        View                dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_curtain, viewGroup, false);

        TextView tvResult = dialogView.findViewById(R.id.tvTitle);
        String   title    = "Please select Action for Curtain ";
        tvResult.setText(title);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
        dialogView.findViewById(R.id.imgCancel).setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.btnClose).setOnClickListener(view -> {
            String message = "{\"id\":\"" + device.getdA0() + "\",\"command\":\"close\"}";
            publishMessage(message, Constants.CURTAIN_IN);
        });
        dialogView.findViewById(R.id.btnOpen).setOnClickListener(view -> {
            String message = "{\"id\":\"" + device.getdA0() + "\",\"command\":\"open\"}";
            publishMessage(message, Constants.CURTAIN_IN);
        });
        dialogView.findViewById(R.id.btnStop).setOnClickListener(view -> {
            String message = "{\"id\":\"" + device.getdA0() + "\",\"command\":\"stop\"}";
            publishMessage(message, Constants.CURTAIN_IN);
        });
    }

    public int pxToDp(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private void initMQTT() {

        String url = Constants.MQTT_SERVER_URI;

        if (SessionManager.isLoggedIn())
            url = SessionManager.getIP();
        mqttAndroidClient = new MqttAndroidClient(
                MainActivity.this, url, Constants.clientId);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect)
                    subscribeToTopic(SessionManager.getProjectId());
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                showLog("Message Arrived Topic => " + topic + " Message => " + message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        try {
            IMqttToken token = mqttAndroidClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    showLog("onSuccess");
                    subscribeToTopic(SessionManager.getProjectId());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    showLog("onFailure");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void subscribeToTopic(String topic) {
        int qos = 1;
        try {
            mqttAndroidClient.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    showLog("Subscribed!");
                    publishMessage("GetRooms", SessionManager.getProjectId());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    showLog("Failed to subscribe");
                }

            });

//            mqttAndroidClient.subscribe(SessionManager.getTopic(), qos, MainActivity.this);

            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                }

                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    showLog("Message Arrived SubScripted " + message.toString());

                    if (message.toString().contains("link")) {
                        initViews(message);
                    } else if (message.toString().contains("setDim")) {
                        showLog("Message Arrived => Light Click");
                        LightClick lightClick = gson.fromJson(message.toString(), LightClick.class);
                        for (Device device : devices) {
                            if (lightClick.getAttributes().getLightID().equals(device.getdId())) {
                                device.setDim(lightClick.getAttributes().getDimLevel());
                            }
                            addView(device);
                            showLog("messageArrived setDim => " + device.getdType());
                        }

                    } else if (message.toString().contains("scanResult")) {
                        LightsStatus lightsStatus = gson.fromJson(message.toString(), LightsStatus.class);
                        for (Device device : devices) {
                            boolean find = false;
                            for (ScanLight scanLight : lightsStatus.getScanResult()) {
                                if (scanLight.getId().equals(String.valueOf(device.getdId()))) {
                                    showLog("scanResult -> " + scanLight.getId() + " " + device.getdId());
                                    device.setDim(scanLight.getDim());
                                    find = true;
                                }
                            }
                            if (!find)
                                device.setDim("0");
                            addView(device);
                            showLog("messageArrived scanResult => " + device.getdType());
                        }
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(message.getPayload()));
                            rooms.clear();
                            viewModel.setRooms(rooms);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Room room = gson.fromJson(jsonArray.get(i).toString(), Room.class);
                                rooms.add(room);
                                showLog("Room: " + room.getId() + " - " + room.getName());
                            }
                            viewModel.setRooms(rooms);
                            viewModel.setShowRooms(rooms.size() > 0);
                            if (rooms.size() > 0) {
                                viewModel.setRoomDefault(rooms.get(0));
                                publishMessage("GetRoom" + rooms.get(0).getId(), SessionManager.getProjectId());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }


    public void publishMessage(String msg, String publishTopic) {
        byte[] encodedPayload;
        try {
            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            mqttAndroidClient.publish(publishTopic, message);
            showLog("Message Published " + publishTopic + " message: " + message.toString());
            if (message.toString().contains("setDim") && message.toString().contains("dimLevel")) {
                LightClick lightClick = gson.fromJson(message.toString(), LightClick.class);
                for (Device device : devices) {
                    if (lightClick.getAttributes().getLightID().equals(device.getdId())) {
                        device.setDim(lightClick.getAttributes().getDimLevel());
                    }
                    addView(device);
                    showLog("messageArrived setDim => " + device.getdType());
                }
            }

        } catch (Exception e) {
            showLog("Error Publishing: " + e.getMessage());
        }
    }

    private void showLog(String mainText) {
        Log.e(TAG, mainText);
    }

    public void publishMessage(View view) {
        publishMessage("GetRoom0", SessionManager.getProjectId());
    }


    private void initViews(@NonNull MqttMessage message) {
        devices.clear();
        scenes.clear();
        viewModel.setSceneDefault(null);
        container.removeAllViews();
        if (message.toString().contains("link"))
            try {
                JSONArray jsonArray = new JSONArray(new String(message.getPayload()));
                if (message.toString().contains("link")) {
                    JSONObject obj      = jsonArray.getJSONObject(0);
                    String     imageUrl = obj.getString("link");
                    viewModel.setImageUrl(imageUrl);
                }
                JSONArray deviceList = jsonArray.getJSONArray(1);
//                showLog("Device Size : " + deviceList.length());

                for (int i = 0; i < deviceList.length(); i++) {
                    Device device = gson.fromJson(deviceList.get(i).toString(), Device.class);
                    devices.add(device);
                    addView(device);
//                    showLog("I: " + device.getId());
                }

                JSONArray deviceScenes = jsonArray.getJSONArray(2);
//                showLog("Scenes Size : " + deviceScenes.length());

                for (int i = 0; i < deviceScenes.length(); i++) {
                    Scene scene = gson.fromJson(deviceScenes.get(i).toString(), Scene.class);
//                    showLog(" deviceScenes: " + scene.getName());
                    scenes.add(scene);
                }
                viewModel.setShowScenes(scenes.size() > 0);
                viewModel.setScenes(scenes);
                if (scenes.size() > 0)
                    viewModel.setSceneDefault(scenes.get(0));

            } catch (JSONException e) {
                showLog("JSONException : " + e.getMessage());
            }
    }

    @Override
    public void onRoomClickListener(Room room) {
        vibration();
        playSound();
        Toast.makeText(this, "Room => " + room.getName(), Toast.LENGTH_SHORT).show();
//        String topicOffline = SessionManager.getProjectId() ;
//        if (SessionManager.getIP().contains("192.168"))
//            topicOffline = "Dali/In";
        publishMessage("GetRoom" + room.getId(), SessionManager.getProjectId());
        viewModel.setRoomDefault(room);
    }

    @Override
    public void onSceneClickListener(Scene scene) {
        vibration();
        playSound();
        String msg = "{\"masterId\":" + scene.getMi() + ",\"command\":\"callScene\",\"attributes\":{\"sceneId\":" + scene.getSi() + "}}";
        publishMessage(msg, Constants.DALI_IN);
        viewModel.setSceneDefault(scene);
        Toast.makeText(this, "Scene => " + scene.getName(), Toast.LENGTH_SHORT).show();
    }
}
