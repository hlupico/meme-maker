package com.example.hlupico.mememaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by melder on 11/17/17.
 */

public class NewMemeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE = 44;
    private static final int REQUEST_CODE_CAMERA = 55;
    private static final int REQUEST_CODE_GALLERY = 66;
    private static final int REQUEST_CODE_TAKE_PHOTO = 77;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 88;
    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    private static final String[] GALLERY_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] SAVE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meme);
        setTitle("Make New Meme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeme();
            }
        });

        findViewById(R.id.gallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        boolean cameraPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (cameraPermissionGranted == false) {
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
        if (storageReadPermissionGranted == false) {
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
        }
    }

    private void setThumbnail(Bitmap image) {
        ImageView thumbnail = (ImageView)findViewById(R.id.image_thumbnail);
        thumbnail.setImageBitmap(image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setThumbnail(imageBitmap);
        } else if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                setThumbnail(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMeme() {
        boolean storageWritePermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (!storageWritePermissionGranted) {
            ActivityCompat.requestPermissions(this, SAVE_PERMISSION, REQUEST_CODE_SAVE);
        } else {
            View memeLayout = findViewById(R.id.meme);
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
                    e.printStackTrace();
                } finally {
                    out = null;
                    MediaScannerConnection.scanFile(this, new String[] {imageFile.getAbsolutePath()}, null, null);
                }
            }
            memeLayout.destroyDrawingCache();
            memeLayout.setDrawingCacheEnabled(false);
            Toast.makeText(this, "Saved to memes folder!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
