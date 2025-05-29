package com.example.augaluratas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPosts extends BaseActivity {
    private User_PostDatabase db;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_posts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        SearchView search = findViewById(R.id.user_posts_search);
        ImageButton return_button = findViewById(R.id.return_from_user_posts);

        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(FUTURE) Search database
            }
        });
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppActivity.getUser_PostDatabase();

        // Gauname visus įrašus foninėje gijoje
        executorService.execute(() -> {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
            Long current_id = sharedPref.getLong("current_user_id", 0);
            Log.d("DEBUG", "current_id: " + current_id);


            // Gauti įrašus ir nustatyti adapterį
            List<Posts> posts = db.postsDAO().getPostsByUser(current_id);
            runOnUiThread(() -> {
                PostAdapter postAdapter = new PostAdapter(posts, true);
                recyclerView.setAdapter(postAdapter);
            });
        });

    }
    }
