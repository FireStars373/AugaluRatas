package com.example.augaluratas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostDescription extends AppCompatActivity {

    private User_PostDatabase db;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_description);
        NotificationHandler.createNotificationChannel(PostDescription.this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_post_description);
        ImageView photo = findViewById(R.id.post_description_photo);
        TextView title = findViewById(R.id.post_description_title);
        TextView description = findViewById(R.id.post_description_description);
        TextView price = findViewById(R.id.post_description_price);


        //(FUTURE) Set variables from database
        db = AppActivity.getUser_PostDatabase();
        long id = getIntent().getLongExtra("POST_ID", -1);
        if (id != -1) {
            Posts post = db.postsDAO().getPostById(id);
            if (post != null) {

                byte[] imageBytes = db.postsDAO().getPostById(id).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                photo.setImageBitmap(bitmap);
                title.setText(post.getPlantName());
                description.setText(post.getDescription());
                price.setText(String.format("%.2f €", post.getPrice()));
            }
            sidebar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), AllPosts.class);
                    startActivity(intent);
                }
            });

            //Notification when someone hearts your post
            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});
            ImageButton heart = findViewById(R.id.imageButton);
            heart.setOnClickListener(v -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    } else {
                        NotificationHandler.sendNotification(PostDescription.this, "", "Jūsų skelbimas buvo įsimintas!", PostDescription.class, 1);
                    }
                }
            });
        }
    }
}