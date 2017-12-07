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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The NewMemeActivity is responsible for showing the UI that appears on the 'Make New Meme' screen
 * The user will have the ability to:
 *      (1) Take a photo with the camera
 *      (2) Choose a photo from the gallery
 *      (3) Display selected photo on screen
 *      (4) Add top and bottom meme text to photo
 *      (5) See preview of meme
 *      (6) Save the meme :)
 */
public class NewMemeActivity extends AppCompatActivity {

    // TODO: Create an integer variable, REQUEST_CODE_CAMERA
    // TODO: Create an integer variable, REQUEST_CODE_TAKE_PHOTO
    // TODO: Create a variable that is an array of Strings, CAMERA_PERMISSION

    /**
     * onCreate() will be called it is launched from {@link MainActivity}
     * Once onCreate() is called the program will run each of the statements
     * contained in onCreate() in the order that they appear in onCreate().
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meme);
        setTitle("Make New Meme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: Get reference for cameraButton

        // TODO: Create OnClickListener for cameraButton

        // TODO: Set OnclickListener for cameraButton
    }

    /**
     * TODO: Create a method name getCameraOnClickListener
     * that takes no method arguments and will return the
     * OnClickListener for the cameraButton
     */



    /**
     * TODO: Create a method, `enterTakePictureFlow()`, that is responsible for managing
     * the sequence of events that need to occur before a user can navigate to the Camera.
     *
     * This method:
     * (1) Checks that the user has granted the app `CAMERA` permissions
     * (2) If app HASN'T been granted permission, request `CAMERA` permission
     * (3) If app HAS been granted permission, open camera : )
     */



    /**
     * TODO: Create a method, openCamera(), that will:
     * (1) Creates an Intent named `takePictureIntent` to access the camera
     * (2) Checks that the device has a camera
     * (3) Uses `takePictureIntent` to start the camera.
     */



    /**
     * ActivityCompat will call this method when it is done checking for permissions.
     * @param requestCode - is the request code used when ActivityCompat.requestPermissions() was called.
     * @param permissions - is the type of permission we requested
     * @param grantResults - can we used to determine if permissions were or were not granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // TODO: Add a check to see if the requestCode returned matches the REQUEST_CAMERA_CODE
        // If the codes match, open the camera. Else, let the user know that they need to enable permissions
    }

    /**
     * Called when an Activity you launched from this Activity exits, giving you:
     * (1) the requestCode you started it with,
     * (2) resultCode it returned,
     * (3) and any additional data from it.
     *
     * The resultCode will be RESULT_CANCELED if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: If the requestCode is equal to REQUEST_CODE_TAKE_PHOTO
        // AND the resultCode is RESULT_OK, then get the image from the data
        // Set the image to the thumbnail.
    }


    /**
     * A helper method which will display the [Bitmap image]
     * in the [ImageView thumbnail].
     */
    private void setThumbnail(Bitmap image) {
        ImageView thumbnail = (ImageView) findViewById(R.id.image_thumbnail);
        thumbnail.setImageBitmap(image);
    }


    /**
     * This method will cause you to return to the previous page
     * after clicking on the white arrow at the top of the screen
     */
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
