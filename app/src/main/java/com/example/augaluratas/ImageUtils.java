package com.example.augaluratas;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
