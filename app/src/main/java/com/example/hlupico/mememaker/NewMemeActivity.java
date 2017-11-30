package com.example.hlupico.mememaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by melder on 11/17/17.
 */

public class NewMemeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 55;
    private static final int REQUEST_CODE_GALLERY = 66;
    private static final int REQUEST_CODE_TAKE_PHOTO = 77;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 88;
    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    private static final String[] GALLERY_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meme);
        setTitle("Make New Meme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.preview_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMemeActivity.this, PreviewMemeActivity.class);
                startActivity(intent);
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
        // TODO: Check for permission to use camera

        //TODO: when permission is granted, open camera
    }

    private void openGallery() {
        //TODO: Check for permission to view gallery

        //TODO: when permission is granted, open gallery
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO: take action if permissions are granted
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
