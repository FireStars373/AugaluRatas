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

    public CartAdapter(List<Posts> postList) {
        this.added_posts = postList;
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

        // Konvertuoja byte[] į Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
        holder.imageView.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(v -> {
            // Perduoti post ID į kitą aktyvumą
            String a = this.getClass().getSimpleName();
            if (belongs_to_user){
                Intent intent = new Intent(v.getContext(), UserPostOverlay.class);
                intent.putExtra("POST_ID", post.getId()); // Įrašome post ID į Intent
                v.getContext().startActivity(intent);
                return;
            }
            Intent intent = new Intent(v.getContext(), PostDescription.class);
            intent.putExtra("POST_ID", post.getId()); // Įrašome post ID į Intent
            v.getContext().startActivity(intent);
        });
        holder.addToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = buttonView.getContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
                Set<String> current = prefs.getStringSet("buying_items", new HashSet<>());
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
//        if (holder.addToCart != null){
//            holder.addToCart.setOnClickListener(v -> {
//
//                SharedPreferences prefs = v.getContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
//                Set<String> current = prefs.getStringSet("items", new HashSet<>());
//                if (current.contains(Long.toString(post.getId()))){
//                    Toast.makeText(v.getContext(), "Šis augalas jau jūsų krepšelyje", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                current.add(Long.toString(post.getId()));
//                prefs.edit().putStringSet("items", current).apply();
//
//                // Animate imageView to cart icon
//                int[] originalPos = new int[2];
//                holder.imageView.getLocationOnScreen(originalPos);
//
//                // Clone image
//                ImageView clone = new ImageView(v.getContext());
//                clone.setImageDrawable(holder.imageView.getDrawable());
//
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                        holder.imageView.getWidth(), holder.imageView.getHeight());
//                params.leftMargin = originalPos[0];
//                params.topMargin = originalPos[1];
//
//                // Get activity context and root layout
//                android.app.Activity activity = (android.app.Activity) v.getContext();
//                FrameLayout rootLayout = (FrameLayout) activity.getWindow().getDecorView();
//                rootLayout.addView(clone, params);
//
//                // Find cart icon position
//                ImageView cartIcon = activity.findViewById(R.id.shopping_cart); // make sure it exists
//                if (cartIcon == null) {
//                    Log.e("PostAdapter", "Cart icon not found!");
//                    return;
//                }
//
//                int[] targetPos = new int[2];
//                cartIcon.getLocationOnScreen(targetPos);
//                targetPos[0] -= cartIcon.getWidth()/2;
//                targetPos[1] -= cartIcon.getHeight()/2;
//
//                float deltaX = targetPos[0] - originalPos[0];
//                float deltaY = targetPos[1] - originalPos[1];
//
//                clone.animate()
//                        .translationX(deltaX)
//                        .translationY(deltaY)
//                        .scaleX(0.2f)
//                        .scaleY(0.2f)
//                        .alpha(0.0f)
//                        .setDuration(1000)
//                        .setInterpolator(new AccelerateDecelerateInterpolator())
//                        .withEndAction(() -> rootLayout.removeView(clone))
//                        .start();
//            });
//        }

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
