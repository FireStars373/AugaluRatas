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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.util.Log;
import android.widget.Toast;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Posts> postList;
    private boolean belongs_to_user;

    public PostAdapter(List<Posts> postList) {
        this(postList, false); // Calls the other constructor with `false`
    }

    public PostAdapter(List<Posts> postList, boolean belongs_to_user) {
        this.postList = postList;
        this.belongs_to_user = belongs_to_user;
    }

    // onCreateViewHolder is still needed. It creates the view.
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (viewType == 1) ? R.layout.post_item : R.layout.post_item_buyable;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new PostViewHolder(view);
    }

    // onBindViewHolder is still needed. It binds the data by delegating.
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Posts post = postList.get(position);

        // This is the single line of code that does all the work now.
        // It calls the reusable binder we created.
        PostViewBinder.INSTANCE.bind(holder.itemView, post, belongs_to_user, holder.itemView.getContext());
    }

    // getItemCount is still needed.
    @Override
    public int getItemCount() {
        return postList.size();
    }

    // getItemViewType is still needed.
    @Override
    public int getItemViewType(int position) {
        return belongs_to_user ? 1 : 0;
    }

    // The ViewHolder inner class is still needed.
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
}


