package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserData extends AppCompatActivity {

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

        ImageButton return_button = findViewById(R.id.return_from_user_data);
        TextView name = findViewById(R.id.user_data_name);
        TextView email = findViewById(R.id.user_data_email);
        TextView password = findViewById(R.id.user_data_password);
        TextView phone_number = findViewById(R.id.user_data_phone_number);
        Button change_data = findViewById(R.id.change_user_data);

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        UsersDatabase database = AppActivity.getUsersDatabase();
        Users user = database.usersDAO().getUserById(current_id);
        name.setText(user.getUsername());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        phone_number.setText(user.getPhoneNumber());

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Number = phone_number.getText().toString().trim();
                if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || Number.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Visi laukai turi būti išpildyti", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Password.length() < 5 || !Password.matches(".*\\d.*")){
                    Toast.makeText(getApplicationContext(), "Slaptažodį turi sudaryti bent 5 simboliai, su bent vienu skaičiu", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(getApplicationContext(), "Neteisingas el. pašto formatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.PHONE.matcher(Number).matches()){
                    Toast.makeText(getApplicationContext(), "Neteisingas tel. numerio formatas", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(database.usersDAO().getUserByUsername(Name) != null && !database.usersDAO().getUserByUsername(Name).getUsername().equals(Name)){
                    Toast.makeText(getApplicationContext(), "Jau yra vartotojas su šiuo vardu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(database.usersDAO().getUserByEmail(Email) != null && !database.usersDAO().getUserByUsername(Name).getEmail().equals(Email)){
                    Toast.makeText(getApplicationContext(), "Šis el. paštas jau panaudotas", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users user = database.usersDAO().getUserById(current_id);
                user.setUsername(Name);
                user.setEmail(Email);
                user.setPassword(Password);
                user.setPhoneNumber(Number);
                database.usersDAO().Update(user);
                Toast.makeText(getApplicationContext(), "Duomenys sėkmingai pakeisti!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}