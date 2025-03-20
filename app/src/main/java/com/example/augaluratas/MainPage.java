package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_main_page);
        TextView username = findViewById(R.id.main_page_username);


        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        Users user = AppActivity.getUser_PostDatabase().usersDAO().getUserById(current_id);
        username.setText(user.getUsername());
        NewsDao newsDao = AppActivity.getNewsDatabase().newsDao();
        //Deleting example data
        //newsDao.deleteAll();
        if (newsDao.getAllNews().isEmpty()){
            //Inserting example data if empty
            newsDao.insert(new News("Naujienos 1!!!!", "Čia yra naujienos.", "Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. ", convertDrawableToByteArray(R.drawable.baseline_filter_24)));
            newsDao.insert(new News("Naujienos 2.", "Čia rimtos naujienos...", "Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. ", convertDrawableToByteArray(R.drawable.baseline_filter_24)));
            newsDao.insert(new News("Naujienos 3!", "Čia smagios naujienos!", "Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. Naujienos tekstas. ", convertDrawableToByteArray(R.drawable.baseline_filter_24)));
        }




        loadNews();

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
            }
        });
    }
    public void loadNews(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.main_page_layout);
        NewsDatabase newsDatabase = AppActivity.getNewsDatabase();
        List<News> newsList = newsDatabase.newsDao().getAllNews();

        if (newsList == null || newsList.isEmpty()){return;}

        for (News news : newsList){
            TextView newTextView = new TextView(this);
            newTextView.setText(news.getHeadline());
            newTextView.setTextColor(getResources().getColor(R. color. text_brown));
            newTextView.setTextSize(24);
            newTextView.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc));



            newTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getBaseContext(), NewsOverlay.class);
                    intent.putExtra("id", news.getId());
                    startActivity(intent);
                }
            });
            layout.addView(newTextView);

            //Divider
            if (news != newsList.get(newsList.size()-1)){
                ImageButton newImageButton = new ImageButton(this);
                //newImageButton.setPadding(110, 8, 0, 8);
                newImageButton.setImageResource(R.drawable.square_background_1);
                //newImageButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(300, 8, 300, 8);
                newImageButton.setLayoutParams(params);
                layout.addView(newImageButton);
            }


        }
    }

    private byte[] convertDrawableToByteArray(int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId, null);

        if (drawable == null) {
            return null;
        }

        // Convert Drawable to Bitmap
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        // Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}