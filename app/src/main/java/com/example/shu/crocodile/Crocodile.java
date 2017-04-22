package com.example.shu.crocodile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

//активити начального окна
public class Crocodile extends AppCompatActivity {

    String cookies;     //строка куки
    String result_queue = "";

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

        /*//JSON вывод параметра в String
        try {
            //id игрока
            JSONObject json = new JSONObject(result_id);
            result_id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //вывод (проверка)
        TextView text = (TextView)findViewById(R.id.textView5);
        text.setText(result_id);*/
    }

    public void onClick_start(View view) throws ExecutionException, InterruptedException {

        //проверка введенного имени
        EditText name = (EditText)findViewById(R.id.editText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        if((name.getText().toString() != "") & (name.getText().length() >= 3)) {
            Button b = (Button)findViewById(R.id.button);
            b.setClickable(false);
            //запрос на отправку имени игрока
            Connection_Post_Name setPlayerName = new Connection_Post_Name();
            setPlayerName.cookie = cookies;     //определение куки
            setPlayerName.execute(name.getText().toString());

            /*//запрос на получении имени игрока (проверка)
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
            }*/

            //встать в очередь
            Connection_Post_Queue getQueuePlayer = new Connection_Post_Queue();
            getQueuePlayer.cookie = cookies;
            getQueuePlayer.execute();


            //туаймер задания проверки роли игрока (каждые 300 мс)
            final Timer myTymer = new Timer();
            myTymer.schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     Connection_Get_Role getPlayerRole = new Connection_Get_Role();
                                     getPlayerRole.cookie = cookies;
                                     getPlayerRole.execute("http://croco.us-west-2.elasticbeanstalk.com/api/player/role.json");
                                     try {
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

                                     if (Integer.parseInt(result_queue) > 1 ){
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
                                     }
                                 }
                             },0L,300L);

            TextView text = (TextView)findViewById(R.id.textView5);
            text.setText("Стоим в очереди. Ждём-с");
        }else{
            AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
            quitDialog.setTitle("Некорректное имя");
            quitDialog.setPositiveButton("Понял", null);
            name.setText("");
            quitDialog.show();
        }
    }
}