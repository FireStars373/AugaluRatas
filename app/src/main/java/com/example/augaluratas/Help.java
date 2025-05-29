package com.example.augaluratas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
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

    private String manufacturer_data;

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

        //CHANGE LATER FOR DEPLOY NOT GOOD THING TO USE HARDCODED DATA
        sendButton.setOnClickListener(v -> {
            String message = complaint.getText().toString().trim();
            if (message.isEmpty()){
                ObjectAnimator animator = ObjectAnimator.ofFloat(sendButton, "translationX",  0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f);
                animator.setDuration(600);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(animator);
                set.start();

                Toast.makeText(getBaseContext(), "Žinutė negali būti tuščia", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
            Long current_id = sharedPref.getLong("current_user_id", 0);
            User_PostDatabase database = AppActivity.getUser_PostDatabase();
            Users user = database.usersDAO().getUserById(current_id);

            StringBuilder builder = new StringBuilder();

            builder.append("MODEL: ").append(Build.MODEL).append("\n")
                    .append("MANUFACTURER: ").append(Build.MANUFACTURER).append("\n")
                    .append("BRAND: ").append(Build.BRAND).append("\n")
                    .append("DEVICE: ").append(Build.DEVICE).append("\n")
                    .append("PRODUCT: ").append(Build.PRODUCT).append("\n")
                    .append("HARDWARE: ").append(Build.HARDWARE).append("\n")
                    .append("HOST: ").append(Build.HOST).append("\n")
                    .append("TAGS: ").append(Build.TAGS).append("\n");

            String userEmail = "thepopteam10@gmail.com";
            String javaPassword = "yfvm ctvr hjxg vaoi";
            String subject = "Pranešimas iš programos";
            String devEmail = "thepopteam10@gmail.com";
            String fullMessage = "Nauja užklausa iš vartotojo (" + user.getEmail() + "):\n\n" + message + "\n\n" + "User's data" + "\n\n" + builder.toString();


            GmailSender.sendMail(userEmail, javaPassword, subject, fullMessage, devEmail);


            Toast.makeText(getBaseContext(), "Jūsų žinutė buvo išsiųsta!", Toast.LENGTH_SHORT).show();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
//                } else {
//                    NotificationHandler.sendNotification(Help.this, "app_channel_id", "Jūsų skelbimas buvo įsimintas!", Help.class, 1);
//                }
//            }
            //
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