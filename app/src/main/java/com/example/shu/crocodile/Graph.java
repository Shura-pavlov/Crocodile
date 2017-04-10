package com.example.shu.crocodile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by shu on 02.04.17.
 */

public class Graph {

    public static int dpToPx(int dp){
        return(int)(dp* Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px){
        return(int)(px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static Bitmap newBitmap (Bitmap b){
        b = Bitmap.createBitmap(dpToPx(340),dpToPx(340), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(Color.DKGRAY);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("white"));

        int x = 0;
        for (int i = 0; i<17; i++){
            int y = 0;
            for (int j = 0; j<17; j++) {
                canvas.drawRect(x, y, x + dpToPx(20) - 2, y + dpToPx(20) - 2, paint);
                y += dpToPx(20);
            }
            x += dpToPx(20);
        }

        return b;
    }

    public static Bitmap getBitmap(float x, float y, Bitmap b, int c){

        int xd = pxToDp((int)x);
        xd = dpToPx((xd/20) * 20);
        int yd = pxToDp((int)y);
        yd = dpToPx((yd/20) * 20);

        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);


        //TO_DO:normal color
        switch (c){
            case 0: paint.setColor(Color.parseColor("#FF000000"));
                    break;
            case 1: paint.setColor(Color.parseColor("#FFF17A0A"));
                    break;
            case 2: paint.setColor(Color.parseColor("#FFCC0000"));
                    break;
            case 3: paint.setColor(Color.parseColor("red"));
                    break;
            case 4: paint.setColor(Color.parseColor("#FFFF4081"));
                    break;
            case 5: paint.setColor(Color.parseColor("#FFFFBB33"));
                    break;
            case 6: paint.setColor(Color.parseColor("yellow"));
                    break;
            case 7: paint.setColor(Color.parseColor("#FF99CC00"));
                    break;
            case 8: paint.setColor(Color.parseColor("#FF669900"));
                    break;
            case 9: paint.setColor(Color.parseColor("#FF00DDFF"));
                    break;
            case 10: paint.setColor(Color.parseColor("blue"));
                    break;
            case 11: paint.setColor(Color.parseColor("#FF33B5E5"));
                    break;
            case 12: paint.setColor(Color.parseColor("#FFAA66CC"));
                    break;
            case 13: paint.setColor(Color.parseColor("white"));
                     break;
        }

        canvas.drawRect(xd, yd, xd + dpToPx(20)-2, yd+dpToPx(20)-2, paint);

        return b;
    }
}
