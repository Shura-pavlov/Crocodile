package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Crocodile extends AppCompatActivity {

    String cookies;     //строка куки

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //запрос на получение id и куки
        Connection_Get_ID getPlayerId = new Connection_Get_ID();
        getPlayerId.execute("http://croco.us-west-2.elasticbeanstalk.com/api/player/id.json");

        String result_id;
        try{
            result_id = getPlayerId.get();
            cookies = getPlayerId.cookie;   //сохранение куки
        }
        catch(InterruptedException e){
            result_id = "error_Interrupted_Croco";
        }
        catch(ExecutionException e){
            result_id = "error2_Execution_Croco";
        }

        //JSON вывод параметра в String
        try {
            //id игрока
            JSONObject json = new JSONObject(result_id);
            result_id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*//вывод (проверка)
        TextView text = (TextView)findViewById(R.id.textView5);
        text.setText(result_id);*/
    }
/*
тест активити
    public void onClickArtistTest(View view) {
        Intent intent = new Intent(Crocodile.this, ArtistActivity.class);
        startActivity(intent);
    }

    public void onClickUserTest(View view) {
        Intent intent = new Intent(Crocodile.this, UserActivity.class);
        startActivity(intent);
    }
*/
    public void onClick_start(View view) throws ExecutionException, InterruptedException {

        //проверка введенного имени
        EditText name = (EditText)findViewById(R.id.editText);
        if((name.getText().toString() != "") & (name.getText().length() >= 3)) {

            //запрос на отправку имени игрока
            Connection_Post_Name setPlayerName = new Connection_Post_Name();
            setPlayerName.cookie = cookies;     //определение куки
            setPlayerName.execute(name.getText().toString());

            //запрос на получении имени игрока (проверка)
            Connection_Get_Name getPlayerName = new Connection_Get_Name();
            getPlayerName.cookie = cookies;     //определение куки
            getPlayerName.execute("http://croco.us-west-2.elasticbeanstalk.com/api/player/name.json");

            String result_name;
            try {
                result_name = getPlayerName.get();
            } catch (InterruptedException e) {
                result_name = "error_Interrupted_Croco";
            } catch (ExecutionException e) {
                result_name = "error2_Execution_Croco";
            }

            //JSON вывод параметра в String
            try {
                //имя игрока
                JSONObject json = new JSONObject(result_name);
                result_name = json.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Connection_Post_Queue getQueuePlayer = new Connection_Post_Queue();
            getQueuePlayer.cookie = cookies;
            getQueuePlayer.execute();


            String result_queue = "0";
            while (Integer.parseInt(result_queue) < 2 ){
                Connectional_Get_Role getPlayerRole = new Connectional_Get_Role();
                getPlayerRole.cookie = cookies;
                getPlayerRole.execute("http://croco.us-west-2.elasticbeanstalk.com/api/player/role.json");
                try {
                    Thread.sleep(1000);
                    result_queue = getPlayerRole.get();
                } catch (InterruptedException e) {
                    result_queue = "-1";
                } catch (ExecutionException e) {
                    result_queue = "-2";
                }

                //JSON вывод параметра в String
                try {
                   //роль игрока
                   JSONObject json = new JSONObject(result_queue);
                    result_queue = json.getString("roleCode");
                } catch (JSONException e) {
                   e.printStackTrace();
                }
            }



                switch(Integer.parseInt(result_queue)){
                    case 2:{
                        Intent intent = new Intent(Crocodile.this, UserActivity.class);
                        intent.putExtra("cook",cookies);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Intent intent = new Intent(Crocodile.this, ArtistActivity.class);
                        intent.putExtra("cook",cookies);
                        startActivity(intent);
                        break;
                    }
                }


            /*//вывод (проверка)
            TextView text = (TextView) findViewById(R.id.textView5);
            text.setText(result_queue);*/
        }else{
            name.setHint("Некорректное имя");
        }
    }
}