package com.example.hlupico.mememaker;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by melder on 11/17/17.
 */

public class PreviewMemeActivity extends AppCompatActivity {
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
}
