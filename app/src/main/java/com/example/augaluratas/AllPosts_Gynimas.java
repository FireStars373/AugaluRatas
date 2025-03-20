package com.example.augaluratas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;

public class AllPosts_Gynimas extends AppCompatActivity {
    private User_PostDatabase db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppActivity.getUser_PostDatabase();
        Spinner spinner = findViewById(R.id.gynimas_spinner);

        // Load user ID
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        currentUserId = sharedPref.getLong("current_user_id", 0);

        // Load all posts by default
        ChangePosts(false);

        // Setup spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_names,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Handle spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ChangePosts(false); // Show all posts
                } else {
                    ChangePosts(true);  // Show user posts
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Sidebar button
        ImageButton sidebar = findViewById(R.id.sidebar_from_all_posts);
        sidebar.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), MeniuOverlay.class)));

        // Shopping cart button
        ImageButton shopping_cart = findViewById(R.id.shopping_cart);
        shopping_cart.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), ShoppingCartList.class)));
    }

    public void ChangePosts(boolean showUserPosts) {
        executorService.execute(() -> {
            List<Posts> posts;
            if (showUserPosts) {
                posts = db.postsDAO().getPostsByUser(currentUserId);
            } else {
                posts = db.postsDAO().getAllPosts();
            }

            runOnUiThread(() -> {
                postAdapter = new PostAdapter(posts);
                recyclerView.setAdapter(postAdapter);
            });
        });
    }
}