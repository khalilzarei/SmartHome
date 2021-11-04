package com.khz.smarthome.ui.main;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khz.smarthome.R;
import com.khz.smarthome.databinding.RoomItem;
import com.khz.smarthome.model.Room;

import java.util.List;


public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Room>     rooms;
    private       LayoutInflater layoutInflater;
    RoomClickListener roomClickListener;

    public RoomAdapter(RoomClickListener roomClickListener, List<Room> rooms) {
        this.roomClickListener = roomClickListener;
        this.rooms             = rooms;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RoomItem itemRow = DataBindingUtil
                .inflate(layoutInflater, R.layout.item_row_room, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Room    room       = rooms.get(position);
        ViewHolder    viewHolder = (ViewHolder) holder;
        RoomViewModel viewModel  = new RoomViewModel(room);

        viewHolder.roomItem.setViewModel(viewModel);
        viewHolder.roomItem.cardRoom.setOnClickListener(view -> {
            if (roomClickListener != null)
                roomClickListener.onRoomClickListener(room);
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        RoomItem roomItem;

        ViewHolder(RoomItem roomItem) {
            super(roomItem.getRoot());
            this.roomItem = roomItem;
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public interface RoomClickListener {
        void onRoomClickListener(Room room);
    }


}