package com.example.augaluratas;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class News {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "headline")
    private String headline;

    @NonNull
    @ColumnInfo(name = "main_text")
    private String main_text;

    @NonNull
    @ColumnInfo(name = "full_text")
    private String full_text;

    @NonNull
    @ColumnInfo(name = "image")
    private byte[] image;

    public News(String headline, String main_text, String full_text, byte[] image){
        this.headline = headline;
        this.main_text = main_text;
        this.full_text = full_text;
        this.image = image;
    }

    public long getId() {return id;}
    public void setId(long id){this.id = id;}
    public String getHeadline(){return headline;}
    public void setHeadline(String headline){this.headline = headline;}
    public String getMain_text(){return main_text;}
    public void setMain_text(String main_text){this.main_text = main_text;}
    public String getFull_text(){return full_text;}
    public void setFull_text(String full_text){this.full_text = full_text;}
    public byte[] getImage(){return image;}
    public void setImage(byte[] image){this.image = image;}


}
