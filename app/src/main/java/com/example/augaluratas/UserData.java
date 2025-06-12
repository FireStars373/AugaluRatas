package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserData extends BaseActivity {
    ImageView add_image;
    private Bitmap selectedImageBitmap = null;

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
        TextView password_rewrite = findViewById(R.id.password_repeat);
        TextView phone_number = findViewById(R.id.user_data_phone_number);
        Button change_data = findViewById(R.id.change_user_data);

        add_image = findViewById(R.id.user_change_photo);

        add_image.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pick.setType("image/*");
            startActivityForResult(pick, 1);
        });

        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        User_PostDatabase database = AppActivity.getUser_PostDatabase();
        Users user = database.usersDAO().getUserById(current_id);
        name.setText(user.getUsername());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        password_rewrite.setText(user.getPassword());
        phone_number.setText(user.getPhoneNumber());
        if(user.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            add_image.setImageBitmap(bitmap);
            add_image.setBackground(getDrawable(R.color.light_green));
        }

        Spinner currency_select = findViewById(R.id.change_currency);

        List<String> currencies = new ArrayList<>(Arrays.asList("EUR", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM",
                "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP",
                "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK",
                "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "FJD", "FKP", "FOK", "GBP", "GEL",
                "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF",
                "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES",
                "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD",
                "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR",
                "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB",
                "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR",
                "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN",
                "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS",
                "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XCG",
                "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"));
//        Set<Currency> currency_set = Currency.getAvailableCurrencies();
//        List<String> currencies = new ArrayList<>();
//
//        for (Currency currency : currency_set) {
//            currencies.add(currency.getCurrencyCode());
//        }
        currencies.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencies
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency_select.setAdapter(adapter);

        String currency = user.getCurrency();
        if(!currency.equals("NOTFOUND")){
            int pos = 0;
            for (String code : currencies){
                if(code.equals(currency)){
                    break;
                }
                pos++;
            }
            currency_select.setSelection(pos);
        }

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
                String Password_rewrite = password_rewrite.getText().toString().trim();
                String Number = phone_number.getText().toString().trim();
                if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || Password_rewrite.isEmpty() || Number.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Visi laukai turi būti išpildyti", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Password_rewrite.length() < 5 || !Password_rewrite.matches(".*\\d.*")){
                    Toast.makeText(getApplicationContext(), "Slaptažodį turi sudaryti bent 5 simboliai, su bent vienu skaičiu", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!Password_rewrite.equals(Password)){
                    Toast.makeText(getApplicationContext(), "Spaltažodžiai turi sutapti", Toast.LENGTH_LONG).show();
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


                String new_currency = currencies.get((int)currency_select.getSelectedItemId());

                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getBaseContext());
                CronetEngine cronetEngine = myBuilder.build();

                Executor executor = Executors.newSingleThreadExecutor();

                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "https://v6.exchangerate-api.com/v6/2d01d5f6b910d11e87a610cb/latest/EUR", new CurrencyConversionUrlRequestCallback(getBaseContext(), new_currency), executor);

                UrlRequest request = requestBuilder.build();
                request.start();

                Users user = database.usersDAO().getUserById(current_id);
                user.setUsername(Name);
                user.setEmail(Email);
                user.setPassword(Password_rewrite);
                user.setPhoneNumber(Number);
                user.setCurrency(new_currency);
                if(selectedImageBitmap != null){
                    byte[] bytes = ImageUtils.bitmapToByteArray(selectedImageBitmap);
                    user.setImage(bytes);
                }
                database.usersDAO().Update(user);
                Toast.makeText(getApplicationContext(), "Duomenys sėkmingai pakeisti!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 1 && res == RESULT_OK && data!=null && data.getData()!=null) {
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), data.getData()
                );
                selectedImageBitmap = scaleBitmap(selectedImageBitmap, 1024);
                add_image.setImageBitmap(selectedImageBitmap);
                add_image.setBackground(getDrawable(R.color.light_green));
            } catch (Exception e) {
                Toast.makeText(this, "Klaida įkeliant nuotrauką",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Bitmap scaleBitmap(Bitmap original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();
        float ratio = (float) width / (float) height;

        int newWidth, newHeight;
        if (ratio > 1) {
            newWidth = maxSize;
            newHeight = (int) (maxSize / ratio);
        } else {
            newHeight = maxSize;
            newWidth = (int) (maxSize * ratio);
        }

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }
}