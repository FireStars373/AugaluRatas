package com.example.augaluratas;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Users.class}, version = 1, exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {
    public abstract UsersDAO usersDAO();
    private static UsersDatabase INSTANCE;
    public static UsersDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (PostsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UsersDatabase.class, "users_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
