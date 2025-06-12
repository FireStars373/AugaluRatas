package com.example.augaluratas;

import android.os.Bundle;
import android.view.MotionEvent;
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
import androidx.core.widget.NestedScrollView;
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
    private RecyclerView recyclerView;
    private SearchView searchbar;
    private NestedScrollView scrollView;
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

        ImageButton return_button = findViewById(R.id.return_from_user_posts);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppActivity.getUser_PostDatabase();

        scrollView = findViewById(R.id.user_posts_scrollView);
        searchbar = findViewById(R.id.user_posts_search);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                add_posts();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    add_posts();
                }
                return false;
            }
        });
        searchbar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                add_posts();
                return false;
            }
        });
        //Remove focus from searchbar
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchbar.clearFocus();
                return false;
            }
        });

        add_posts();


    }

    public void add_posts(){
        executorService.execute(() -> {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
            Long current_id = sharedPref.getLong("current_user_id", 0);
            Log.d("DEBUG", "current_id: " + current_id);

            List<Posts> posts = db.postsDAO().getPostsByUser(current_id);
            posts.removeIf(post -> !post.getPlantName().toLowerCase().contains(searchbar.getQuery().toString().toLowerCase()));
            runOnUiThread(() -> {
                PostAdapter postAdapter = new PostAdapter(posts, true);
                recyclerView.setAdapter(postAdapter);
            });
        });
    }
}
