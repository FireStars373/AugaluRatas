package com.example.augaluratas;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "augalai")
public class Plants implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "plant_name")
    private String name;
    private String idW;
    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;

    @NonNull
    @ColumnInfo(name = "origin")
    private String origin;

    @NonNull
    @ColumnInfo(name = "image")
    private byte[] image;

    public Plants(@NonNull String name, @NonNull String description, @NonNull String origin, @NonNull byte[] image) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.image = image;
        this.category = name.substring(0, 1).toUpperCase(); // Pasiima pirmąją raidę ir paverčia ją didžiąja
    }
    // Getteriai ir setteriai

    public Plants() {
        this.name = "";
        this.description = "";
        this.category = "";
        this.origin = "";
        this.image = new byte[0];
    }
    public Plants(Parcel in) {
        name = in.readString();
        description = in.readString();
        category = in.readString();
        origin = in.readString();
        image = in.createByteArray();
    }

    public static final Creator<Plants> CREATOR = new Creator<Plants>() {
        @Override
        public Plants createFromParcel(Parcel in) {
            return new Plants(in);
        }

        @Override
        public Plants[] newArray(int size) {
            return new Plants[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(origin);
        dest.writeByteArray(image);
    }
    public String getIdW() { return idW; }
    public void setIdW(String idW) { this.idW = idW; }
    public void setId(@NonNull long name) {this.id = id;}
    public long getId() {return this.id;}
    public void setName(@NonNull String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setDescription(@NonNull String description) {this.description =description;}
    public String getDescription() { return this.description;}
    public void setCategory(@NonNull String category) {this.category = category;}
    public String getCategory() {return  this.category;}

    public void setOrigin(@NonNull String origin) {this.origin = origin;}
    public String getOrigin() {return this.origin;}
    public void setImage(@NonNull byte[] image) {this.image = image;}
    public byte[] getImage() {return this.image;}
}
