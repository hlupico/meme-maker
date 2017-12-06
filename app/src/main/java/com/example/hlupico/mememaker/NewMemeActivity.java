package com.example.hlupico.mememaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
 *
 * TODO: Break code into branches that correspond to the Day x, Step x in the comments
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

        /**
         * Day 2, Step 1
         */
        // Get reference for cameraButton, create OnClickListener for
        // cameraButton and set OnclickListener for cameraButton
        Button cameraButton = (Button) findViewById(R.id.camera_button);
        View.OnClickListener cameraButtonOnClick = getCameraOnClickListener();
        cameraButton.setOnClickListener(cameraButtonOnClick);

        /**
         * Day 2, Step 2
         */
        // Get reference for galleryButton, create OnClickListener for
        // galleryButton and set OnclickListener for galleryButton
        Button galleryButton = (Button) findViewById(R.id.gallery_button);
        View.OnClickListener galleryButtonOnClick = getGalleryOnClickListener();
        galleryButton.setOnClickListener(galleryButtonOnClick);

        /**
         * Day 2, Step 3
         */
        // Get reference for saveButton, create OnClickListener for
        // saveButton and set OnclickListener for saveButton
        Button saveButton = (Button) findViewById(R.id.save_button);
        View.OnClickListener saveButtonOnClick = getSaveOnClickListener();
        saveButton.setOnClickListener(saveButtonOnClick);
    }

    /**
     * Day 2, Step 1
     * This method takes no method arguments and
     * will return the OnClickListener for the cameraButton
     */
    private View.OnClickListener getCameraOnClickListener() {
        View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
            // This `onClick` is to be called when the cameraButton,
            // the one with the 'Take Photo` text, is clicked by the user.
            @Override
            public void onClick(View view) {
                goToCamera();
            }
        };

        return cameraOnClickListener;
    }

    /**
     * Day 2, Step 1
     * The openCamera() method is responsible for requesting camera permissions if
     * the user has not already granted the app camera permissions and for opening the camera.
     *
     * TODO: Consider breaking this logic up further to be something like `openCamera` and `checkForPermissions`
     */
    private void goToCamera() {
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

        // TODO: Question for students
        // Day 2, Step 4
        // What will happen if the user DOES NOT have a camera on their device?
        // How can we improve the experience for these users?
    }

    /**
     * Day 2, Step 2
     * This method takes no method arguments and
     * will return the OnClickListener for the galleryButton
     */
    private View.OnClickListener getGalleryOnClickListener() {
        View.OnClickListener galleryOnClickListener = new View.OnClickListener() {
            // This `onClick` should be called when the galleryButton,
            // the one with the 'From Gallery` text, is clicked by the user.
            @Override
            public void onClick(View view) {
                goToGallery();
            }
        };

        return galleryOnClickListener;
    }

    private void goToGallery() {
        boolean storageReadPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (storageReadPermissionGranted == false) {
            ActivityCompat.requestPermissions(this, GALLERY_PERMISSION, REQUEST_CODE_GALLERY);
        } else {
            openGallery();
        }
    }

    /**
     * Day 2, Step 2
     */
    private void openGallery() {
        // TODO: Create an Intent called galleryIntent
        Intent galleryIntent = new Intent();
        // Show only images, no videos or anything else
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_CODE_CHOOSE_PHOTO);

    }

    /**
     * Day 2, Step 3
     */
    private View.OnClickListener getSaveOnClickListener() {
        View.OnClickListener saveOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeme();
            }
        };

        return saveOnClickListener;
    }

    /**
     * Day 2, Step 3
     */
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /**
         * Day 2, Step 1
         */
        if (requestCode == REQUEST_CODE_CAMERA) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Need permissions to take a photo", Toast.LENGTH_LONG).show();
            }
        }
        /**
         * Day 2, Step 2
         */
        else if (requestCode == REQUEST_CODE_GALLERY) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Need permissions to select a photo", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Day 2, Step 1
         */
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setThumbnail(imageBitmap);
        }
        /**
         * Day 2, Step 2
         */
        else if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
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
