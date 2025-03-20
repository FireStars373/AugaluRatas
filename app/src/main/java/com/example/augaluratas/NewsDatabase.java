package com.example.augaluratas;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {News.class}, version = 1, exportSchema = true)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
