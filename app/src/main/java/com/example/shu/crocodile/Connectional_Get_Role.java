package com.example.shu.crocodile;


import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connectional_Get_Role  extends AsyncTask<String, Void, String> {

    HttpURLConnection c;    //объект соединения
    String cookie;          //строка куки (определяется вне класса!!!)

    @Override
    protected String doInBackground(String... params) {

        StringBuilder result = new StringBuilder();     //вывод результата запроса
        URL url = null;                                 //url строка

        try {

            //взять параметры
            for (String param : params) url = new URL(param);

            //установить соединение, определить метод (GET/POST), задать куки
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Cookie",cookie);
            c.connect();

            //если код == 200
            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(c.getInputStream());

                BufferedReader read = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = read.readLine()) != null) {
                    result.append(line);
                }
                read.close();

                return result.toString();
            }

        } catch (Exception e) {
            return "error_conn";
        } finally {
            c.disconnect();
        }
        return "error_no_conn";
    }
}