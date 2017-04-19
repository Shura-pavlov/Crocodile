package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONArray;
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

        Bitmap b = null;
        b = Graph.getNewUserBitmap(b);
        ImageView imageUser = (ImageView)findViewById(R.id.imageView2);
        imageUser.setImageBitmap(b);

        String winnerUser=null;

        while (true) {

            Connection_Get_Game game = new Connection_Get_Game();
            game.cookie = cookies;
            game.execute("http://croco.us-west-2.elasticbeanstalk.com/api/lobby/game.json");
            String result = "0";
            try {
                Thread.sleep(1000);
                result = game.get();
            } catch (InterruptedException e) {
                result = "err";
            } catch (ExecutionException e) {
                result = "err";
            }

            try {
                JSONObject json = new JSONObject(result);
                Boolean quadsRemoved = json.getBoolean("quadsRemoved");
                JSONArray messages = json.getJSONArray("quadsRemoved");

                JSONArray quads = json.getJSONArray("quads");

                if (false) {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    for (int i = 0; i < 400; i++) {
                        b = Graph.getNewUserBitmap(b);
                    }
                }
                else {

                    for (int i = 0; i < quads.length(); i++) {

                        JSONObject quad = quads.getJSONObject(i);

                        int numberQuad = quad.getInt("number");
                        int colorQuad = quad.getInt("color");

                        b = Graph.getUserBitmap(b, numberQuad, colorQuad);
                    }
                    imageUser.setImageBitmap(b);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
