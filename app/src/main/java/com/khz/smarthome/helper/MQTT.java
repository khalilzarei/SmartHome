package com.khz.smarthome.helper;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTT implements MqttCallback {

    MqttClient client;


//    public static void main(String[] args) {
//        new MQTT().doDemo();
//    }

    public void doDemo() {
        try {
            client = new MqttClient("tcp://5.253.26.230:1883", "ExampleAndroidClient");
            client.connect();
            client.setCallback(this);
            client.subscribe("8");
            MqttMessage message = new MqttMessage();
            message.setPayload("GetRoom0".getBytes());
            client.publish("8", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.e("messageArrived", "messageArrived" + message);
        System.out.println(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}