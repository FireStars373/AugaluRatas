package com.example.augaluratas;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "users")
public class Users {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "subscribed")
    private Boolean subscribed;

    public Users(@NonNull String username, @NonNull String password, @NonNull String phoneNumber, @NonNull String email) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        subscribed = false;
    }

    public void setId(@NonNull long id) {this.id = id;}
    public long getId() {return this.id;}
    public void setUsername(@NonNull String username) {this.username = username;}
    public String getUsername() {return this.username;}
    public void setPassword(@NonNull String password) {this.password =password;}
    public String getPassword() { return this.password;}
    public void setPhoneNumber(@NonNull String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public void setEmail(@NonNull String email) {this.email = email;}
    public String getEmail() {return this.email;}

    public void setSubscribed(Boolean subscribed) {this.subscribed = subscribed;}
    public Boolean getSubscribed(){return subscribed;}
}
