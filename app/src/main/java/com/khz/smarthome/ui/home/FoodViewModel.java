package com.khz.smarthome.ui.home;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.android.material.snackbar.Snackbar;
import com.khz.smarthome.BR;
import com.khz.smarthome.model.Food;

public class FoodViewModel extends BaseObservable {

    Food food;

    public FoodViewModel(Food food) {
        this.food = food;
    }

    @Bindable
    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
        notifyPropertyChanged(BR.food);
    }

    public void onFoodClick(View view) {
        Snackbar.make(view, food.getName(), Snackbar.LENGTH_LONG).show();
    }
}
