package com.example.augaluratas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AllPlants extends AppCompatActivity {
    private PlantsDatabase db;
    private PlantsDAO plantsDAO;
    private LinearLayout plantListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plants);

        db = PlantsDatabase.getDatabase(getApplicationContext());
        plantsDAO = db.plantsDAO();

        Plants plant = new Plants("Rožė", "Graži gėlė");
        new Thread(() -> {
            plantsDAO.insert(plant);
        }).start();

        plantListLayout = findViewById(R.id.plant_list_layout);

        loadPlants();
    }

    private void loadPlants() {
        List<Plants> plants = plantsDAO.getAllPlants();
        String currentCategory = "";

        for (Plants plant : plants) {
            // Add category button if a new category is encountered
            if (!plant.getCategory().equals(currentCategory)) {
                currentCategory = plant.getCategory();
                Button categoryButton = new Button(this);
                categoryButton.setText(currentCategory);
                categoryButton.setEnabled(false);
                plantListLayout.addView(categoryButton);
            }

            // Add plant button
            Button plantButton = new Button(this);
            plantButton.setText(plant.getName());
            plantButton.setOnClickListener(v -> openPlantDetails(plant.getName()));
            plantListLayout.addView(plantButton);
        }
    }

    private void openPlantDetails(String plantName) {
        Intent intent = new Intent(this, plant_maintanance.class);
        intent.putExtra("PLANT_NAME", plantName);
        startActivity(intent);
    }

}