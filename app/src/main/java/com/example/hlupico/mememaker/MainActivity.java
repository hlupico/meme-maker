package com.example.hlupico.mememaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SAVE = 44;
    private static final int REQUEST_CODE_CAMERA = 55;
    private static final int REQUEST_CODE_GALLERY = 66;
    private static final int REQUEST_CODE_TAKE_PHOTO = 77;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 88;
    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    private static final String[] GALLERY_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] SAVE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button cameraButton = findViewById(R.id.camera_button);
        Button galleryButton = findViewById(R.id.gallery_button);
        Button saveButton = findViewById(R.id.save_img_button);
        backgroundImage = findViewById(R.id.imageView);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_button:
                openCamera();
                break;
            case R.id.gallery_button:
                openGallery();
                break;
            case R.id.save_img_button:
                saveImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Need permissions to take a photo", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Need permissions to select a photo", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_SAVE) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "Need permissions to save your creation", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            backgroundImage.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                backgroundImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void openCamera() {
        boolean cameraPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (!cameraPermissionGranted) {
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSION, REQUEST_CODE_CAMERA);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // device has a camera app
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private void openGallery() {
        boolean storageReadPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (!storageReadPermissionGranted) {
            ActivityCompat.requestPermissions(this, GALLERY_PERMISSION, REQUEST_CODE_GALLERY);
        } else {
            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE_PHOTO);
        }
    }

    private void saveImage() {
        boolean storageWritePermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (!storageWritePermissionGranted) {
            ActivityCompat.requestPermissions(this, SAVE_PERMISSION, REQUEST_CODE_SAVE);
        } else {
            View memeLayout = findViewById(R.id.meme_layout);
            memeLayout.setDrawingCacheEnabled(true);
            memeLayout.buildDrawingCache();
            Bitmap full = memeLayout.getDrawingCache();
            //todo: what are the other cases/should we worry about them?
            if (Environment.getExternalStorageState().equalsIgnoreCase("mounted")) {
                File imageFolder = new File(Environment.getExternalStorageDirectory(), "memes");
                imageFolder.mkdirs();
                FileOutputStream out = null;
                File imageFile = new File(imageFolder, String.valueOf(System.currentTimeMillis()) + ".png");
                try {
                    out = new FileOutputStream(imageFile);
                    full.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error saving" + e.getLocalizedMessage());
                } finally {
                    out = null;
                    MediaScannerConnection.scanFile(this, new String[] {imageFile.getAbsolutePath()}, null, null);
                }
            }
            memeLayout.destroyDrawingCache();
            memeLayout.setDrawingCacheEnabled(false);
        }
    }
}
