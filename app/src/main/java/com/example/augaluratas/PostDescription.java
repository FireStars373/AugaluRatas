package com.example.augaluratas;

import android.content.Intent;
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

public class PostDescription extends AppCompatActivity {
    private final ImageButton return_button = findViewById(R.id.return_from_post_description);
    private final ImageButton sidebar = findViewById(R.id.sidebar_from_post_description);
    private final ImageView photo = findViewById(R.id.post_description_photo);
    private final TextView title = findViewById(R.id.post_description_title);
    private final TextView description = findViewById(R.id.post_description_description);
    private final TextView price = findViewById(R.id.post_description_price);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //(FUTURE) Set variables from database

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
    }
}