package com.example.augaluratas;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

@Dao
public interface UserPostLikesDAO {
    @Insert
    void insert(UserPostLikes like);
    @Delete
    void delete(UserPostLikes like);

    @Query("SELECT * FROM posts INNER JOIN user_post_likes L ON L.post_id = id WHERE L.user_id = :user_id")
    List<Posts> getAllUserLikes(long user_id);
    @Query("SELECT * FROM user_post_likes WHERE user_id =:user_id AND post_id =:post_id")
    UserPostLikes getSpecificLike(long user_id, long post_id);
    @Query("SELECT * FROM user_post_likes")
    List<UserPostLikes> getAll();

}
