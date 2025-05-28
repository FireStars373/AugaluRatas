package com.example.augaluratas;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class AddPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST       = 1;
    private static final int CAMERA_PERMISSION_REQ    = 100;

    private PreviewView    previewView;
    private ImageCapture   imageCapture;
    private Bitmap         selectedImageBitmap = null;

    private ImageView      selectPhotoView;
    private ImageButton    takePhotoBtn;
    private ImageButton    cameraCaptureBtn;
    private ImageButton    sidebarBtn;
    private Button         uploadBtn;
    private EditText       titleEt, descEt, priceEt;
    private User_PostDatabase database;
    private View[]         uiElements;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);

        // edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.contstraint_layout),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // find views
        selectPhotoView   = findViewById(R.id.button24);
        takePhotoBtn      = findViewById(R.id.take_photo_button);
        previewView       = findViewById(R.id.camera_preview);
        cameraCaptureBtn  = findViewById(R.id.camera_capture_btn);
        sidebarBtn        = findViewById(R.id.sidebar_from_add_post);
        uploadBtn         = findViewById(R.id.upload_post);
        titleEt           = findViewById(R.id.add_post_title);
        descEt            = findViewById(R.id.add_post_description);
        priceEt           = findViewById(R.id.add_post_price);
        title = findViewById(R.id.textView19);

        // group UI for hide/show
        uiElements = new View[]{
                selectPhotoView, takePhotoBtn, sidebarBtn,
                uploadBtn, titleEt, descEt, priceEt, title
        };

        // hide camera preview initially
        previewView.setVisibility(View.GONE);
        cameraCaptureBtn.setVisibility(View.GONE);

        MediaPlayer errSound = MediaPlayer.create(this, R.raw.bad_info);
        MediaPlayer okSound  = MediaPlayer.create(this, R.raw.succesful);

        // 1) gallery
        selectPhotoView.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pick.setType("image/*");
            startActivityForResult(pick, PICK_IMAGE_REQUEST);
        });

        // 2) take photo -> request permission then enter camera mode
        takePhotoBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.CAMERA },
                        CAMERA_PERMISSION_REQ
                );
            } else {
                enterCameraMode();
            }
        });

        // 3) capture button
        cameraCaptureBtn.setOnClickListener(v -> takePhoto());

        // 4) sidebar nav
        sidebarBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MeniuOverlay.class));
            overridePendingTransition(R.anim.slide_out_left, 0);
        });

        // 5) upload
        uploadBtn.setOnClickListener(v -> handleUpload(errSound, okSound));
    }

    private void enterCameraMode() {
        // hide all other UI
        for (View el : uiElements) el.setVisibility(View.GONE);
        previewView.setVisibility(View.VISIBLE);
        cameraCaptureBtn.setVisibility(View.VISIBLE);
        startCamera();
    }

    private void exitCameraMode() {
        // restore UI
        for (View el : uiElements) el.setVisibility(View.VISIBLE);
        previewView.setVisibility(View.GONE);
        cameraCaptureBtn.setVisibility(View.GONE);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider provider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();
                CameraSelector selector = CameraSelector.DEFAULT_BACK_CAMERA;

                provider.unbindAll();
                provider.bindToLifecycle(this, selector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Kamera nepasiekiama", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) return;
        imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy proxy) {
                        // Po to, kai nufotografavai ar pasirinkai iš galerijos:
                        Bitmap raw = previewView.getBitmap();
                        selectedImageBitmap = scaleBitmap(raw, 1024); // maksimalus ilgis 1024px
                        selectPhotoView.setImageBitmap(selectedImageBitmap);
                        /*Bitmap bmp = previewView.getBitmap();
                        selectedImageBitmap = bmp;
                        selectPhotoView.setImageBitmap(bmp);*/
                        proxy.close();
                        exitCameraMode();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        Toast.makeText(AddPost.this,
                                "Nepavyko nufotografuoti", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // handle gallery result
    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == PICK_IMAGE_REQUEST && res == RESULT_OK && data!=null && data.getData()!=null) {
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), data.getData()
                );
                selectedImageBitmap = scaleBitmap(selectedImageBitmap, 1024);
                selectPhotoView.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Klaida įkeliant nuotrauką",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // handle camera permission response
    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] perms, @NonNull int[] grants) {
        super.onRequestPermissionsResult(code, perms, grants);
        if (code == CAMERA_PERMISSION_REQ
                && grants.length>0
                && grants[0]==PackageManager.PERMISSION_GRANTED) {
            enterCameraMode();
        } else {
            Toast.makeText(this, "Kameros leidimas atmestas",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // upload logic
    private void handleUpload(MediaPlayer errSound, MediaPlayer okSound) {
        String title = titleEt.getText().toString().trim();
        String desc  = descEt .getText().toString().trim();
        String price= priceEt.getText().toString().trim();
        if (title.isEmpty()||desc.isEmpty()||price.isEmpty()) {
            shakeButton(uploadBtn);
            errSound.start();
            Toast.makeText(this, "Laukai negali būti tušti",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageBitmap==null) {
            shakeButton(uploadBtn);
            errSound.start();
            Toast.makeText(this, "Pasirinkite nuotrauką",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences prefs = getSharedPreferences(
                "augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE
        );
        long uid = prefs.getLong("current_user_id",0);
        byte[] imgBytes = ImageUtils.bitmapToByteArray(selectedImageBitmap);
        Posts post = new Posts(uid, title, desc, imgBytes,
                Double.parseDouble(price));
        database = AppActivity.getUser_PostDatabase();
        database.postsDAO().insert(post);
        okSound.start();
        startActivity(new Intent(this, UserPosts.class));
    }

    private void shakeButton(View btn) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(
                btn,"translationX",0f,25f,-25f,15f,-15f,5f,-5f,0f
        );
        shake.setDuration(600);
        AnimatorSet set = new AnimatorSet(); set.playSequentially(shake); set.start();
    }
    private Bitmap scaleBitmap(Bitmap original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();
        float ratio = (float) width / (float) height;

        int newWidth, newHeight;
        if (ratio > 1) {
            // pločio > aukščio
            newWidth = maxSize;
            newHeight = (int) (maxSize / ratio);
        } else {
            newHeight = maxSize;
            newWidth = (int) (maxSize * ratio);
        }

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }
}