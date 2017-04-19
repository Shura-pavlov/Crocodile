package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;


public class ArtistActivity extends AppCompatActivity implements View.OnTouchListener {

    String cookies;
    ImageView image;
    float mX, mY;
    Bitmap bitmap = null;
    int colour = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

        Connection_Get_keyword getKeyword = new Connection_Get_keyword();
        getKeyword.cookie = cookies;
        getKeyword.execute("http://croco.us-west-2.elasticbeanstalk.com/api/lobby/keyword.json");

        String result_keyword;
        try{
            result_keyword = getKeyword.get();
        } catch (InterruptedException e) {
            result_keyword = "err";
        } catch (ExecutionException e) {
            result_keyword = "err";
        }

        //JSON вывод параметра в String
        try {
            //слово художника
            JSONObject json = new JSONObject(result_keyword);
            result_keyword = json.getString("keyword");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView text = (TextView)findViewById(R.id.textView2);
        text.setText(result_keyword);

        image = (ImageView)findViewById(R.id.imageView);
        bitmap = Graph.newBitmap(bitmap, cookies);
        image.setImageBitmap(bitmap);
        image.setOnTouchListener(this);

    }

    public boolean onTouch(View v, MotionEvent event) {

        mX = event.getX();
        mY = event.getY();

        bitmap = Graph.getBitmap(mX, mY, bitmap, colour,cookies);
        image.setImageBitmap(bitmap);

        return true;
    }

    public void onClick_purple(View view) {
        colour = 12;
    }

    public void onClick_white(View view) {
        colour = 13;
    }

    public void onClick_black(View view) {
        colour = 0;
    }

    public void onClick_brown(View view) {
        colour = 1;
    }

    public void onClick_redDark(View view) {
        colour = 2;
    }

    public void onClick_orange(View view) {
        colour = 5;
    }

    public void onClick_pink(View view) {
        colour = 4;
    }

    public void onClick_blueLigth(View view) {
        colour = 9;
    }

    public void onClick_greenLigth(View view) {
        colour = 7;
    }

    public void onClick_greenDark(View view) {
        colour = 8;
    }

    public void onClick_red(View view) {
        colour = 3;
    }

    public void onClick_blue(View view) {
        colour = 10;
    }

    public void onClick_blueBlue(View view) {
        colour = 11;
    }

    public void onClick_yellow(View view) {
        colour = 6;
    }

    public void onClick_clear(View view) {

        bitmap = Graph.newBitmap(bitmap, cookies);
        image.setImageBitmap(bitmap);

    }
}
