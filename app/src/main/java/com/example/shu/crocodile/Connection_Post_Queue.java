package com.example.shu.crocodile;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection_Post_Queue extends AsyncTask<Void, Void, String> {

    HttpURLConnection c;        //объект соединения
    String cookie;              //строка куки (определяется вне класса!!!)

    @Override
    protected String doInBackground(Void... params) {
        byte[] data = null;
        InputStream is = null;
        URL url = null;
        try {
            //взять параметры
            String k = "";

            //определить метод (GET/POST), задать куки, размер передаваемого запроса, свойства (?)
            url = new URL("http://croco.us-west-2.elasticbeanstalk.com/api/player/queue.json");
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Length", "" + Integer.toString(k.getBytes().length));
            c.setRequestProperty("Cookie", cookie);
            c.setUseCaches(true);
            c.setDoInput(true);
            c.setDoOutput(true);

            //запись в поток вывода(?)
            data = k.getBytes("UTF-8");
            OutputStream os = c.getOutputStream();
            os.write(data);
            data = null;

            //установить соединение
            c.connect();

            //если код == 200
            if(c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                is = c.getInputStream();

                byte[] buffer = new byte[8192];
                int bytesRead;

                //передача запроса(?)
                while ((bytesRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            c.disconnect();
        }
        return null;
    }
}
