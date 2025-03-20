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
    static UsersDatabase db2;

    static PostsDatabase db3;
    static NewsDatabase db4;

    @Override
    public void onCreate(){
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),PlantsDatabase.class,"my_app_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        db2 = Room.databaseBuilder(getApplicationContext(),UsersDatabase.class,"users_db").allowMainThreadQueries().build();
        db3 = Room.databaseBuilder(getApplicationContext(),PostsDatabase.class,"posts_db").allowMainThreadQueries().build();
        db4 = Room.databaseBuilder(getApplicationContext(),NewsDatabase.class,"news_db").allowMainThreadQueries().build();
    }

    public static PlantsDatabase getDatabase()
    {
        return db;
    }

    public static UsersDatabase getUsersDatabase()
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
