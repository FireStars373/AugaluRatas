package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Notifications extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox viewed = findViewById(R.id.notification_post_viewed);
        CheckBox shared = findViewById(R.id.notification_post_shared);
        CheckBox liked = findViewById(R.id.notification_post_liked);
        CheckBox purchased = findViewById(R.id.notification_plant_purchased);
        ImageButton return_button = findViewById(R.id.return_from_notifications);

        User_PostDatabase db = AppActivity.getUser_PostDatabase();
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        UserSettings settings = db.userSettingsDAO().getByUserId(current_id);
        FirebaseFirestore dbb = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbb.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {

            if (settings.getPostViewed()) {
                viewed.setChecked(sharedPref.getBoolean("viewed", true));

            }
            if (settings.getPostShared()) {
                shared.setChecked(sharedPref.getBoolean("shared", true));
            }
            if (settings.getPostLiked()) {
                liked.setChecked(sharedPref.getBoolean("liked", true));
            }
            if (settings.getPostBought()) {
                purchased.setChecked(sharedPref.getBoolean("purchased", true));
            }

            return_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            //Setting new settings
            viewed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    settings.setPostViewed(isChecked);
                    db.userSettingsDAO().Update(settings);
                    updateNotificationInFirestore(currentUser.getUid(), "post_viewed", isChecked);
                }
            });
            shared.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    settings.setPostShared(isChecked);
                    db.userSettingsDAO().Update(settings);
                    updateNotificationInFirestore(currentUser.getUid(), "post_shared", isChecked);
                }
            });
            liked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    settings.setPostLiked(isChecked);
                    db.userSettingsDAO().Update(settings);
                    updateNotificationInFirestore(currentUser.getUid(), "post_liked", isChecked);
                }
            });
            purchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    settings.setPostBought(isChecked);
                    db.userSettingsDAO().Update(settings);
                    updateNotificationInFirestore(currentUser.getUid(), "post_purchased", isChecked);
                }
            });
        });
    }
    private void updateNotificationInFirestore(String userId, String type, boolean isActive) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notif = new HashMap<>();
        notif.put("type", type);
        notif.put("active", isActive);

        db.collection("users")
                .document(userId)
                .collection("notifications")
                .document(type) // naudojame type kaip dokumento ID
                .set(notif)
                .addOnSuccessListener(unused -> Log.d("Firestore", type + " updated to " + isActive))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to update " + type, e));
    }
}