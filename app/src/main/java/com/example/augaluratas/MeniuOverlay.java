package com.example.augaluratas;

import android.content.Intent;
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

    private ImageButton remove_sidebar = (ImageButton) findViewById(R.id.remove_sidebar);
    private Button main_menu = (Button) findViewById(R.id.sidebar_main_page);
    private Button plant_gallery = (Button) findViewById(R.id.sidebar_all_posts);
    private Button upload_plant = (Button) findViewById(R.id.sidebar_add_post);
    private Button plant_maintenance = (Button) findViewById(R.id.sidebar_all_plants);
    private Button cart = (Button) findViewById(R.id.sidebar_cart_list);
    private Button account = (Button) findViewById(R.id.sidebar_user_profile);
    private Button subscribe = (Button) findViewById(R.id.sidebar_subscription);
    private Button logout = (Button) findViewById(R.id.sidebar_logout);

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
                Intent intent = new Intent(getBaseContext(), FirstLoadScreen.class);
                startActivity(intent);
            }
        });
    }
}