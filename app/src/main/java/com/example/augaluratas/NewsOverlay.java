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
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class NewsOverlay extends BaseActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_overlay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_news_overlay);
        TextView headline = findViewById(R.id.news_headline);
        TextView main_text = findViewById(R.id.news_main_text);
        TextView full_text = findViewById(R.id.news_full_text);
        ImageView photo = findViewById(R.id.news_image);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        String newsId = getIntent().getStringExtra("id"); // Firestore dokumento ID

        if (newsId == null) {
            Toast.makeText(this, "Naujienos ID nerastas", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("news").document(newsId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String DBheadline = document.getString("headline");
                        String DBmainText = document.getString("summary");
                        String DBfullText = document.getString("text");
                        String imagePath = document.getString("imageUrl"); // Storage path

                        headline.setText(DBheadline);
                        main_text.setText(DBmainText);
                        full_text.setText(DBfullText);

                        Glide.with(this)
                                .load(imagePath)
                                .into(photo);
                        /*if (imagePath != null && !imagePath.isEmpty()) {
                            StorageReference imageRef = storage.getReference().child(imagePath);
                            final long ONE_MB = 1024 * 1024;
                            imageRef.getBytes(ONE_MB).addOnSuccessListener(bytes -> {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                photo.post(() -> photo.setImageBitmap(Bitmap.createScaledBitmap(
                                        bmp, photo.getWidth(), photo.getHeight(), false)));
                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, "Nepavyko įkelti nuotraukos", Toast.LENGTH_SHORT).show();
                            });
                        }*/

                    } else {
                        Toast.makeText(this, "Dokumentas nerastas", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Klaida gaunant duomenis", Toast.LENGTH_SHORT).show();
                    finish();
                });

        return_button.setOnClickListener(v -> finish());
    }
}