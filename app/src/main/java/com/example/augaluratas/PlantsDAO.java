package com.example.augaluratas;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import java.util.List;
@Dao
public interface PlantsDAO {
    @Insert
    void insert(Plants plant);

    @Query("SELECT * FROM augalai")
    List<Plants> getAllPlants();

    @Delete
    void delete(Plants plant);
}
