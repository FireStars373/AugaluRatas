package com.example.augaluratas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_add_post);
        Button upload_post = findViewById(R.id.upload_post);
        EditText title = findViewById(R.id.add_post_title);
        EditText description = findViewById(R.id.add_post_description);
        EditText price = findViewById(R.id.add_post_price);

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking field validity
                String Title = title.getText().toString().trim();
                String Description = description.getText().toString().trim();
                String Price = price.getText().toString().trim();
                if (Title.isEmpty() || Description.isEmpty() || Price.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Laukai negali būti tušti", Toast.LENGTH_SHORT).show();
                    return;
                }
                //(FUTURE)New post is uploaded to database

                Intent intent = new Intent(getBaseContext(), AllPosts.class);
                startActivity(intent);
            }
        });
    }
}