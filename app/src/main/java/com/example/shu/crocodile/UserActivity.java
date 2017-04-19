package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserActivity extends AppCompatActivity {

    String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

        Connection_Get_Game game = new Connection_Get_Game();
        game.cookie = cookies;
        game.execute("http://croco.us-west-2.elasticbeanstalk.com/api/lobby/game");
        String result = "0";
        try{
            result = game.get();
        } catch (InterruptedException e) {
            result = "err";
        } catch (ExecutionException e) {
            result = "err";
        }
        String data="";

        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("quadsRemoved");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView text = (TextView)findViewById(R.id.textView3);
        text.setText(data);
    }
}
