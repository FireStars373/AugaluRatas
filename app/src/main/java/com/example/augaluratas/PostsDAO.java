package com.example.augaluratas;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostsDAO {
    @Insert
    void insert(Posts post);

    @Query("SELECT * FROM posts")
    List<Posts> getAllPosts();

    @Query("SELECT * FROM posts WHERE user_id = :userId")
    List<Posts> getPostsByUser(long userId);
    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    Posts getPostById(long postId);  // Čia naudojame 'LIMIT 1', nes ID turėtų būti unikalus
    @Delete
    void delete(Posts post);

    @Query("DELETE FROM posts")
    void deleteAll();

    @Query("DELETE FROM posts WHERE user_id = :userId")
    void deletePostsByUser(long userId);

    @Update
    void updatePost(Posts post);
}
