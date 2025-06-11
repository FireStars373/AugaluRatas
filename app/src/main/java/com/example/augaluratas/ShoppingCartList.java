package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingCartList extends BaseActivity {

    private User_PostDatabase db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_shopping_cart_list);
        Button shopping_cart = findViewById(R.id.shopping_cart_list_shopping_cart);

        db = AppActivity.getUser_PostDatabase();

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setHasFixedSize(true);

        add_items();

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
        shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getBaseContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
                Set<String> buying = prefs.getStringSet("buying_items", new HashSet<>());
                if(buying.isEmpty()){
                    Toast.makeText(getBaseContext(), "Nepasirinktos prekes", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getBaseContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        add_items();
    }
    public void add_items(){
        executorService.execute(() -> {
            SharedPreferences prefs = getBaseContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
            Set<String> current = prefs.getStringSet("items", new HashSet<>());
            List<Posts> posts = new ArrayList<>();
            for (String item : current){
                if (db.postsDAO().getPostById(Long.parseLong(item))!= null)
                    posts.add(db.postsDAO().getPostById(Long.parseLong(item)));
            }
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
            Long current_id = sharedPref.getLong("current_user_id", 0);

            //List<Posts> posts = db.postsDAO().getPostsWithoutUser(current_id);
            runOnUiThread(() -> {
                CartAdapter cartAdapter = new CartAdapter(posts, getBaseContext());
                PostAdapter postAdapter = new PostAdapter(posts);
                recyclerView.setAdapter(cartAdapter);
            });
        });
    }
}