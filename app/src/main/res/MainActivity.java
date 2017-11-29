package com.retailmenot.chicktechsandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        findViewById(R.id.new_meme_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.retailmenot.chicktechsandbox.MainActivity.this, com.retailmenot.chicktechsandbox.NewMemeActivity.class);
                startActivity(intent);
            }
        });
    }
}
