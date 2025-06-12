package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;
import java.util.Set;

public class PostDescription extends BaseActivity {

    private User_PostDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_post_description);
        ImageView photo = findViewById(R.id.post_description_photo);
        TextView title = findViewById(R.id.post_description_title);
        TextView description = findViewById(R.id.post_description_description);
        TextView price = findViewById(R.id.post_description_price);

        Button add_to_cart = findViewById(R.id.add_to_cart_from_desc);
        CheckBox favorite = findViewById(R.id.favorite_toggle);

        db = AppActivity.getUser_PostDatabase();
        long id = getIntent().getLongExtra("POST_ID", -1);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                Long current_id = sharedPref.getLong("current_user_id", 0);
                if (db.userShoppingCartDAO().getSpecificItem(current_id, id)!=null){
                    Toast.makeText(v.getContext(), "Šis augalas jau jūsų krepšelyje", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(v.getContext(), "Augalas sėkmingai pridėtas!", Toast.LENGTH_SHORT).show();
                UserShoppingCart cart = new UserShoppingCart(current_id, id);
                db.userShoppingCartDAO().insert(cart);
            }
        });

        if (id != -1) {
            Posts post = db.postsDAO().getPostById(id);
            if (post != null) {

                byte[] imageBytes = db.postsDAO().getPostById(id).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                photo.setImageBitmap(bitmap);
                title.setText(post.getPlantName());
                description.setText(post.getDescription());
                price.setText(String.format("%.2f €", post.getPrice()));

                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
                Long current_id = sharedPref.getLong("current_user_id", 0);

                UserPostLikes like = db.userPostLikesDAO().getSpecificLike(current_id, id);
                if (like == null){
                    favorite.setChecked(false);
                }
                else{
                    favorite.setChecked(true);
                }
                favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        UserPostLikes like = db.userPostLikesDAO().getSpecificLike(current_id, id);
                        if (like == null && isChecked){
                            UserPostLikes new_like = new UserPostLikes(current_id, id);
                            db.userPostLikesDAO().insert(new_like);
                        } else if (like != null && !isChecked) {
                            db.userPostLikesDAO().delete(like);
                        }
                    }
                });
            }
            sidebar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }
}