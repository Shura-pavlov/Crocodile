package com.example.shu.crocodile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


//файл активити чата+список игроков+холст для отгадывающих
public class UserActivity extends AppCompatActivity {

    //список сообщений чата
    private List<Message> messagesArray = new ArrayList<>();

    //список имен игроков
    private List<String> namesArray = new ArrayList<>();

    //адаптер вывода сообщений чата
    RVAdapterUsers adapter;
    private RecyclerView rv;

    //адаптер вывода имен игроков
    RVAdapterPlayers adapterPlayers;
    private RecyclerView rv_players;

    //под куки
    String cookies;

    //отгадываемое изображение
    Bitmap b = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //область имен игроков
        rv_players = (RecyclerView)findViewById(R.id.rv_players);
        rv_players.setHasFixedSize(true);
        LinearLayoutManager llmp = new LinearLayoutManager(this);
        rv_players.setLayoutManager(llmp);

        //область сообщений чата
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //взять куки с активити
        Intent intent = getIntent();
        cookies = intent.getStringExtra("cook");

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

                //получить список игроков
                Connection_Get_Players playersList = new Connection_Get_Players();
                playersList.cookie = cookies;
                playersList.execute("http://croco.us-west-2.elasticbeanstalk.com/api/lobby/players.json");
                String result_playersList = "0";
                try {
                    result_playersList = playersList.get();
                } catch (InterruptedException e) {
                    result_playersList = "err";
                } catch (ExecutionException e) {
                    result_playersList = "err";
                }

                try{
                    //очистка списка игроков для обновления
                    namesArray = new ArrayList<>();

                    //загрузка JSON имен игроков
                    JSONObject json_players = new JSONObject(result_playersList);

                    //проверка художника
                    if (json_players.isNull("painter")){
                        myTymer.cancel();
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                openQuitDialogNoPainter();
                            }
                        });
                    }
                    else{
                        //добавление художника в общий список имен игроков

                        //загрузка информации по художнику
                        JSONObject painter = json_players.getJSONObject("painter");
                        final String id_painter = painter.getString("id");
                        final String name_painter = painter.getString("name");

                        namesArray.add(name_painter+"(художник)");
                    }

                    //загрузка имен отгадывающих
                    JSONArray guessers = json_players.getJSONArray("guessers");

                    for(int i =0; i<guessers.length(); i++){

                        JSONObject player = guessers.getJSONObject(i);
                        final String id_player = player.getString("id");
                        final String name_player = player.getString("name");

                        namesArray.add(name_player);
                    }

                    //вывод в область имен игроков списка имен игроков через адаптер вывода имен игроков
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapterPlayers = new RVAdapterPlayers(namesArray);
                            rv_players.setAdapter(adapterPlayers);
                        }
                    });

                    //проверка на победителя
                    if (!json_players.isNull("winner")){
                        JSONObject winner = json_players.getJSONObject("winner");
                        final String name_winner = winner.getString("name");

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


                        //слово художника
                        JSONObject json = new JSONObject(result_keyword);
                        final String keyword = json.getString("keyword");


                        myTymer.cancel();
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                openQuitDialogWinner(name_winner, keyword);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //получить данные обновления для отгадывающих
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

                    //загрузка JSON данных обновления отгадывающих
                    JSONObject json = new JSONObject(result);
                    //загрузка метки очищение холста
                    Boolean quadsRemoved = json.getBoolean("quadsRemoved");
                    //загрузка сообщений чата
                    JSONArray messages = json.getJSONArray("messages");

                    if((messages.length() != messagesArray.size())& (messages.length()!=0)) {

                        //обнуление списка сообщений для обновления
                        messagesArray = new ArrayList<>();

                        for (int i = 0; i < messages.length(); i++) {

                            JSONObject message = messages.getJSONObject(i);

                            final int number = message.getInt("number");
                            final String sender = message.getString("sender");
                            final String text = message.getString("text");
                            final Boolean mark;
                            if (message.isNull("marked")){
                                mark = null;
                            }
                            else{
                                mark = message.getBoolean("marked");
                            }

                            //добавление в список сообщений чата загруженное сообщение чата
                            // (в начало для корректного отображения)
                            messagesArray.add(0, new Message(number, sender, text, mark));
                        }

                        //вывод в область сообщений чата список сообщений чата через адаптер вывода сообщений чата
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new RVAdapterUsers(messagesArray, cookies);
                                rv.setAdapter(adapter);
                            }
                        });
                    }


                    //отрисовка холста для отгадывающих
                    //загрузка массива нарисованных квадратов
                    JSONArray quads = json.getJSONArray("quads");
                    //удаление какого-либо квадрата
                    if (quadsRemoved) {
                        b = Graph.getNewUserBitmap(b);
                    }
                        //стирание изображения для обновления рисунка
                        //b = Graph.getNewUserBitmap(b);
                    if (!json.isNull("quads")){
                        for (int i = 0; i < quads.length(); i++) {

                            JSONObject quad = quads.getJSONObject(i);

                            final int numberQuad = quad.getInt("number");
                            final int colorQuad = quad.getInt("color");

                            b = Graph.getUserBitmap(b, numberQuad, colorQuad);
                        }
                            //вывод изображения
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageUser.setImageBitmap(b);
                                }
                            });
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },0L,500L);

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

    //диалог выхода если нет художника
    public void openQuitDialogNoPainter(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Художник покинул комнату");
        quitDialog.setPositiveButton("Печалька", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserActivity.this, Crocodile.class));
                finish();
            }
        });

        quitDialog.show();
    }

    //нажатие на кнопку отправления сообщения в чат
    public void onClick_send(View view) {
        EditText text = (EditText)findViewById(R.id.editText2);

        //закрытие клавиатура по нажатию на кнопку
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        //отправка сообщения на сервер
        Connection_Post_Message sendMessage = new Connection_Post_Message();
        sendMessage.cookie = cookies;     //определение куки
        sendMessage.execute(text.getText().toString());
        text.setText("");
    }

    //диалог выхода если игра закончилась
    public void openQuitDialogWinner(String name, String keyword){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Конец игры");
        quitDialog.setMessage("Победитель: "+name+"\n\nСлово: "+ keyword);
        quitDialog.setPositiveButton("Начать новую игру", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserActivity.this, Crocodile.class));
                finish();
            }
        });

        quitDialog.show();
    }
}
