package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends BaseActivity {

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Repeat_password = repeat_password.getText().toString().trim();
                String Number = number.getText().toString().trim();

                ObjectAnimator animator = ObjectAnimator.ofFloat(register, "translationX", 0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
                animator.setDuration(600);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(animator);


                MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.bad_info);
                mp.setVolume(0.8f, 0.8f);

                if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || Repeat_password.isEmpty() || Number.isEmpty()) {
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Visi laukai turi būti išpildyti", Toast.LENGTH_SHORT).show();
                    set.start();
                    return;
                }
                if (Password.length() < 5 || !Password.matches(".*\\d.*")) {
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Slaptažodį turi sudaryti bent 5 simboliai, su bent vienu skaičiu", Toast.LENGTH_LONG).show();
                    set.start();
                    return;
                }
                if (!Password.equals(Repeat_password)) {
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Slaptažodžiai nesutampa", Toast.LENGTH_SHORT).show();
                    set.start();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Neteisingas el. pašto formatas", Toast.LENGTH_SHORT).show();
                    set.start();
                    return;
                }
                if (!Patterns.PHONE.matcher(Number).matches()) {
                    register.setSoundEffectsEnabled(false);
                    mp.start();
                    Toast.makeText(getApplicationContext(), "Neteisingas tel. numerio formatas", Toast.LENGTH_SHORT).show();
                    set.start();
                    return;
                }
                // 1. Tikriname, ar vardas užimtas Firestore
                db.collection("users").whereEqualTo("username", Name).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            register.setSoundEffectsEnabled(false);
                            mp.start();
                            Toast.makeText(getApplicationContext(), "Jau yra vartotojas su šiuo vardu", Toast.LENGTH_SHORT).show();
                            set.start();
                            return;
                        }

                        // 2. Jei vardas laisvas – tikriname, ar el. paštas jau panaudotas
                        db.collection("users").whereEqualTo("email", Email).get().addOnCompleteListener(emailTask -> {
                            if (emailTask.isSuccessful()) {
                                if (!emailTask.getResult().isEmpty()) {
                                    register.setSoundEffectsEnabled(false);
                                    mp.start();
                                    Toast.makeText(getApplicationContext(), "Šis el. paštas jau panaudotas", Toast.LENGTH_SHORT).show();
                                    set.start();
                                    return;
                                }
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                auth.createUserWithEmailAndPassword(Email, Password)
                                        .addOnCompleteListener(taskk -> {
                                            if (taskk.isSuccessful()) {
                                                FirebaseUser currentUser = auth.getCurrentUser();
                                                String userId = currentUser.getUid();

                                                Map<String, Object> user = new HashMap<>();
                                                user.put("currency", "eur");
                                                user.put("email", Email);
                                                user.put("phoneNumber", Number);
                                                user.put("subscribed", false);
                                                user.put("username", Name);

                                                db.collection("users")
                                                        .document(userId)
                                                        .set(user)
                                                        .addOnSuccessListener(unused -> {
                                                            Intent intent = new Intent(getBaseContext(), Login.class);
                                                            startActivity(intent);
                                                        });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Registracijos klaida: " + taskk.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                            } else {
                                Toast.makeText(getApplicationContext(), "Klaida tikrinant el. paštą", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Klaida tikrinant vartotojo vardą", Toast.LENGTH_SHORT).show();
                    }
                });
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