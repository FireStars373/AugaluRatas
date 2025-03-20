package com.example.augaluratas;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, description, price;
        ImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.text_plant_name);
            description = itemView.findViewById(R.id.text_description);
            price = itemView.findViewById(R.id.text_price);
            imageView = itemView.findViewById(R.id.image_plant);
        }
    }
}

