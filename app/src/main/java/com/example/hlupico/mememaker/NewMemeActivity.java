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

    private static final int REQUEST_CODE_CAMERA = 55;
    // TODO: Create an integer variable, REQUEST_CODE_GALLERY

    private static final int REQUEST_CODE_TAKE_PHOTO = 77;
    // TODO: Create an integer variable, REQUEST_CODE_CHOOSE_PHOTO

    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    // TODO: Create a variable that is an array of Strings, GALLERY_PERMISSION

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

        // Get reference for cameraButton, create OnClickListener for
        // cameraButton and set OnclickListener for cameraButton
        Button cameraButton = (Button) findViewById(R.id.camera_button);
        View.OnClickListener cameraButtonOnClick = getCameraOnClickListener();
        cameraButton.setOnClickListener(cameraButtonOnClick);

        // TODO: Get reference for galleryButton

        // TODO: Create OnClickListener for galleryButton

        // TODO: Set OnclickListener for galleryButton
    }

    /**
     * This method takes no method arguments and
     * will return the OnClickListener for the cameraButton
     */
    private View.OnClickListener getCameraOnClickListener() {
        View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
            // This `onClick` is to be called when the cameraButton,
            // the one with the 'Take Photo` text, is clicked by the user.
            @Override
            public void onClick(View view) {
                enterTakePictureFlow();
            }
        };

        return cameraOnClickListener;
    }

    /**
     * The enterTakePictureFlow method is responsible for managing the sequence of events
     * that need to occur before a user can navigate to the Camera. This method:
     *
     * (1) Checks that the user has granted the app `CAMERA` permissions
     * (2) If app HASN'T been granted permission, request `CAMERA` permission
     * (3) If app HAS been granted permission, open camera : )
     */
    private void enterTakePictureFlow() {
        // Check to see if the user has given the app camera permissions.
        boolean cameraPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        // If the app DOES NOT have camera permissions, request camera permissions.
        if (cameraPermissionGranted == false) {
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSION, REQUEST_CODE_CAMERA);
        }
        //Else, the app DOES have camera permissions so open the camera!! :))
        else {
            openCamera();
        }
    }

    /**
     * The openCamera() method will:
     * (1) Creates an Intent named `takePictureIntent` to access the camera
     * (2) Checks that the device has a camera
     * (3) Uses `takePictureIntent` to start the camera.
     */
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // device has a camera app
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    /**
     * TODO: Create a method name getGalleryOnClickListener()
     * that takes no method arguments and will return the
     * OnClickListener for the galleryButton
     */


    /**
     * TODO: Create a method, `enterGalleryFlow()`, that is responsible for managing
     * the sequence of events that need to occur before a user can navigate to the Galler
     * This method:
     *
     * (1) Checks that the user has granted the app `READ_EXTERNAL_STORAGE` permissions
     * (2) If app HASN'T been granted permission, request `READ_EXTERNAL_STORAGE` permission
     * (3) If app HAS been granted permission, open gallery : )
     */


    /**
     * The openGallery() method will:
     * (1) Creates an Intent named `galleryIntent` to access the camera
     * (3) Uses `galleryIntent` to start the camera.
     */
    private void openGallery() {
        // TODO: Create Intent, galleryIntent, with an Action that
        // will open the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // TODO: Uncomment two code once galleryIntent is creatd.
//        galleryIntent.setType("image/*");
//        galleryIntent = Intent.createChooser(galleryIntent, "Select Picture");

        // TODO: Start Activity for result.
    }

    /**
     * ActivityCompat will call this method when it is done checking for permissions.
     * @param requestCode - is the request code used when ActivityCompat.requestPermissions() was called.
     * @param permissions - is the type of permission we requested
     * @param grantResults - can we used to determine if permissions were or were not granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Need permissions to take a photo", Toast.LENGTH_LONG).show();
            }
        }

        // TODO: Add a check to see if the requestCode returned matches REQUEST_CODE_GALLERY
        // If the permission was not granted let the user know that they need to enable permissions


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

        // TODO: Change the assignment of canSetThumbnail to represent the following condition:
        // (1) Evaluates to TRUE when requestCode is equal to REQUEST_CODE_CHOOSE_PHOTO
        // AND resultCode is RESULT_OK.
        //
        // (2) Evaluates to FALSE if EITHER the requestCode does not equal REQUEST_CODE_CHOOSE_PHOTO
        // OR resultCode is not RESULT_OK.
        boolean canSetThumbail = true;

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setThumbnail(imageBitmap);
        }
        else if (canSetThumbail) {
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                setThumbnail(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
