package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Crocodile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //test git
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void onClickArtistTest(View view) {
        Intent intent = new Intent(Crocodile.this, ArtistActivity.class);
        startActivity(intent);
    }

    public void onClickUserTest(View view) {
        Intent intent = new Intent(Crocodile.this, UserActivity.class);
        startActivity(intent);
    }
}
