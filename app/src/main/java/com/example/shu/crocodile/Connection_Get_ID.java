package com.example.shu.crocodile;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//запрос id (необходим для инициализации, возможно пригодится дальше)
public class Connection_Get_ID extends AsyncTask <String, Void, String> {

    HttpURLConnection c;    //объект соединения
    String cookie;          //строка куки (определяется в классе)

    @Override
    protected String doInBackground(String... params) {

        StringBuilder result = new StringBuilder();     //вывод результата запроса
        URL url = null;                                 //url строка

        try {
            //взять параметры
            for (String param : params) url = new URL(param);

            //установить соединение, определить метод (GET/POST)
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            //если код == 200
            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(c.getInputStream());
                //работает.
                BufferedReader read = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = read.readLine()) != null) {
                    result.append(line);
                }
                read.close();

                //механизм взятия куки
                cookie = c.getHeaderField("Set-Cookie");
                cookie = cookie.substring(0,cookie.indexOf(';'));

                return result.toString();
            }

        } catch (Exception e) {
            return "error_conn";
        } finally {
            c.disconnect(); //разрыв соединения
        }
        return "error_no_conn";
    }
}

