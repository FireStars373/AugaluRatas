package com.example.augaluratas;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(
        tableName = "user_post_likes",
        primaryKeys = {"user_id", "post_id"},
        foreignKeys = {
            @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(
                entity = Posts.class,
                parentColumns = "id",
                childColumns = "post_id",
                onDelete = ForeignKey.CASCADE
            )
        }
)
public class UserPostLikes {

    @ColumnInfo(name = "user_id")
    private long userId;
    @ColumnInfo(name = "post_id")
    private long postId;

    public UserPostLikes(@NonNull long userId, @NonNull long postId) {
        this.postId = postId;
        this.userId = userId;
    }

    public void setPostId(@NonNull long postId){this.postId = postId;}
    public long getPostId(){return postId;}
    public void setUserId(@NonNull long userId){this.userId = userId;}
    public long getUserId(){return userId;}

}
