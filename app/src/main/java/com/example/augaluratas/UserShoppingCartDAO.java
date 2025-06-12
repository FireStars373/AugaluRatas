package com.example.augaluratas;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

@Dao
public interface UserShoppingCartDAO {
    @Insert
    void insert(UserShoppingCart cart_item);
    @Delete
    void delete(UserShoppingCart cart_item);

    @Query("SELECT * FROM posts INNER JOIN user_shopping_cart L ON L.post_id = id WHERE L.user_id = :user_id")
    List<Posts> getAllUserItems(long user_id);
    @Query("SELECT * FROM posts INNER JOIN user_shopping_cart L ON L.post_id = id WHERE L.user_id = :user_id AND buying = true")
    List<Posts> getAllUserBuyingItems(long user_id);
    @Query("SELECT * FROM user_shopping_cart WHERE user_id =:user_id AND post_id =:post_id")
    UserShoppingCart getSpecificItem(long user_id, long post_id);
    @Update
    void Update(UserShoppingCart cart_item);
}
