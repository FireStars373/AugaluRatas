package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MeniuOverlay extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meniu_overlay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         ImageButton remove_sidebar = findViewById(R.id.remove_sidebar);
         Button main_menu = findViewById(R.id.sidebar_main_page);
         Button plant_gallery = findViewById(R.id.sidebar_all_posts);
         Button upload_plant = findViewById(R.id.sidebar_add_post);
         Button plant_maintenance = findViewById(R.id.sidebar_all_plants);
         Button cart = findViewById(R.id.sidebar_cart_list);
         Button account = findViewById(R.id.sidebar_user_profile);
         Button subscribe = findViewById(R.id.sidebar_subscription);
         Button logout = findViewById(R.id.sidebar_logout);

        remove_sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
            }
        });
        plant_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AllPosts.class);
                startActivity(intent);
            }
        });
        upload_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddPost.class);
                startActivity(intent);
            }
        });
        plant_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AllPlants.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ShoppingCartList.class);
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserProfile.class);
                startActivity(intent);
            }
        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Check if user is subscribed
//                if (subscribed){
//                    Intent intent = new Intent(getBaseContext(), SubscriptionSuccess.class);
//                    startActivity(intent);
//                    return;
//                }
                Intent intent = new Intent(getBaseContext(), Subscription.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removing current user
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_user_username", getString(R.string.username));
                editor.apply();
                Intent intent = new Intent(getBaseContext(), FirstLoadScreen.class);
                startActivity(intent);
            }
        });
    }
}