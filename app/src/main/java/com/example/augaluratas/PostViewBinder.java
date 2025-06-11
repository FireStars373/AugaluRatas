package com.example.augaluratas;// In a new file: PostViewBinder.java
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

public class PostViewBinder {

    public static final PostViewBinder INSTANCE = new PostViewBinder();
    private PostViewBinder() { }

    private PostAdapter.PostViewHolder getViewHolder(View view) {
        Object existingTag = view.getTag();
        if (existingTag instanceof PostAdapter.PostViewHolder) {
            return (PostAdapter.PostViewHolder) existingTag;
        }
        PostAdapter.PostViewHolder newViewHolder = new PostAdapter.PostViewHolder(view);
        view.setTag(newViewHolder);
        return newViewHolder;
    }

    public void bind(View view, Posts post, boolean belongsToUser, Context context) {
        PostAdapter.PostViewHolder holder = getViewHolder(view);

        holder.plantName.setText(post.getPlantName());
        holder.description.setText(post.getDescription());

        SharedPreferences sharedPref = context.getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        long currentId = sharedPref.getLong("current_user_id", 0);
        User_PostDatabase database = AppActivity.getUser_PostDatabase();
        Users user = database.usersDAO().getUserById(currentId);

        if (user != null) {
            String currency = user.getCurrency();
            SharedPreferences sharedPrefCur = context.getSharedPreferences("augalu_ratas.CURRENT_CURRENCY", Context.MODE_PRIVATE);
            float conversionRate = sharedPrefCur.getFloat("current_conversion_rate", 1.0f);

            Currency currencyInstance = Currency.getInstance(currency);
            String currencySymbol = currencyInstance.getSymbol();
            int decimalPoint = currencyInstance.getDefaultFractionDigits();
            double price = post.getPrice() * conversionRate;

            String formatPattern = "%." + decimalPoint + "f %s";
            String formattedPrice = String.format(formatPattern, price, currencySymbol);
            holder.price.setText(formattedPrice);
        } else {
            holder.price.setText(String.format("%.2f €", post.getPrice()));
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
        holder.imageView.setImageBitmap(bitmap);

        view.setOnClickListener(v -> {
            Intent intent;
            if (belongsToUser) {
                intent = new Intent(context, UserPostOverlay.class);
            } else {
                intent = new Intent(context, PostDescription.class);
            }
            intent.putExtra("POST_ID", post.getId());
            context.startActivity(intent);
        });

        if (holder.addToCart != null) {
            holder.addToCart.setOnClickListener(v -> {
                SharedPreferences prefs = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
                Set<String> current = prefs.getStringSet("items", new HashSet<>());

                if (current.contains(String.valueOf(post.getId()))) {
                    Toast.makeText(context, "Šis augalas jau jūsų krepšelyje", Toast.LENGTH_SHORT).show();
                    return;
                }

                Set<String> newSet = new HashSet<>(current);
                newSet.add(String.valueOf(post.getId()));
                prefs.edit().putStringSet("items", newSet).apply();

                // Animation logic (check if context is an Activity)
                if (context instanceof Activity) {
                    animateToCart((Activity) context, holder.imageView);
                }
            });
        }
    }

    private void animateToCart(Activity activity, ImageView imageView) {
        FrameLayout rootLayout = (FrameLayout) activity.getWindow().getDecorView();
        ImageView cartIcon = activity.findViewById(R.id.shopping_cart);
        if (cartIcon == null) {
            return; // Exit if cart icon isn't found
        }

        ImageView clone = new ImageView(activity);
        clone.setImageDrawable(imageView.getDrawable());

        int[] originalPos = new int[2];
        imageView.getLocationOnScreen(originalPos);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageView.getWidth(), imageView.getHeight());
        params.leftMargin = originalPos[0];
        params.topMargin = originalPos[1];
        rootLayout.addView(clone, params);

        int[] targetPos = new int[2];
        cartIcon.getLocationOnScreen(targetPos);
        targetPos[0] -= cartIcon.getWidth() / 2;
        targetPos[1] -= cartIcon.getHeight() / 2;

        float deltaX = (float) (targetPos[0] - originalPos[0]);
        float deltaY = (float) (targetPos[1] - originalPos[1]);

        clone.animate()
                .translationX(deltaX)
                .translationY(deltaY)
                .scaleX(0.2f)
                .scaleY(0.2f)
                .alpha(0.0f)
                .setDuration(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> rootLayout.removeView(clone)) // A lambda can be used here
                .start();
    }
}