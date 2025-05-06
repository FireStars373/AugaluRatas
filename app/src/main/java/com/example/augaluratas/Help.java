package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Help extends BaseActivity {

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);

        // Create notification channel FIRST
        createNotificationChannel();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton return_button = findViewById(R.id.return_from_help);
        return_button.setOnClickListener(v -> finish());

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});

        Button sendButton = findViewById(R.id.button45);
        EditText complaint = findViewById(R.id.editTextTextEmailAddress);
        sendButton.setOnClickListener(v -> {
            if (complaint.getText().toString().trim().isEmpty()){
                ObjectAnimator animator = ObjectAnimator.ofFloat(sendButton, "translationX",  0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
                animator.setDuration(600);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(animator);
                set.start();

                Toast.makeText(getBaseContext(), "Žinutė negali būti tuščia", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getBaseContext(), "Jūsų žinutė buvo išsiųsta!", Toast.LENGTH_SHORT).show();
            complaint.setText("");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
//                } else {
//                    NotificationHandler.sendNotification(Help.this, "app_channel_id", "Jūsų skelbimas buvo įsimintas!", Help.class, 1);
//                }
//            }
        });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "kanalo pavadinimas";
            String description = "kanalo aprasymas";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("app_channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}