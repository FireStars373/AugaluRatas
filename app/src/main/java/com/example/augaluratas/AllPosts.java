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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
public class AllPosts extends AppCompatActivity {
    private User_PostDatabase db;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        recyclerView = findViewById(R.id.recyclerView);

        db = AppActivity.getUser_PostDatabase();

        // Gauname visus įrašus foninėje gijoje

        add_posts();
        ImageButton sidebar = findViewById(R.id.sidebar_from_all_posts);
        ImageButton shopping_cart = findViewById(R.id.shopping_cart);


        sidebar.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_left, 0);
        });

        shopping_cart.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ShoppingCartList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.expand, 0);
        });
    }
    @Override
    public void onResume(){
        super.onResume();
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
                    Toast.makeText(AllPosts.this, "Klaida: vartotojas neegzistuoja!", Toast.LENGTH_LONG).show();
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
            List<Posts> posts = db.postsDAO().getPostsWithoutUser(current_id);
            runOnUiThread(() -> {
                PostAdapter postAdapter = new PostAdapter(posts);
                recyclerView.setAdapter(postAdapter);

                TextView plant_count = findViewById(R.id.plant_count);
                plant_count.setText("Augalų kiekis: " + posts.size());
            });
        });
    }
}
