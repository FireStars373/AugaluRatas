package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login extends BaseActivity {


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
        MediaPlayer mp = MediaPlayer.create(this, R.raw.bad_info);
        mp.setVolume(0.8f,0.8f);
        User_PostDatabase usersDatabase = AppActivity.getUser_PostDatabase();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Password = password.getText().toString().trim();

                ObjectAnimator animator = ObjectAnimator.ofFloat(login, "translationX",  0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
                animator.setDuration(600);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(animator);

                if (Name.isEmpty()){
                    login.setSoundEffectsEnabled(false);
                    Toast.makeText(getApplicationContext(), "Neįrašytas vardas", Toast.LENGTH_SHORT).show();

                    set.start();

                    mp.start();

                    return;
                }
                if (Password.isEmpty()){
                    login.setSoundEffectsEnabled(false);
                    Toast.makeText(getApplicationContext(), "Neįrašytas slaptažodis", Toast.LENGTH_SHORT).show();

                    set.start();

                    mp.start();

                    return;
                }
                Users user = usersDatabase.usersDAO().getUserByUsername(Name);
                UserSettings settings = usersDatabase.userSettingsDAO().getByUserId(user.getId());
                if(settings == null)
                {
                    settings = new UserSettings(user.getId());
                    usersDatabase.userSettingsDAO().insert(settings);
                }
                if(user == null || !user.getPassword().equals(Password)){
                    login.setSoundEffectsEnabled(false);
                    Toast.makeText(getApplicationContext(), "Vartotojo vardas arba slaptažodis neteisingas", Toast.LENGTH_SHORT).show();

                    set.start();

                    mp.start();

                    return;
                }

                String currency = settings.getCurrency();

                //If currency code isn't saved, gets it from sim card location. USD by default
                if (currency == null){
                    TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    String country = tm.getSimCountryIso().toUpperCase();
                    if (country.isEmpty()){
                        currency = "USD";
                    }
                    else{
                        currency = Currency.getInstance(new Locale("", country)).getCurrencyCode();
                    }
                    settings.setCurrency(currency);
                    usersDatabase.userSettingsDAO().Update(settings);
                    AppActivity.getUser_PostDatabase().usersDAO().Update(user);
                }
                //If conversion rate isn't saved, calls API to find it. 1.0 by default
                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getBaseContext());
                CronetEngine cronetEngine = myBuilder.build();

                Executor executor = Executors.newSingleThreadExecutor();

                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "https://v6.exchangerate-api.com/v6/2d01d5f6b910d11e87a610cb/latest/EUR", new CurrencyConversionUrlRequestCallback(getBaseContext(), currency), executor);

                UrlRequest request = requestBuilder.build();
                request.start();

                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("current_user_id", user.getId());
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