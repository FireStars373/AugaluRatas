package com.example.augaluratas;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Posts.class, Users.class}, version = 1, exportSchema = false)
public abstract class PostsDatabase extends RoomDatabase {

    // Abstraktus metodas, kuris grąžina PostsDAO
    public abstract PostsDAO postsDAO();

    // Singleton pattern, kad būtų užtikrinta tik viena duomenų bazės instancija
    private static PostsDatabase INSTANCE;

    public static PostsDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (PostsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PostsDatabase.class, "posts_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
