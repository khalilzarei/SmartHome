package com.khz.smarthome.ui.main;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khz.smarthome.R;
import com.khz.smarthome.databinding.SceneItem;
import com.khz.smarthome.model.Scene;

import java.util.List;


public class SceneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Scene>    scenes;
    private       LayoutInflater layoutInflater;
    SceneClickListener sceneClickListener;

    public SceneAdapter(SceneClickListener sceneClickListener, List<Scene> scenes) {
        this.sceneClickListener = sceneClickListener;
        this.scenes             = scenes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SceneItem itemRow = DataBindingUtil
                .inflate(layoutInflater, R.layout.item_row_scene, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Scene    scene       = scenes.get(position);
        ViewHolder    viewHolder = (ViewHolder) holder;
        SceneViewModel viewModel  = new SceneViewModel(scene);

        viewHolder.sceneItem.setViewModel(viewModel);
        viewHolder.sceneItem.cardRoom.setOnClickListener(view -> {
            if (sceneClickListener != null)
                sceneClickListener.onSceneClickListener(scene);
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        SceneItem sceneItem;

        ViewHolder(SceneItem sceneItem) {
            super(sceneItem.getRoot());
            this.sceneItem = sceneItem;
        }
    }

    @Override
    public int getItemCount() {
        return scenes.size();
    }

    public interface SceneClickListener {
        void onSceneClickListener(Scene scene);
    }


}