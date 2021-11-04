package com.khz.smarthome.ui.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.khz.smarthome.BR;
import com.khz.smarthome.model.Room;
import com.khz.smarthome.model.Scene;
import com.khz.smarthome.ui.main.RoomAdapter.RoomClickListener;
import com.khz.smarthome.ui.main.SceneAdapter.SceneClickListener;

import java.util.List;

public class MainViewModel extends BaseObservable {
    String      imageUrl;
    List<Room>  rooms;
    List<Scene> scenes;
    boolean     showRooms;
    boolean     showScenes;
    Room        roomDefault;
    Scene       sceneDefault;

    public MainViewModel(String imageUrl, List<Room> rooms, List<Scene> scenes) {
        this.imageUrl = imageUrl;
        this.rooms    = rooms;
        this.scenes   = scenes;
    }

    @Bindable
    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
        notifyPropertyChanged(BR.scenes);
    }

    @Bindable
    public Scene getSceneDefault() {
        return sceneDefault;
    }

    public void setSceneDefault(Scene sceneDefault) {
        this.sceneDefault = sceneDefault;
        notifyPropertyChanged(BR.sceneDefault);
    }

    @Bindable
    public Room getRoomDefault() {
        return roomDefault;
    }

    public void setRoomDefault(Room roomDefault) {
        this.roomDefault = roomDefault;
        notifyPropertyChanged(BR.roomDefault);
    }

    @Bindable
    public boolean getShowRooms() {
        return showRooms;
    }

    public void setShowRooms(boolean showRooms) {
        this.showRooms = showRooms;
        notifyPropertyChanged(BR.showRooms);
    }

    @Bindable
    public boolean getShowScenes() {
        return showScenes;
    }

    public void setShowScenes(boolean showScenes) {
        this.showScenes = showScenes;
        notifyPropertyChanged(BR.showScenes);
    }

    @Bindable
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyPropertyChanged(BR.rooms);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public void showRoomList(View view) {
        setShowRooms(!getShowRooms());
    }

    public void showSceneList(View view) {
        setShowScenes(!getShowScenes());
    }

    public void backPress(View view) {
        ((Activity) view.getContext()).onBackPressed();
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String imageUrl) {
        Log.e("setImageUrl", imageUrl + "");
        if (!imageUrl.isEmpty()) {
            Glide.with(imageView)
                    .asBitmap()
                    .load(imageUrl)
//                    .into(imageView);
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    Transition<? super Bitmap> transition) {
//                            int w = bitmap.getWidth();
//                            int h = bitmap.getHeight()
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }

    }

    @BindingAdapter("setRoomItems")
    public static void setRoomItems(RecyclerView recyclerView, List<Room> rooms) {
        RoomAdapter adapter = new RoomAdapter((RoomClickListener) recyclerView.getContext(), rooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("setSceneItems")
    public static void setSceneItems(RecyclerView recyclerView, List<Scene> scenes) {
        SceneAdapter adapter = new SceneAdapter((SceneClickListener) recyclerView.getContext(), scenes);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
