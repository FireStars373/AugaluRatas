package com.example.augaluratas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class ShoppingCart extends AppCompatActivity {

    private ImageButton return_button = (ImageButton) findViewById(R.id.return_from_shopping_cart);
    private TextView seller = (TextView) findViewById(R.id.cart_seller);
    private TextView description = (TextView) findViewById(R.id.cart_description);
    private TextView price = (TextView) findViewById(R.id.cart_price);
    private ImageView photo = (ImageView) findViewById(R.id.cart_photo);
    private RadioGroup mail_options = (RadioGroup) findViewById(R.id.get_mail_options);
    private Button pay_by_card = (Button) findViewById(R.id.pay_by_card);
    private Button pay_by_apple_pay = (Button) findViewById(R.id.pay_by_apple_pay);
    private Button pay_by_google_pay = (Button) findViewById(R.id.pay_by_google_pay);

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
                    return;
                }
            }
        });
        pay_by_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
            }
        });
        pay_by_apple_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
            }
        });
        pay_by_google_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Take to pay
            }
        });
    }
}