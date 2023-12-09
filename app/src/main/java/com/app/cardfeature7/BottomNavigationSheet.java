package com.app.cardfeature7;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomNavigationSheet extends BottomSheetDialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_sheet, container, false);




        // Handle Camera option click
        view.findViewById(R.id.camera_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your camera option action here
            }
        });

        // Handle Icon option click
        view.findViewById(R.id.icon_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determine whether to pick from gallery or take a photo from the camera
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose an action");
                builder.setItems(new CharSequence[]{"Pick from Gallery", "Take a Photo"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Open the gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                                break;
                            case 1:
                                // Open the camera
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handling gallery image pick
            Uri selectedImageUri = data.getData();
            // Process the selected image from the gallery

        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handling the captured photo
            // The photo is available in the Intent's data field
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Process the captured image from the camera

        }
    }
}