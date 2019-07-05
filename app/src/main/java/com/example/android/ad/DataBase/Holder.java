package com.example.android.ad.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.ad.model.adv_data;


@Database(entities = {adv_data.class}, version = 1, exportSchema = false)
public abstract class Holder extends RoomDatabase {
    private static final String LOG_TAG = Holder.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "list";
    private static Holder sInstance;

    public static Holder getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.e(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        Holder.class, Holder.DATABASE_NAME)

                        .build();
            }
        }
        Log.e(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
