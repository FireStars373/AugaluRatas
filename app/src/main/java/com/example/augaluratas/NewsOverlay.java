package com.example.augaluratas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class NewsOverlay extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_overlay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_news_overlay);
        TextView headline = (TextView) findViewById(R.id.news_headline);
        TextView main_text = (TextView) findViewById(R.id.news_main_text);
        TextView full_text = (TextView) findViewById(R.id.news_full_text);
        ImageView photo = (ImageView) findViewById(R.id.news_image);

        Long id = getIntent().getExtras().getLong("id");
        NewsDatabase newsDatabase = AppActivity.getNewsDatabase();
        News news = newsDatabase.newsDao().getNewsById(id);

        //Converting byte array to bitmap
        Bitmap bmp = BitmapFactory.decodeByteArray(news.getImage(), 0, news.getImage().length);

        headline.setText(news.getHeadline());
        main_text.setText(news.getMain_text());
        full_text.setText(news.getFull_text());

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photo.post(new Runnable() {
            @Override
            public void run() {
                photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, photo.getWidth(), photo.getHeight(), false));
            }
        });
    }
}