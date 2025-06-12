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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritePosts extends BaseActivity {
    private User_PostDatabase db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_posts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton back = findViewById(R.id.arrow_back);
        recyclerView = findViewById(R.id.favorite_posts_recyclerView);
        db = AppActivity.getUser_PostDatabase();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserProfile.class);
                startActivity(intent);
            }
        });
        add_posts();
    }
    public void add_posts(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        executorService.execute(() -> {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
            Long current_id = sharedPref.getLong("current_user_id", 0);
            Log.d("DEBUG", "current_id: " + current_id);

            List<Users> visiVartotojai = db.usersDAO().getAllUsers();
            Log.d("DEBUG", "DB vartotojai: " + visiVartotojai.size());
            for (Users u : visiVartotojai) {
                Log.d("DEBUG", "Vartotojas ID: " + u.getId());
            }

            // Tikriname, ar vartotojas egzistuoja
            Users user = db.usersDAO().getUserById(current_id);
            if (user == null) {
                runOnUiThread(() -> {
                    Toast.makeText(getBaseContext(), "Klaida: vartotojas neegzistuoja!", Toast.LENGTH_LONG).show();
                });
                Log.e("DATABASE_ERROR", "Foreign Key Constraint: Vartotojas su ID " + current_id + " neegzistuoja!");
                return; // Nutraukiame vykdymą, kad nebandytume įterpti įrašų su neegzistuojančiu FK
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bananmedis);
            if (bitmap == null) {
                Log.e("BitmapError", "Nepavyko užkrauti lotus_icon");
                return;
            }

            byte[] imageBytes = ImageUtils.bitmapToByteArray(bitmap);

            // Jei vartotojas egzistuoja, tęsiame įrašymą
            //db.postsDAO().insert(new Posts(current_id,"Bananmedis", "fainas medis", imageBytes, 2));

            // Gauti įrašus ir nustatyti adapterį
            List<Posts> posts = db.userPostLikesDAO().getAllUserLikes(current_id);
            //Applying search filter
            runOnUiThread(() -> {
                PostAdapter postAdapter = new PostAdapter(posts, false);
                recyclerView.setAdapter(postAdapter);
            });
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        add_posts();
    }
}