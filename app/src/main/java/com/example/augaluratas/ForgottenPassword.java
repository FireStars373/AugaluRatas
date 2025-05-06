package com.example.augaluratas;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgottenPassword extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotten_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText email = findViewById(R.id.forgotten_password_email);
        Button send = findViewById(R.id.forgotten_password_send);
        Button go_back = findViewById(R.id.return_from_forgotten_password);
        User_PostDatabase database = AppActivity.getUser_PostDatabase();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                if(Email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Turi būti įrašytas el. paštas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(getApplicationContext(), "Neteisingas el. pašto formatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(database.usersDAO().getUserByEmail(Email) == null){
                    Toast.makeText(getApplicationContext(), "Registruoto vartotojo su tokiu el. paštu nėra", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Sėkmingai išsiūstas laiškas!", Toast.LENGTH_SHORT).show();
                //(FUTURE) Send email for recovery
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