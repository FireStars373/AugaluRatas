package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        Button login = findViewById(R.id.login);
        Button return_to_first_screen = findViewById(R.id.return_to_first_screen_from_login);
        TextView forgotten_password = findViewById(R.id.to_forgotten_password);
        EditText name = findViewById(R.id.login_name);
        EditText password = findViewById(R.id.login_password);

        UsersDatabase usersDatabase = AppActivity.getUsersDatabase();


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
                Users user = usersDatabase.usersDAO().getUserByUsername(Name);
                if(user == null || !user.getPassword().equals(Password)){
                    Toast.makeText(getApplicationContext(), "Vartotojo vardas arba slaptažodis neteisingas", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_user_username", Name);
                editor.apply();
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