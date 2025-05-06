package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCart extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_shopping_cart);
        TextView seller = findViewById(R.id.cart_seller);
        TextView description = findViewById(R.id.cart_description);
        TextView price = findViewById(R.id.cart_price);
        ImageView photo = findViewById(R.id.cart_photo);
        RadioGroup mail_options = findViewById(R.id.get_mail_options);
        Button pay_by_card = findViewById(R.id.pay_by_card);
        Button pay_by_apple_pay = findViewById(R.id.pay_by_apple_pay);
        Button pay_by_google_pay = findViewById(R.id.pay_by_google_pay);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mail_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.get_mail_from_seller){
                    //(FUTURE) Change mailing option
                    return;
                }
                if (checkedId == R.id.get_mail_from_drop_box){
                    //(FUTURE) Change mailing option
                    return;
                }
                if (checkedId == R.id.get_mail_from_carrier){
                    //(FUTURE) Change mailing option
                }
            }
        });
        pay_by_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
                buy();

            }
        });
        pay_by_apple_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
                buy();

            }
        });
        pay_by_google_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
                buy();
            }
        });
    }
    public void buy(){
        User_PostDatabase database = AppActivity.getUser_PostDatabase();
        SharedPreferences prefs = getBaseContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
        Set<String> all_in_cart = prefs.getStringSet("items", new HashSet<>());
        Set<String> buying = prefs.getStringSet("buying_items", new HashSet<>());
        if(buying.isEmpty()){
            Toast.makeText(getBaseContext(), "Nepasirinkti pirkiniai", Toast.LENGTH_SHORT).show();
            return;
        }
        for (String id : buying){
            if (database.postsDAO().getPostById(Long.parseLong(id)) != null){
                database.postsDAO().delete(database.postsDAO().getPostById(Long.parseLong(id)));
            }
        }
        all_in_cart.removeAll(buying);

        prefs.edit().putStringSet("items", all_in_cart).apply();
        Toast.makeText(getBaseContext(), "Pirkimas sėkmingai įvykdytas!", Toast.LENGTH_SHORT).show();

        finish();
    }

}