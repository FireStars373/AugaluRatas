package com.example.augaluratas;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
@Database(entities = {Posts.class, Users.class, UserPostLikes.class, UserSettings.class, UserShoppingCart.class}, version = 1, exportSchema = false)
public abstract class User_PostDatabase extends RoomDatabase {

    public abstract PostsDAO postsDAO();
    public abstract UsersDAO usersDAO();
    public abstract UserPostLikesDAO userPostLikesDAO();
    public abstract UserSettingsDAO userSettingsDAO();
    public abstract UserShoppingCartDAO userShoppingCartDAO();

    private static volatile User_PostDatabase INSTANCE;

    public static User_PostDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (User_PostDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    User_PostDatabase.class, "posts_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    db.execSQL("PRAGMA foreign_keys=ON;");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
