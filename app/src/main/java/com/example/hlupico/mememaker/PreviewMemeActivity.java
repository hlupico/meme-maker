package com.example.hlupico.mememaker;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by melder on 11/17/17.
 */

public class PreviewMemeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE = 44;
    private static final String[] SAVE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_meme);
        setTitle("Preview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        String topText = extras.getString("topText");
        String bottomText = extras.getString("bottomText");
        Uri imageUri = Uri.parse(extras.getString("image"));

        ImageView background = (ImageView)findViewById(R.id.background_image);
        try {
            Bitmap backgroundImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            background.setImageBitmap(backgroundImage);
        } catch (IOException e) {}

        TextView topTextView = (TextView)findViewById(R.id.top_text);
        topTextView.setText(topText);
        TextView bottomTextView = (TextView)findViewById(R.id.bottom_text);
        bottomTextView.setText(bottomText);

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewMemeActivity.this.finish();
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_SAVE) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "Need permissions to save your creation", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveImage() {
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
}
