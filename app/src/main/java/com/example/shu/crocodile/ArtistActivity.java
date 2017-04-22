package com.example.shu.crocodile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class ArtistActivity extends AppCompatActivity implements View.OnTouchListener {

    String cookies;     //куки
    ImageView image;    //инициализация холста
    float mX, mY;       //координаты касания
    Bitmap bitmap = null;   //изображение
    int colour = 0;         //код цвета

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //взять куки из crocodile
        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

        //получить слово
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

        //вывод слова
        TextView text = (TextView)findViewById(R.id.textView2);
        text.setText(result_keyword);

        //начальная отрисовка сетки холста, его постановка на форму и листнер касания
        image = (ImageView)findViewById(R.id.imageView);
        bitmap = Graph.newBitmap(bitmap, cookies);
        image.setImageBitmap(bitmap);
        image.setOnTouchListener(this);

    }

    public boolean onTouch(View v, MotionEvent event) {
        //если касаться пальцем
        if(MotionEvent.ACTION_DOWN == event.getAction()) {
            mX = event.getX();
            mY = event.getY();

            //функция отрисовки квадрата
            bitmap = Graph.getBitmap(mX, mY, bitmap, colour, cookies);
            image.setImageBitmap(bitmap);
        }
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
        //удаление всего холста
        bitmap = Graph.newBitmap(bitmap, cookies);
        image.setImageBitmap(bitmap);

    }

    //обработчик нажатия кнопку "назад"
    public void onBackPressed(){
        openQuitDialog();
    }

    //диалог выхода
    public void openQuitDialog(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Текущая сессия будет потряна: вы уверены?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ArtistActivity.this, Crocodile.class));
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
