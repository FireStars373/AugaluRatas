package com.example.augaluratas;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.widget.Toast;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Posts> postList;
    boolean belongs_to_user;

    public PostAdapter(List<Posts> postList) {
        this(postList, false); // Calls the other constructor with `false`
    }
    public PostAdapter(List<Posts> postList, boolean belongs_to_user) {
        this.postList = postList;
        this.belongs_to_user = belongs_to_user;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (viewType == 1) ? R.layout.post_item : R.layout.post_item_buyable;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Posts post = postList.get(position);
        holder.plantName.setText(post.getPlantName());
        holder.description.setText(post.getDescription());
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
        if (holder.addToCart != null){
            holder.addToCart.setOnClickListener(v -> {

                SharedPreferences prefs = v.getContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
                Set<String> current = prefs.getStringSet("items", new HashSet<>());
                if (current.contains(Long.toString(post.getId()))){
                    Toast.makeText(v.getContext(), "Šis augalas jau jūsų krepšelyje", Toast.LENGTH_SHORT).show();
                    return;
                }
                current.add(Long.toString(post.getId()));
                prefs.edit().putStringSet("items", current).apply();

                // Animate imageView to cart icon
                int[] originalPos = new int[2];
                holder.imageView.getLocationOnScreen(originalPos);

                // Clone image
                ImageView clone = new ImageView(v.getContext());
                clone.setImageDrawable(holder.imageView.getDrawable());

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        holder.imageView.getWidth(), holder.imageView.getHeight());
                params.leftMargin = originalPos[0];
                params.topMargin = originalPos[1];

                // Get activity context and root layout
                android.app.Activity activity = (android.app.Activity) v.getContext();
                FrameLayout rootLayout = (FrameLayout) activity.getWindow().getDecorView();
                rootLayout.addView(clone, params);

                // Find cart icon position
                ImageView cartIcon = activity.findViewById(R.id.shopping_cart); // make sure it exists
                if (cartIcon == null) {
                    Log.e("PostAdapter", "Cart icon not found!");
                    return;
                }

                int[] targetPos = new int[2];
                cartIcon.getLocationOnScreen(targetPos);
                targetPos[0] -= cartIcon.getWidth()/2;
                targetPos[1] -= cartIcon.getHeight()/2;

                float deltaX = targetPos[0] - originalPos[0];
                float deltaY = targetPos[1] - originalPos[1];

                clone.animate()
                        .translationX(deltaX)
                        .translationY(deltaY)
                        .scaleX(0.2f)
                        .scaleY(0.2f)
                        .alpha(0.0f)
                        .setDuration(1000)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withEndAction(() -> rootLayout.removeView(clone))
                        .start();
            });
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, description, price;
        ImageView imageView;
        ImageButton addToCart;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.text_plant_name);
            description = itemView.findViewById(R.id.text_description);
            price = itemView.findViewById(R.id.text_price);
            imageView = itemView.findViewById(R.id.image_plant);
            addToCart = itemView.findViewById(R.id.add_to_cart_btn);
        }
    }
    @Override
    public int getItemViewType(int position) {
        // Return different view types based on `belongs_to_user`
        return belongs_to_user ? 1 : 0;
    }
}

