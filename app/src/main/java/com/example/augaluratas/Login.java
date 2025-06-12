package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login extends BaseActivity {    @Override
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
    MediaPlayer mp = MediaPlayer.create(this, R.raw.bad_info);
    mp.setVolume(0.8f, 0.8f);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    login.setOnClickListener(v -> {
        String Name = name.getText().toString().trim();
        String Password = password.getText().toString().trim();

        ObjectAnimator animator = ObjectAnimator.ofFloat(login, "translationX", 0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
        animator.setDuration(600);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator);

        // Ieškom vartotojo pagal username, kad gautume jo email
        db.collection("users")
                .whereEqualTo("username", Name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Vartotojo vardas arba slaptažodis neteisingas", Toast.LENGTH_SHORT).show();
                        set.start();
                        mp.start();
                        return;
                    }

                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    String email = document.getString("email");

                    if (email == null || email.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nepavyko rasti el. pašto", Toast.LENGTH_SHORT).show();
                        set.start();
                        mp.start();
                        return;
                    }

                    // Prisijungiame per Firebase Authentication
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, Password)
                            .addOnSuccessListener(authResult -> {
                                String userId = document.getId();

                                // Gauti valiutą arba apskaičiuoti
                                String currency = document.getString("currency");
                                if (currency == null || currency.isEmpty()) {
                                    TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                                    String country = tm.getSimCountryIso().toUpperCase();
                                    if (country.isEmpty()) {
                                        currency = "USD";
                                    } else {
                                        currency = Currency.getInstance(new Locale("", country)).getCurrencyCode();
                                    }
                                    db.collection("users").document(userId).update("currency", currency);
                                }

                                // Paleidžiam valiutos keitimo užklausą
                                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getBaseContext());
                                CronetEngine cronetEngine = myBuilder.build();
                                Executor executor = Executors.newSingleThreadExecutor();
                                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                                        "https://v6.exchangerate-api.com/v6/2d01d5f6b910d11e87a610cb/latest/EUR",
                                        new CurrencyConversionUrlRequestCallback(getBaseContext(), currency), executor
                                );
                                UrlRequest request = requestBuilder.build();
                                request.start();

                                // Išsaugom user ID
                                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("current_user_id", userId);
                                editor.apply();

                                Intent intent = new Intent(getBaseContext(), MainPage.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Vartotojo vardas arba slaptažodis neteisingas", Toast.LENGTH_SHORT).show();
                                set.start();
                                mp.start();
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE", "Klaida: " + e.getMessage(), e);
                    Toast.makeText(getApplicationContext(), "Prisijungimo klaida: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    });

    return_to_first_screen.setOnClickListener(v -> finish());

    forgotten_password.setOnClickListener(v -> {
        Intent intent = new Intent(getBaseContext(), ForgottenPassword.class);
        startActivity(intent);
    });
}
}