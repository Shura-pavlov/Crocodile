package com.example.shu.crocodile;


import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//удалить квадрат
public class Connection_Delete_quad   extends AsyncTask<String, Void, Void> {

    HttpURLConnection c;        //объект соединения
    String cookie;              //строка куки (определяется вне класса!!!)
    String urll;

    @Override
    protected Void doInBackground(String... params) {
        byte[] data = null;
        InputStream is = null;
        URL url = null;
        try {
            //взять параметры
            for (String k : params) {

                //определить метод (GET/POST), задать куки, размер передаваемого запроса, свойства (?)
                url = new URL(urll);
                c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("DELETE");
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
