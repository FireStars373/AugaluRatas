package com.example.augaluratas;

import android.app.Application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.ByteArrayOutputStream;

public class AppActivity extends Application {
    static PlantsDatabase db;
    static User_PostDatabase db2;


    @Override
    public void onCreate(){
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),PlantsDatabase.class,"my_app_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        //Log.d("DB_CHECK", "Duomenų bazė sukurta!");
        db2 = Room.databaseBuilder(getApplicationContext(),User_PostDatabase.class,"users_db").allowMainThreadQueries().build();
    }

    public static PlantsDatabase getDatabase()
    {
        return db;
    }

    public static User_PostDatabase getUser_PostDatabase()
    {
        return db2;
    }
    public static PostsDatabase getPostsDatabase()
    {
        return db3;
    }
    public static NewsDatabase getNewsDatabase()
    {
        return db4;
    }
}
