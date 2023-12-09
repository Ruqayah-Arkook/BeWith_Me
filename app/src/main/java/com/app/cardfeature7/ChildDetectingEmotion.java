package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChildDetectingEmotion extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private FirebaseServices services;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_detecting_emotion);
        services=new FirebaseServices(ChildDetectingEmotion.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }

        findViewById(R.id.open_bottom_sheet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ChildDetectingEmotion.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(ChildDetectingEmotion.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChildDetectingEmotion.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                }else{
//
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST);
                }
            }
        });

//
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            File selectedImageFile = uriToFile(getApplicationContext(), selectedImageUri);

            // Retrieve the child's UID from your authentication system
            String childUID = getChildUID();
            services.uploadData("Emotion not found", childUID);
            // Pass the child's UID to the uploadData method
            ApiClient apiClient = new ApiClient("http://192.168.1.120:8000/recognize", this, selectedImageFile, childUID);
            apiClient.execute(selectedImageFile);

        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File photoFile = bitmapToFile(photo, "photo.jpg"); // You can provide a specific filename
            // Retrieve the child's UID from your authentication system
            String childUID = getChildUID();
//            services.uploadData("Emotion not found", childUID);
            // Pass the child's UID to the uploadData method
            System.out.println("Rhis iss sfv "+childUID);
            ApiClient apiClient = new ApiClient("http://192.168.1.120:8000/recognize", this, photoFile, childUID);
            apiClient.execute(photoFile);
        }
    }

    private String getChildUID() {
        // Retrieve the child's UID from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("ChildPreferences", MODE_PRIVATE);
        return preferences.getString("childUID", "");
    }

    private File bitmapToFile(Bitmap bitmap, String filename) {
        File file = new File(getFilesDir(), filename); // You can choose a different directory if needed
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static File uriToFile(Context context, Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            // For content URIs (e.g., from the Gallery or other apps)
            String[] projection = { android.provider.MediaStore.Images.ImageColumns.DATA };
            try {
                android.database.Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.ImageColumns.DATA);
                    cursor.moveToFirst();
                    filePath = cursor.getString(column_index);
                    cursor.close();
                }
            } catch (Exception e) {
                // Handle the exception as needed.
            }
        } else if ("file".equals(uri.getScheme())) {
            // For file URIs (e.g., from your own app's storage)
            filePath = uri.getPath();
        }

        if (filePath != null) {
            System.out.println(filePath);
            return new File(filePath);
        } else {
            // Handle the case where the file path is not found.
            return null;
        }
    }
}