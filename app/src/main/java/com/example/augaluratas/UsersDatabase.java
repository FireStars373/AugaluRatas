package com.example.augaluratas;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Users.class}, version = 1, exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {
    public abstract UsersDAO usersDAO();

}
