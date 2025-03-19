package com.example.augaluratas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllPosts extends AppCompatActivity {
    private PostsDatabase db;
    private  UsersDatabase db2;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);

        //db = PostsDatabase.getDatabase(this);
        //db.postsDAO().insert(new Posts(1,"Bananmedis", "fainas medis", new byte[1], 2));
        // Gauname visus įrašus foninėje gijoje

        /*executorService.execute(() -> {
            db2 = UsersDatabase.getDatabase(this);
            Users user = db2.usersDAO().getUserByUsername("MAR");


            db = PostsDatabase.getDatabase(this);
            db.postsDAO().insert(new Posts(user.getId(),"Bananmedis", "fainas medis", new byte[1], 2));
            new GetPostsTask().execute();
        });*/
        ImageButton sidebar = findViewById(R.id.sidebar_from_all_posts);
        ImageButton shopping_cart = findViewById(R.id.shopping_cart);

        sidebar.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
            startActivity(intent);
        });

        shopping_cart.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ShoppingCartList.class);
            startActivity(intent);
        });
    }

    // AsyncTask klasė duomenų gavimui foninėje gijoje
    private class GetPostsTask extends AsyncTask<Void, Void, List<Posts>> {
        @Override
        protected List<Posts> doInBackground(Void... voids) {
            return db.postsDAO().getAllPosts();  // Kreipiamės į duombazę fone
        }

        @Override
        protected void onPostExecute(List<Posts> posts) {
            if (posts != null) {
                Toast.makeText(AllPosts.this, "Gauta įrašų: " + posts.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

