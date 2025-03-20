package com.example.augaluratas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlantDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageButton return_button = findViewById(R.id.return_from_plant_description);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        TextView plantName = findViewById(R.id.textView18);
        TextView description = findViewById(R.id.textView78);
        TextView origin = findViewById(R.id.textView21);
        ImageView image = findViewById(R.id.imageView6);

        Plants plant = getIntent().getParcelableExtra("augalas");
        if (plant != null) {
            plantName.setText(plant.getName());
            description.setText(plant.getDescription());
            origin.setText(plant.getOrigin());
            byte[] imageBytes = plant.getImage(); // Gauname byte[] iš duomenų bazės
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(bitmap);
        }
    }
}