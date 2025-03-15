package com.example.augaluratas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgottenPassword extends AppCompatActivity {

    private EditText email = (EditText) findViewById(R.id.forgotten_password_email);
    private Button send = (Button) findViewById(R.id.forgotten_password_send);
    private Button go_back = (Button) findViewById(R.id.return_from_forgotten_password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotten_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                if(Email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Turi būti įrašytas el. paštas", Toast.LENGTH_SHORT).show();
                    return;
                }
                //(FUTURE) Check if email is in database
                //(FUTURE) If yes, send email for recovery
            }
        });
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}