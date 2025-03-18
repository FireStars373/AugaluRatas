package com.example.augaluratas;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Plants.class}, version = 2, exportSchema = true)
public abstract class PlantsDatabase extends RoomDatabase {
    private static volatile PlantsDatabase INSTANCE;

    public abstract PlantsDAO plantsDAO();

    public static PlantsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlantsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PlantsDatabase.class, "plants_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
