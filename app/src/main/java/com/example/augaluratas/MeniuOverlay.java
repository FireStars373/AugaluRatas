package com.example.augaluratas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        android.widget.ImageButton remove_sidebar = (android.widget.ImageButton) findViewById(R.id.remove_sidebar);
        Button main_menu = (Button) findViewById(R.id.sidebar_main_page);
        Button plant_gallery = (Button) findViewById(R.id.sidebar_all_posts);
        Button upload_plant = (Button) findViewById(R.id.sidebar_add_post);
        Button plant_maintenance = (Button) findViewById(R.id.sidebar_all_plants);
        Button cart = (Button) findViewById(R.id.sidebar_cart_list);
        Button account = (Button) findViewById(R.id.sidebar_user_profile);
        Button subscribe = (Button) findViewById(R.id.sidebar_subscription);
        Button logout = (Button) findViewById(R.id.sidebar_logout);

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
                Intent intent = new Intent(getBaseContext(), Subscription.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FirstLoadScreen.class);
                startActivity(intent);
            }
        });
    }
}