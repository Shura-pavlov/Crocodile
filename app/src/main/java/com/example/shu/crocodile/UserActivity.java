package com.example.shu.crocodile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

//файл активити чата+список игроков+холст для отгадывающих
public class UserActivity extends AppCompatActivity {

    String cookies;
    Bitmap b = null;
    int[] map_color = new int[399]; // запоминание цвета квадрата

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //взять куки с активити
        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

        //"обнуление" массива
        for(int i = 0; i<400; i++){
            map_color[i] = -1;
        }

        //отрисовать начальную разметку карты
        b = Graph.getNewUserBitmap(b);
        final ImageView imageUser = (ImageView)findViewById(R.id.imageView2);
        imageUser.setImageBitmap(b);

        //таймер задания проверки чата, отрисовки рисунка, (каждые 300 мс)
        final Handler uiHandler = new Handler();
        final Timer myTymer = new Timer();
        myTymer.schedule(new TimerTask() {
            @Override
            public void run() {
                Connection_Get_Game game = new Connection_Get_Game();
                game.cookie = cookies;
                game.execute("http://croco.us-west-2.elasticbeanstalk.com/api/lobby/game.json");
                String result = "0";
                try {
                    result = game.get();
                } catch (InterruptedException e) {
                    result = "err";
                } catch (ExecutionException e) {
                    result = "err";
                }

                try {
                    JSONObject json = new JSONObject(result);
                    //загрузка метки очищение холста
                    Boolean quadsRemoved = json.getBoolean("quadsRemoved");
                    //загрузка сообщений чата
                    JSONArray messages = json.getJSONArray("messages");
                    //загрузка quads
                    JSONArray quads = json.getJSONArray("quads");

                    //отрисовка холста для отгадывающих
                    if (quadsRemoved) {
                        b = Graph.getNewUserBitmap(b);
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageUser.setImageBitmap(b);
                            }
                        });
                    }
                    else {
                        b = Graph.getNewUserBitmap(b);
                        for (int i = 0; i < quads.length(); i++) {

                            JSONObject quad = quads.getJSONObject(i);

                            final int numberQuad = quad.getInt("number");
                            final int colorQuad = quad.getInt("color");

                            //TODO сделать нормальное обновление изображения
                            if (map_color[i] != colorQuad){
                                map_color[i] = colorQuad;
                                b = Graph.getUserBitmap(b, numberQuad, colorQuad);
                            }

                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageUser.setImageBitmap(b);
                                }
                            });
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if (Integer.parseInt(result_queue) > 1 ){
                    switch (Integer.parseInt(result_queue)){
                        case 2:{
                            Intent intent = new Intent(Crocodile.this, UserActivity.class);
                            intent.putExtra("cook",cookies);
                            startActivity(intent);
                            finish();
                            break;
                        }
                        case 3:{
                            Intent intent = new Intent(Crocodile.this, ArtistActivity.class);
                            intent.putExtra("cook",cookies);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                    myTymer.cancel();
                }*/
            }
        },0L,500L);


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
