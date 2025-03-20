package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Notifications extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox viewed = findViewById(R.id.notification_post_viewed);
        CheckBox shared = findViewById(R.id.notification_post_shared);
        CheckBox liked = findViewById(R.id.notification_post_liked);
        CheckBox purchased = findViewById(R.id.notification_plant_purchased);
        ImageButton return_button = findViewById(R.id.return_from_notifications);

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.NOTIFICATION_SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //Displaying settings
        Boolean a  =sharedPref.getBoolean("viewed", true);
        viewed.setChecked(sharedPref.getBoolean("viewed", true));
        shared.setChecked(sharedPref.getBoolean("shared", true));
        liked.setChecked(sharedPref.getBoolean("liked", true));
        purchased.setChecked(sharedPref.getBoolean("purchased", true));


        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Setting new settings
        viewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("viewed", isChecked);
                editor.apply();
            }
        });
        shared.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("shared", isChecked);
                editor.apply();
            }
        });
        liked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("liked", isChecked);
                editor.apply();
            }
        });
        purchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("purchased", isChecked);
                editor.apply();
            }
        });
    }
}