package com.khz.smarthome.ui.main;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khz.smarthome.BR;
import com.khz.smarthome.model.Room;

public class RoomViewModel extends BaseObservable {
    Room room;

    public RoomViewModel(Room room) {
        this.room = room;
    }

    @Bindable
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        notifyPropertyChanged(BR.room);
    }

}
