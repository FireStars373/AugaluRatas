package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<Posts> added_posts;
    boolean belongs_to_user;
    Context context;

    public CartAdapter(List<Posts> postList, Context context)
    {
        this.added_posts = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = R.layout.cart_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Posts post = added_posts.get(position);
        holder.plantName.setText(post.getPlantName());
        holder.price.setText(String.format("%.2f €", post.getPrice()));

        SharedPreferences prefs = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        Set<String> current = prefs.getStringSet("buying_items", new HashSet<>());
        current.remove(Long.toString(post.getId()));
        prefs.edit().putStringSet("buying_items", current).apply();

        // Konvertuoja byte[] į Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
        holder.imageView.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(v -> {
            // Perduoti post ID į kitą aktyvumą
            String a = this.getClass().getSimpleName();
            if (belongs_to_user){
                Intent intent = new Intent(v.getContext(), UserPostOverlay.class);
                intent.putExtra("POST_ID", post.getId());
                v.getContext().startActivity(intent);
                return;
            }
            Intent intent = new Intent(v.getContext(), PostDescription.class);
            intent.putExtra("POST_ID", post.getId());
            v.getContext().startActivity(intent);
        });
        holder.addToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    current.add(Long.toString(post.getId()));
                    prefs.edit().putStringSet("buying_items", current).apply();
                    return;
                }
                if (current.contains(Long.toString(post.getId()))){
                    current.remove(Long.toString(post.getId()));
                    prefs.edit().putStringSet("buying_items", current).apply();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return added_posts.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, price;
        ImageView imageView;
        CheckBox addToCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            imageView = itemView.findViewById(R.id.cart_item_image);
            addToCart = itemView.findViewById(R.id.cart_item_checkbox);
        }
    }
}
