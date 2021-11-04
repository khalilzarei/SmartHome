package com.khz.smarthome.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.khz.smarthome.helper.Constants;
import com.khz.smarthome.model.User;
import com.khz.smarthome.model.UserImage;

//@Database(entities = {}, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

//    public abstract UserDao getUserDao();


    private static DatabaseHelper databaseHelper;

    // synchronized is use to avoid concurrent access in multithred environment
    public static /*synchronized*/ DatabaseHelper getInstance(Context context) {
        if (null == databaseHelper) {
            databaseHelper = buildDatabaseInstance(context);
        }
        return databaseHelper;
    }

    private static DatabaseHelper buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DatabaseHelper.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public void cleanUp() {
        databaseHelper = null;
    }
}
