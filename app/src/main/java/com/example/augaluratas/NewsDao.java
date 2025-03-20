package com.example.augaluratas;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert
    void insert(News news);

    @Query("SELECT * FROM news")
    List<News> getAllNews();

    @Delete
    void delete(News news);

    @Query("DELETE FROM news")
    void deleteAll();

    @Query("SELECT * FROM news WHERE id = :id LIMIT 1")
    News getNewsById(Long id);
}
