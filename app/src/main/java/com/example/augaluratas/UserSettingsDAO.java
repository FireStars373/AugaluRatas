package com.example.augaluratas;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserSettingsDAO {

    @Insert
    void insert(UserSettings userSettings);
    @Query("SELECT * FROM user_settings WHERE user_id=:id LIMIT 1")
    UserSettings getByUserId(long id);

    @Delete
    void delete(UserSettings userSettings);

    @Update
    void Update(UserSettings userSettings);
}
