package com.khz.smarthome.ui.main;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khz.smarthome.BR;
import com.khz.smarthome.model.Scene;

public class SceneViewModel extends BaseObservable {
    Scene scene;

    public SceneViewModel(Scene scene) {
        this.scene = scene;
    }

    @Bindable
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        notifyPropertyChanged(BR.scene);
    }

}
