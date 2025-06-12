package com.example.augaluratas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PlantDescription extends BaseActivity {

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_description);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // UI references
        ImageButton returnButton = findViewById(R.id.return_from_plant_description);
        TextView tvName        = findViewById(R.id.textView18);
        TextView tvDescription = findViewById(R.id.textView78);
        TextView tvOrigin      = findViewById(R.id.textView21);
        ImageView ivPlant      = findViewById(R.id.imageView6);

        returnButton.setOnClickListener(v -> finish());

        // Init Firebase
        db      = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Gauti plantId iš Intent
        String plantId = getIntent().getStringExtra("plantId");
        if (plantId == null) {
            Toast.makeText(this, "Nėra augalo ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Skaitome dokumentą
        db.collection("plants").document(plantId).get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(this, "Augalas nerastas", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    // Užpildome laukus
                    String name        = doc.getString("name");
                    String description = doc.getString("description");
                    String origin      = doc.getString("origin");
                    String imageUrl    = doc.getString("imageUrl");

                    tvName.setText(name);
                    tvDescription.setText(description);
                    tvOrigin.setText(origin);

                    // Krauname paveikslėlį per Glide
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.alokazija_polly)
                                .into(ivPlant);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Klaida gaunant augalo duomenis", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}