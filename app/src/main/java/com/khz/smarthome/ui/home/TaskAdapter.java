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

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khz.smarthome.R;
import com.khz.smarthome.databinding.TaskItem;
import com.khz.smarthome.model.Food;
import com.khz.smarthome.model.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<TaskModel> taskModels;
    private LayoutInflater layoutInflater;

    public TaskAdapter(List<TaskModel> taskModels) {
        this.taskModels = taskModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TaskItem taskItem = DataBindingUtil
                .inflate(layoutInflater, R.layout.item_row_task, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskModel     taskModel  = taskModels.get(position);
        ViewHolder    viewHolder = (TaskAdapter.ViewHolder) holder;
        TaskViewModel viewModel  = new TaskViewModel(taskModel);
        viewHolder.taskItem.setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TaskItem taskItem;

        public ViewHolder(TaskItem taskItem) {
            super(taskItem.getRoot());
            this.taskItem = taskItem;
        }

    }
}
