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

        // Get reference for cameraButton, create OnClickListener for
        // cameraButton and set OnclickListener for cameraButton
        Button cameraButton = (Button) findViewById(R.id.camera_button);

        // Get reference for galleryButton, create OnClickListener for
        // galleryButton and set OnclickListener for galleryButton
        Button galleryButton = (Button) findViewById(R.id.gallery_button);

        // Get reference for saveButton, create OnClickListener for
        // saveButton and set OnclickListener for saveButton
        Button saveButton = (Button) findViewById(R.id.save_button);
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
     * Step 3
     *
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
            Toast.makeText(this, "You have permission! Now what?", Toast.LENGTH_LONG).show();
            //TODO: open the camera
        }
    }

    /**
     *
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
     *
     * The getGalleryOnClickListener() will return the OnClickListener for the galleryButton
     */
    private View.OnClickListener getGalleryOnClickListener() {
        View.OnClickListener galleryOnClickListener = new View.OnClickListener() {
            // This `onClick` should be called when the galleryButton,
            // the one with the 'From Gallery` text, is clicked by the user.
            @Override
            public void onClick(View view) {
                enterGalleryFlow();
            }
        };

        return galleryOnClickListener;
    }

    /**
     *
     * The enterGalleryFlow method is responsible for managing the sequence of events
     * that need to occur before a user can navigate to the Galley. This method:
     *
     * (1) Checks that the user has granted the app `READ_EXTERNAL_STORAGE` permissions
     * (2) If app HASN'T been granted permission, request `READ_EXTERNAL_STORAGE` permission
     * (3) If app HAS been granted permission, open gallery : )
     */
    private void enterGalleryFlow() {
        boolean storageReadPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (storageReadPermissionGranted == false) {
            ActivityCompat.requestPermissions(this, GALLERY_PERMISSION, REQUEST_CODE_GALLERY);
        } else {
            Toast.makeText(this, "You have permission! Now what?", Toast.LENGTH_LONG).show();
            //todo: open the gallery
        }
    }

    /**
     * Step 4
     *
     * The openGallery() method will:
     * (1) Creates an Intent named `galleryIntent` to access the camera
     * (3) Uses `galleryIntent` to start the camera.
     */
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // Show only images, no videos or anything else
        galleryIntent.setType("image/*");

        // Always show the chooser (if there are multiple options available)
        galleryIntent = Intent.createChooser(galleryIntent, "Select Picture");

        // Start Activity
        startActivityForResult(galleryIntent, REQUEST_CODE_CHOOSE_PHOTO);
    }

    /**
     * Step 5
     *
     * This method will return the OnClickListener for the saveButton
     */
    private View.OnClickListener getSaveOnClickListener() {
        View.OnClickListener saveOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterSaveMemeFlow();
            }
        };

        return saveOnClickListener;
    }

    /**
     * Step 5
     *
     * The enterSaveMemeFlow method is responsible for managing the sequence of events
     * that need to occur before a user can save a photo. This method:
     *
     * (1) Checking that the user has granted the app `WRITE_EXTERNAL_STORAGE` permissions
     * (2) If app HASN'T been granted permission, request `WRITE_EXTERNAL_STORAGE` permission
     * (3) If app HAS been granted permission, save meme : )
     */
    private void enterSaveMemeFlow() {
        boolean storageWritePermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        if (!storageWritePermissionGranted) {
            ActivityCompat.requestPermissions(this, SAVE_PERMISSION, REQUEST_CODE_SAVE);
        } else {
            Toast.makeText(this, "You have permission! Now what?", Toast.LENGTH_LONG).show();
            //todo: save the meme
        }
    }

    /**
     * Step 5
     *
     * The saveMeme() will take the image we see on the thumbnail
     * in the form of a Bitmap (a type of file that stores an image)
     * and create a file for the image in the "memes" folder.
     */
    private void saveMeme() {
        View memeLayout = findViewById(R.id.meme);
        memeLayout.setDrawingCacheEnabled(true);
        memeLayout.buildDrawingCache();
        Bitmap full = memeLayout.getDrawingCache();
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
                MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, null);
            }
        }
        memeLayout.destroyDrawingCache();
        memeLayout.setDrawingCacheEnabled(false);
        Toast.makeText(this, "Saved to memes folder!", Toast.LENGTH_LONG).show();
    }

    /**
     * ActivityCompat will call this method when it is done checking for permissions.
     * @param requestCode - is the request code used when ActivityCompat.requestPermissions() was called.
     * @param permissions - is the type of permission we requested
     * @param grantResults - can we used to determine if permissions were or were not granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /**
         * Camera permissions
         */
        if (requestCode == REQUEST_CODE_CAMERA) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enterTakePictureFlow();
            } else {
                Toast.makeText(this, "Need permissions to take a photo", Toast.LENGTH_LONG).show();
            }
        }
        /**
         * Gallery Permissions
         */
        else if (requestCode == REQUEST_CODE_GALLERY) {
            // did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enterGalleryFlow();
            } else {
                Toast.makeText(this, "Need permissions to select a photo", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == REQUEST_CODE_SAVE) {
            //did the user give us permission?
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enterSaveMemeFlow();
            } else {
                Toast.makeText(this, "Need permissions to save", Toast.LENGTH_LONG).show();
            }
        }
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
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(this, "You have a photo from the camera! Now what?", Toast.LENGTH_LONG).show();
        }
        /**
         * Step 4
         */
        else if (requestCode == REQUEST_CODE_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Toast.makeText(this, "You have a photo from the gallery! Now what?", Toast.LENGTH_LONG).show();
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
