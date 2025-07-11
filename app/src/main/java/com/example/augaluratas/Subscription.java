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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Subscription extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        User_PostDatabase database = AppActivity.getUser_PostDatabase();
        UserSettings settings = database.userSettingsDAO().getByUserId(current_id);

        ImageButton sidebar = findViewById(R.id.sidebar_from_subscription);
        Button subscribe = findViewById(R.id.subscribe);

        if(settings.getSubscribed()){
            Intent intent = new Intent(getBaseContext(), SubscriptionSuccess.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        }

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore dbb = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                dbb.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                    dbb.collection("users").document(currentUser.getUid()).update("subscribed", true);
                    settings.setSubscribed(true);
                    database.userSettingsDAO().Update(settings);
                    Intent intent = new Intent(getBaseContext(), SubscriptionSuccess.class);
                    startActivity(intent);
                });
            }
        });
    }
}