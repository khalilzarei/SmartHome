package com.khz.smarthome.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LightsStatus {

    @SerializedName("masterId")
    private String           masterId;

    @SerializedName("scanResult")
    private List<ScanLight> scanLight = null;

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public List<ScanLight> getScanResult() {
        return scanLight;
    }

    public void setScanResult(List<ScanLight> scanLight) {
        this.scanLight = scanLight;
    }

}
