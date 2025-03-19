package com.example.augaluratas;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(
        tableName = "posts",
        foreignKeys = @ForeignKey(
                entity = Users.class,  // Ryšys su Users lentele
                parentColumns = "id",  // Parent stulpelis (Users.id)
                childColumns = "user_id",  // Child stulpelis (Posts.user_id)
                onDelete = ForeignKey.CASCADE  // Jei vartotojas ištrinamas, ištrinami ir susiję įrašai
        )
)
public class Posts {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "user_id")
    private long userId;  // Užsienio raktas į Users lentelę

    @NonNull
    @ColumnInfo(name = "plant_name")
    private String plantName;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "image")
    private byte[] image;

    @NonNull
    @ColumnInfo(name = "price")
    double price;

    public Posts(@NonNull long userId, @NonNull String plantName, @NonNull String description, @NonNull byte[] image, @NonNull double price) {
        this.plantName = plantName;
        this.userId = userId;
        this.description = description;
        this.image = image;
        this.price = price;
    }


    public void setId(@NonNull long id) {this.id = id;}
    public long getId() {return this.id;}
    public void setUserId(@NonNull long userId) {this.userId = userId;}
    public long getUserId() {return this.userId;}
    public void setPlantName(@NonNull String plantName) {this.plantName = plantName;}
    public String getPlantName() {return this.plantName;}
    public void setImage(@NonNull byte[] image) {this.image = image;}
    public byte[] getImage() {return this.image;}
    public void setPrice(@NonNull double price) {this.price = price;}
    public double getPrice() {return this.price;}
    public void setDescription(@NonNull String description) {this.description = description;}
    public String getDescription() {return this.description;}
}
