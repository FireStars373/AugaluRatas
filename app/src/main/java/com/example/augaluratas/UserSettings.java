package com.example.augaluratas;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(
        tableName = "user_settings",
        foreignKeys = @ForeignKey(
                entity = Users.class,  // Ryšys su Users lentele
                parentColumns = "id",  // Parent stulpelis (Users.id)
                childColumns = "user_id",  // Child stulpelis (Posts.user_id)
                onDelete = ForeignKey.CASCADE  // Jei vartotojas ištrinamas, ištrinami ir susiję įrašai
        )
)
public class UserSettings {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "user_id")
    private long userId;

    @ColumnInfo(name = "subscribed")
    private Boolean subscribed;
    @ColumnInfo(name = "currency")
    private String currency;
    //Notifs
    @ColumnInfo(name = "post_viewed")
    private Boolean postViewed;
    @ColumnInfo(name = "post_shared")
    private Boolean postShared;
    @ColumnInfo(name = "post_liked")
    private Boolean postLiked;
    @ColumnInfo(name = "post_bought")
    private Boolean postBought;

    public UserSettings(@NonNull long userId) {
        this.userId = userId;
        subscribed = false;
        postViewed = true;
        postShared = true;
        postLiked = true;
        postBought = true;
    }

    public long getId() {return id;}
    public void setId(long id){this.id = id;}

    public long getUserId() {return userId;}
    public void setUserId(long userId){this.userId = userId;}
    public Boolean getSubscribed(){return subscribed;}
    public void setSubscribed(Boolean subscribed){this.subscribed = subscribed;}
    public Boolean getPostViewed(){return postViewed;}
    public void setPostViewed(Boolean postViewed){this.postViewed = postViewed;}
    public Boolean getPostShared(){return postShared;}
    public void setPostShared(Boolean postShared){this.postShared = postShared;}
    public Boolean getPostLiked(){return postLiked;}
    public void setPostLiked(Boolean postLiked){this.postLiked = postLiked;}
    public Boolean getPostBought(){return postBought;}
    public void setPostBought(Boolean postBought){this.postBought = postBought;}
    public String getCurrency(){return currency;}
    public void setCurrency(String currency){this.currency = currency;}
}
