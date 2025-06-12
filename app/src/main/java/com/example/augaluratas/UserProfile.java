package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

public class UserProfile extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_user_profile);
        ImageView user_photo = findViewById(R.id.user_image);
        Button favorite_posts = findViewById(R.id.favorite_posts);
        Button user_posts = findViewById(R.id.user_profile_user_posts);
        Button settings = findViewById(R.id.user_profile_settings);
        Button delete_user = findViewById(R.id.user_profile_delete_user);
        TextView username = findViewById(R.id.user_profile_username);


        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id= sharedPref.getLong("current_user_id", 0);
        User_PostDatabase database = AppActivity.getUser_PostDatabase();
        Users user = database.usersDAO().getUserById(current_id);
        username.setText(user.getUsername());
        if(user.getImage() == null){
            user_photo.setImageResource(R.drawable.user_icon);
            user_photo.setColorFilter(ContextCompat.getColor(this, R.color.lotus_green), PorterDuff.Mode.SRC_IN);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            user_photo.setImageBitmap(bitmap);
        }

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
        user_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserPosts.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Settings.class);
                startActivity(intent);
            }
        });
        favorite_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FavoritePosts.class);
                startActivity(intent);
            }
        });
        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deleting from database
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                Long current_id = sharedPref.getLong("current_user_id", 0);
                Users user = database.usersDAO().getUserById(current_id);
                database.usersDAO().delete(user);
                //Signing out
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("current_user_id", 0);
                editor.apply();
                Intent intent = new Intent(getBaseContext(), FirstLoadScreen.class);
                startActivity(intent);
            }
        });
    }
}