package com.example.augaluratas;

import android.content.Intent;
import android.media.MediaPlayer;
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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button register = findViewById(R.id.register);
        Button return_to_first_screen = findViewById(R.id.return_to_first_screen_from_register);
        EditText name = findViewById(R.id.register_name);
        EditText email = findViewById(R.id.register_email);
        EditText password = findViewById(R.id.register_password);
        EditText repeat_password = findViewById(R.id.register_password_repeat);
        EditText number = findViewById(R.id.register_phone_number);

        User_PostDatabase usersDatabase = AppActivity.getUser_PostDatabase();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Repeat_password = repeat_password.getText().toString().trim();
                String Number = number.getText().toString().trim();
                MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.bad_info);
                mp.setVolume(0.8f,0.8f);
                if ( Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || Repeat_password.isEmpty() || Number.isEmpty()){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Visi laukai turi būti išpildyti", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Password.length() < 5 || !Password.matches(".*\\d.*")){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Slaptažodį turi sudaryti bent 5 simboliai, su bent vienu skaičiu", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Password.equals(Repeat_password)){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Slaptažodžiai nesutampa", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Neteisingas el. pašto formatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.PHONE.matcher(Number).matches()){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Neteisingas tel. numerio formatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(usersDatabase.usersDAO().getUserByUsername(Name) != null){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Jau yra vartotojas su šiuo vardu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(usersDatabase.usersDAO().getUserByEmail(Email) != null){
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Šis el. paštas jau panaudotas", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users user = new Users(Name, Password, Number, Email);
                usersDatabase.usersDAO().insert(user);
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
            }
        });
        return_to_first_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}