package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserPostOverlay extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_post_overlay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_user_post_overlay);
        ImageView photo = findViewById(R.id.user_post_photo);
        EditText title = findViewById(R.id.user_post_title);
        EditText description = findViewById(R.id.user_post_description);
        EditText price = findViewById(R.id.user_post_price);
        Button change = findViewById(R.id.user_post_change);

        //(FUTURE) Set data from database
        User_PostDatabase db = AppActivity.getUser_PostDatabase();
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
        }

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
//                startActivity(intent);
                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Photo selector
                String Title = title.getText().toString().trim();
                String Description = description.getText().toString().trim();
                String Price = price.getText().toString().trim();

                if (Title.isEmpty() || Description.isEmpty() || Price.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Laukai negali būti tušti", Toast.LENGTH_SHORT).show();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(change, "translationX",  0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
                    animator.setDuration(600);

                    AnimatorSet set = new AnimatorSet();
                    set.playSequentially(animator);
                    set.start();
                    return;
                }

                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                Long currentUserId = sharedPref.getLong("current_user_id", 0);

                Posts post = db.postsDAO().getPostById(id);
                post.setPlantName(Title);
                post.setDescription(Description);
                String cleaned = Price.replaceAll("[^\\d.]", "");
                post.setPrice(Double.parseDouble(cleaned));
                db.postsDAO().updatePost(post);
                Toast.makeText(getBaseContext(), "Sėkmingai pakeistas turinys!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}