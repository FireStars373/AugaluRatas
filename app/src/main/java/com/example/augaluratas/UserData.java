package com.example.augaluratas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserData extends AppCompatActivity {

    private final ImageButton return_button = findViewById(R.id.return_from_user_data);
    private final TextView name = findViewById(R.id.user_data_name);
    private final TextView email = findViewById(R.id.user_data_email);
    private final TextView password = findViewById(R.id.user_data_password);
    private final TextView phone_number = findViewById(R.id.user_data_phone_number);
    private final Button change_data = findViewById(R.id.change_user_data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //(FUTURE) Set data from database

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) set data to database
            }
        });

    }
}