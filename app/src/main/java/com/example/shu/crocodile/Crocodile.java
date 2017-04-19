package com.example.shu.crocodile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
            result_id = getPlayerId.get();  //id игрока
            cookies = getPlayerId.cookie;   //сохранение куки
        }
        catch(InterruptedException e){
            result_id = "error_Interrupted_Croco";
        }
        catch(ExecutionException e){
            result_id = "error2_Execution_Croco";
        }

        //вывод (проверка)
        TextView text = (TextView)findViewById(R.id.textView5);
        text.setText(result_id);
    }

    public void onClickArtistTest(View view) {
        Intent intent = new Intent(Crocodile.this, ArtistActivity.class);
        startActivity(intent);
    }

    public void onClickUserTest(View view) {
        Intent intent = new Intent(Crocodile.this, UserActivity.class);
        startActivity(intent);
    }

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

            String result;
            try {
                result = getPlayerName.get();
            } catch (InterruptedException e) {
                result = "error_Interrupted_Croco";
            } catch (ExecutionException e) {
                result = "error2_Execution_Croco";
            }

            Connection_Post_Queue getQueuePlayer = new Connection_Post_Queue();
            getQueuePlayer.cookie = cookies;
            getQueuePlayer.execute();
            String resultq;
            try{
                getQueuePlayer.get();
                resultq = "queue";
            }catch (InterruptedException e) {
                resultq = "error_Interrupted_Croco";
            } catch (ExecutionException e) {
                resultq = "error2_Execution_Croco";
            }

            result = "0";

               while (Integer.parseInt(result) <2 ){
                    Connectional_Get_Role getPlayerRole = new Connectional_Get_Role();
                    getPlayerRole.cookie = cookies;
                    getPlayerRole.execute("http://croco.us-west-2.elasticbeanstalk.com/api/player/role.json");

                    try {
                        result = getPlayerRole.get();
                        result = result.substring(result.indexOf(":")+1,result.indexOf("}"));
                    } catch (InterruptedException e) {
                        result = "-1";
                    } catch (ExecutionException e) {
                        result = "-2";
                    }
               }
                switch(Integer.parseInt(result)){
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


            //вывод (проверка)
            TextView text = (TextView) findViewById(R.id.textView5);
            text.setText(result);
        }else{
            name.setHint("Некорректное имя");
        }
    }
}