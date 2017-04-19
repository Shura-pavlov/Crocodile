package com.example.shu.crocodile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.concurrent.ExecutionException;


public class Graph {

    public static int count_pix = 20;
    public static int dp_can = 340;
    public static int size_pix = dp_can/count_pix;

    public static int dpToPx(int dp){
        return(int)(dp* Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(float px){
        return(int)(px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap newBitmap (Bitmap b, String cook){

        b = Bitmap.createBitmap(dpToPx(dp_can),dpToPx(dp_can), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(Color.DKGRAY);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        int x = 0;
        for (int i = 0; i<count_pix; i++){
            int y = 0;
            for (int j = 0; j<count_pix; j++) {
                canvas.drawRect(x+1, y+1, x + dpToPx(size_pix) - 1, y + dpToPx(size_pix) - 1, paint);
                y += dpToPx(size_pix);
            }
            x += dpToPx(size_pix);
        }

        Connection_Delete_quads quads = new Connection_Delete_quads();
        quads.cookie = cook;
        quads.execute();

        return b;
    }

    public static String getCol (Integer c){

        String col= "";

        switch (c){
            case 0: col="ff000000";
                break;
            case 1: col="ffb97a57";
                break;
            case 2: col="ff880016";
                break;
            case 3: col="ffed1b24";
                break;
            case 4: col="fffeaec9";
                break;
            case 5: col="ffff7f26";
                break;
            case 6: col="fffef200";
                break;
            case 7: col="ffb5e51d";
                break;
            case 8: col="ff23b14d";
                break;
            case 9: col="ff00a3e8";
                break;
            case 10: col="ff3f47cc";
                break;
            case 11: col="ff9ad9ea";
                break;
            case 12: col="ffa349a3";
                break;
            case 13: col="ffffffff";
                break;
        }
        return col;
    }

    public static Bitmap getBitmap(float x, float y, Bitmap b, int c, String cookies){

        int xd = pxToDp(x);
        int yd = pxToDp(y);

        int number = (yd/size_pix) * count_pix + xd/size_pix;

        xd = dpToPx((xd/size_pix) * size_pix);
        yd = dpToPx((yd/size_pix) * size_pix);

        String col=getCol(c);

        if (Integer.toHexString(b.getPixel(xd+2,yd+2)) != col) {
            col = "#"+col;
            Canvas canvas = new Canvas(b);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.parseColor(col));

            canvas.drawRect(xd + 1, yd + 1, xd + dpToPx(size_pix) - 1, yd + dpToPx(size_pix) - 1, paint);

            if (c < 13) {
                Connection_Post_quad_paint quad = new Connection_Post_quad_paint();
                quad.cookie = cookies;
                quad.urll = "http://croco.us-west-2.elasticbeanstalk.com/api/lobby/quad/" + Integer.toString(number);
                quad.execute("color=" + Integer.toString(c));

            } else {
                Connection_Delete_quad quad = new Connection_Delete_quad();
                quad.cookie = cookies;
                quad.urll = "http://croco.us-west-2.elasticbeanstalk.com/api/lobby/quad/" + Integer.toString(number);
                quad.execute("color=" + Integer.toString(c));

            }
        }

        return b;
    }

    public static Bitmap getUserBitmap (Bitmap b, Integer n, Integer c){

        int y = dpToPx((n/count_pix)*8);
        int x = dpToPx((n%count_pix)*8);

        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.parseColor("#"+getCol(c)));
        canvas.drawRect(x+1, y+1, x + dpToPx(8)-1, y+dpToPx(8)-1, paint);

        return b;
    }

    public static Bitmap getNewUserBitmap (Bitmap b) {

        b = Bitmap.createBitmap(dpToPx(160), dpToPx(160), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(Color.DKGRAY);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        int x = 0;
        for (int i = 0; i < count_pix; i++) {
            int y = 0;
            for (int j = 0; j < count_pix; j++) {
                canvas.drawRect(x + 1, y + 1, x + dpToPx(8) - 1, y + dpToPx(8) - 1, paint);
                y += dpToPx(8);
            }
            x += dpToPx(8);
        }

        return b;
    }


}
