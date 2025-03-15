package com.example.augaluratas;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "augalai")
public class Plants {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "plant_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;

    public Plants(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
        this.category = name.substring(0, 1).toUpperCase(); // Pasiima pirmąją raidę ir paverčia ją didžiąja
    }
    public void setId(@NonNull long name) {this.id = id;}
    public long getId() {return this.id;}
    public void setName(@NonNull String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setDescription(@NonNull String description) {this.description =description;}
    public String getDescription() { return this.description;}
    public void setCategory(@NonNull String category) {this.category = category;}
    public String getCategory() {return  this.category;}
}
