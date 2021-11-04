/*******************************************************************************
 * Copyright (c) 1999, 2016 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 */
package com.khz.smarthome.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khz.smarthome.R;
import com.khz.smarthome.databinding.FoodItem;
import com.khz.smarthome.model.Food;

import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Food> foods;
    private LayoutInflater layoutInflater;


    public FoodsAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FoodItem foodItem = DataBindingUtil
                .inflate(layoutInflater, R.layout.item_row_food, parent, false);
        return new ViewHolder(foodItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Food          food       = foods.get(position);
        ViewHolder    viewHolder = (ViewHolder) holder;
        FoodViewModel viewModel  = new FoodViewModel(food);
        viewHolder.foodItem.setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        FoodItem foodItem;

        public ViewHolder(FoodItem foodItem) {
            super(foodItem.getRoot());
            this.foodItem = foodItem;
        }
    }
}
