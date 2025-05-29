package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;

public class Privacy extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_privacy);
        CheckBox agreement = findViewById(R.id.privacy_agreement);

        TextView about = findViewById(R.id.privacy_text);
        about.setText(loadTextFromAsset("privacy_policy.txt"));

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        agreement.setChecked(sharedPref.getBoolean("privacy", true));

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        agreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    Toast.makeText(getBaseContext(),"Naudojant programą, būtina priimti privatumo politiką", Toast.LENGTH_SHORT).show();
                    agreement.setChecked(true);
                }
                editor.putBoolean("privacy", isChecked);
                editor.apply();
            }
        });
    }
    public String loadTextFromAsset(String fileName) {
        String text = "";
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return text;
    }
}