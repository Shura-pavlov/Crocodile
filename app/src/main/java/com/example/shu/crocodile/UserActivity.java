package com.example.shu.crocodile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

//файл активити чата+список игроков+холст для отгадывающих
public class UserActivity extends AppCompatActivity {

    String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //взять куки с активити
        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

        //отрисовать начальную разметку карты
        Bitmap b = null;
        b = Graph.getNewUserBitmap(b);
        ImageView imageUser = (ImageView)findViewById(R.id.imageView2);
        imageUser.setImageBitmap(b);

        /*
        Была идея сделать фон опять через цикл, но это непрактично и глупо

        цикл завершается в тот момент, когда от какого либо пользователя получается роль 4 - победитель.

        Упорные предпринятые попытки не увенчались успехом
        крашится при загрузке layout. почему - неизвестно
        Однако функции отрисовки холста проверены, правильно должен отрисовывать холст
        Загрузку сообщения и списка игроков еще предстоит написать, но по сути механизм ничем не отличается от
        загрузки quads и далее.


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

                //загрузка quads
                JSONArray quads = json.getJSONArray("quads");

                //отрисовка холста для отгадывающих
                if (quadsRemoved) {
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
        }*/
    }


    //обработчик нажатия кнопку "назад"
    public void onBackPressed(){
        openQuitDialog();
    }

    //диалог выхода
    public void openQuitDialog(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Текущая сессия будет потеряна: вы уверены?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserActivity.this, Crocodile.class));
                finish();
            }
        });
        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        quitDialog.show();
    }
}
