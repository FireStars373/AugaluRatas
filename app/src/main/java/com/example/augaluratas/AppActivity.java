package com.example.augaluratas;

import android.app.Application;

import androidx.room.Room;

public class AppActivity extends Application {
    static PlantsDatabase db;
    static UsersDatabase db2;

    @Override
    public void onCreate(){
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),PlantsDatabase.class,"my_app_db").allowMainThreadQueries().build();
        db2 = Room.databaseBuilder(getApplicationContext(),UsersDatabase.class,"users_db").allowMainThreadQueries().build();
    }

    public static PlantsDatabase getDatabase()
    {
        return db;
    }

    public static UsersDatabase getUsersDatabase()
    {
        return db2;
    }
}
