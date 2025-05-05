package com.example.augaluratas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;
import android.widget.Toast;

public class AllPlants extends BaseActivity {
    private LinearLayout plantsContainer;
    private PlantsDatabase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plants);

        plantsContainer = findViewById(R.id.plantsContainer);
        database = PlantsDatabase.getDatabase(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bananmedis);
        if (bitmap == null) {
            Log.e("BitmapError", "Nepavyko užkrauti lotus_icon");
            return;
        }
        byte[] imageBytes = ImageUtils.bitmapToByteArray(bitmap);
        executorService.execute(() -> {
            database.plantsDAO().insert(new Plants("Aguona", "Reikia daug laistyti", "Latvija", imageBytes));
            database.plantsDAO().insert(new Plants("Bananmedis", "Daug saules reikia", "Azija", imageBytes));
            database.plantsDAO().insert(new Plants("Tikras medis", "Daug saules reikia", "Azija", imageBytes));
        });

        loadPlants();

        ImageButton menu = findViewById(R.id.sidebar_from_all_plants);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
    }
    private void loadPlants() {
        executorService.execute(() -> {
            // Užklausa į duomenų bazę
            List<Plants> plantList = database.plantsDAO().getAllPlants();
            if (plantList == null || plantList.isEmpty()) return;

            // Naudojame LinkedHashSet, kad pašalintume pasikartojančius pavadinimus
            Set<String> uniquePlantNames = new LinkedHashSet<>();
            for (Plants plant : plantList) {
                uniquePlantNames.add(plant.getName());
            }

            // Paverčiame atgal į sąrašą ir surūšiuojame pagal pavadinimą
            List<String> sortedPlantNames = new ArrayList<>(uniquePlantNames);
            Collections.sort(sortedPlantNames, String.CASE_INSENSITIVE_ORDER);

            // Atliksime UI atnaujinimus pagrindiniame gije
            runOnUiThread(() -> {
                plantsContainer.removeAllViews(); // Išvalome senus duomenis
                char lastLetter = '\0'; // Laikome paskutinę įtrauktą raidę

                for (String plantName : sortedPlantNames) {
                    char firstLetter = Character.toUpperCase(plantName.charAt(0));

                    // Pridėti naują raidės antraštę, jei ši raidė dar nebuvo pridėta
                    if (firstLetter != lastLetter) {
                        TextView letterHeader = new TextView(this);
                        letterHeader.setText(String.valueOf(firstLetter));
                        letterHeader.setTextSize(24);
                        letterHeader.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
                        letterHeader.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc), Typeface.BOLD);
                        plantsContainer.addView(letterHeader);
                        lastLetter = firstLetter;
                    }

                    // Sukuriame Button kiekvienam unikaliam augalui
                    Button plantButton = new Button(this);
                    plantButton.setText(plantName);
                    plantButton.setTextSize(20);
                    plantButton.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
                    plantButton.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc));
                    plantButton.setBackgroundColor(Color.TRANSPARENT); // Be fono
                    plantButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    plantButton.setOnClickListener(v -> {
                        // Užklausa į duomenų bazę, kad gauti konkretaus augalo informaciją
                        executorService.execute(() -> {
                            Plants selectedPlant = database.plantsDAO().getPlantByName(plantName);
                            if (selectedPlant != null) {
                                runOnUiThread(() -> {
                                    // Pereiti į kitą aktyvumą su pasirinktu augalu
                                    Intent intent = new Intent(getBaseContext(), PlantDescription.class);
                                    intent.putExtra("augalas", selectedPlant);
                                    startActivity(intent);
                                });
                            }
                        });
                    });

                    plantsContainer.addView(plantButton);
                }
            });
        });
    }
}
