package com.example.augaluratas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubscriptionSuccess extends AppCompatActivity {

    private final ImageButton sidebar = findViewById(R.id.sidebar_from_subscription_success);
    private final ImageButton return_button = findViewById(R.id.return_from_subscription_success);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscription_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
            }
        });
    }
}