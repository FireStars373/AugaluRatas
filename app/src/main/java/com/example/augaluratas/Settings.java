package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_settings);
        Button user_data = findViewById(R.id.settings_user_data);
        Button notifications = findViewById(R.id.settings_notifications);
        Button help = findViewById(R.id.settings_help);
        Button about = findViewById(R.id.settings_about);
        Button privacy = findViewById(R.id.settings_privacy);
        Spinner country_select = findViewById(R.id.country_code_select);

        String[] countryCodes = Locale.getISOCountries();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                countryCodes
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_select.setAdapter(adapter);

        SharedPreferences sharedPrefCur = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_CURRENCY", Context.MODE_PRIVATE);
        String country = sharedPrefCur.getString("current_country_code", "NOTFOUND");
        if(!country.equals("NOTFOUND")){
            int pos = 0;
            for (String code : countryCodes){
                if(code.equals(country)){
                    break;
                }
                pos++;
            }
            country_select.setSelection(pos);
        }

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        user_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserData.class);
                startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Notifications.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Help.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), About.class);
                startActivity(intent);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Privacy.class);
                startActivity(intent);
            }
        });
        country_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPrefCur.edit();
                editor.putString("current_country_code", countryCodes[(int)id]);
                editor.apply();

                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getBaseContext());
                CronetEngine cronetEngine = myBuilder.build();

                Executor executor = Executors.newSingleThreadExecutor();

                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "https://v6.exchangerate-api.com/v6/2d01d5f6b910d11e87a610cb/latest/EUR", new CurrencyConversionUrlRequestCallback(getBaseContext(), countryCodes[(int)id]), executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}