package com.example.augaluratas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private final Button login = findViewById(R.id.login);
    private final Button return_to_first_screen = findViewById(R.id.return_to_first_screen_from_login);
    private final TextView forgotten_password = findViewById(R.id.to_forgotten_password);
    private final EditText name = findViewById(R.id.login_name);
    private final EditText password = findViewById(R.id.login_password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Password = password.getText().toString().trim();
                if (Name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Neįrašytas vardas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Neįrašytas slaptažodis", Toast.LENGTH_SHORT).show();
                    return;
                }
                //(FUTURE) Check if name and password is in database
                Intent intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
            }
        });
        return_to_first_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgotten_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForgottenPassword.class);
                startActivity(intent);
            }
        });
    }
}