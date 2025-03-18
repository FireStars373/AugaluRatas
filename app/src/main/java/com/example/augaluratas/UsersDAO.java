package com.example.augaluratas;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

@Dao
public interface UsersDAO {
    @Insert
    void insert(Users user);

    @Query("SELECT * FROM users")
    List<Users> getAllUsers();

    @Delete
    void delete(Users user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    Users getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);

    @Update
    void Update(Users user);
}
