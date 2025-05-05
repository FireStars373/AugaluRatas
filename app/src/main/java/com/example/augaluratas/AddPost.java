package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class AddPost extends AppCompatActivity {

    private User_PostDatabase database;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedImageBitmap = null;
    private ImageView select_photo;
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
        MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.bad_info);
        mp.setVolume(0.8f,0.8f);
        MediaPlayer mp2 = MediaPlayer.create(getBaseContext(), R.raw.succesful);
        mp2.setVolume(0.5f,0.5f);
        select_photo = findViewById(R.id.button24);

        select_photo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
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
                    upload_post.setSoundEffectsEnabled(false);
                    mp.start();
                    return;
                }

                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                Long currentUserId = sharedPref.getLong("current_user_id", 0);
                if (selectedImageBitmap == null) {
                    Toast.makeText(getApplicationContext(), "Pasirinkite nuotrauką", Toast.LENGTH_SHORT).show();
                    upload_post.setSoundEffectsEnabled(false);
                    mp.start();
                    return;
                }
                Bitmap bitmap = selectedImageBitmap;
                byte[] imageBytes = ImageUtils.bitmapToByteArray(bitmap);
                Posts post = new Posts(currentUserId, Title, Description, imageBytes, Double.parseDouble(Price));
                database = AppActivity.getUser_PostDatabase();
                database.postsDAO().insert(post);


                Intent intent = new Intent(getBaseContext(), AllPosts.class);
                mp2.start();

                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                select_photo.setImageBitmap(selectedImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Klaida įkeliant nuotrauką", Toast.LENGTH_SHORT).show();
            }
        }
    }

}