package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingCart extends BaseActivity {
    private User_PostDatabase db;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ConstraintLayout layout;
    private RadioGroup mail_options;

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
        layout = findViewById(R.id.shopping_cart_constraintLayout);
        mail_options = findViewById(R.id.get_mail_options);
        Button pay_by_card = findViewById(R.id.pay_by_card);
        Button pay_by_apple_pay = findViewById(R.id.pay_by_apple_pay);
        Button pay_by_google_pay = findViewById(R.id.pay_by_google_pay);

        db = AppActivity.getUser_PostDatabase();

        add_items();

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
        if (mail_options.getCheckedRadioButtonId() == -1){
            Toast.makeText(getBaseContext(), "Nepasirinktas pristatymo būdas", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences prefs = getBaseContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
        Set<String> all_in_cart = prefs.getStringSet("items", new HashSet<>());
        Set<String> buying = prefs.getStringSet("buying_items", new HashSet<>());
        if(buying.isEmpty()){
            Toast.makeText(getBaseContext(), "Nepasirinkti pirkiniai", Toast.LENGTH_SHORT).show();
            return;
        }
        for (String id : buying){
            if (db.postsDAO().getPostById(Long.parseLong(id)) != null){
                db.postsDAO().delete(db.postsDAO().getPostById(Long.parseLong(id)));
            }
        }
        all_in_cart.removeAll(buying);

        prefs.edit().putStringSet("items", all_in_cart).apply();
        Toast.makeText(getBaseContext(), "Pirkimas sėkmingai įvykdytas!", Toast.LENGTH_SHORT).show();

        finish();
    }
    public void add_items(){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
        Set<String> buying = prefs.getStringSet("buying_items", new HashSet<>());
        List<Posts> posts = new ArrayList<>();
        for (String item : buying){
            if (db.postsDAO().getPostById(Long.parseLong(item))!= null)
                posts.add(db.postsDAO().getPostById(Long.parseLong(item)));
        }

        int previousViewId = ConstraintSet.PARENT_ID;
        LayoutInflater inflater = getLayoutInflater();

        layout.setVisibility(View.GONE);

        for (Posts post : posts) {
            View itemView = inflater.inflate(R.layout.post_item, layout, false);
            itemView.setId(View.generateViewId());
            layout.addView(itemView);

            PostViewBinder.INSTANCE.bind(itemView, post, false, this);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);

            constraintSet.connect(itemView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(itemView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

            constraintSet.connect(
                    itemView.getId(),
                    ConstraintSet.TOP,
                    previousViewId,
                    (previousViewId == ConstraintSet.PARENT_ID) ? ConstraintSet.TOP : ConstraintSet.BOTTOM,

                    16
            );

            constraintSet.applyTo(layout);
            previousViewId = itemView.getId();
        }

        layout.setVisibility(View.VISIBLE);
    }

}