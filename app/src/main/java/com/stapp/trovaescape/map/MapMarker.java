package com.stapp.trovaescape.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.ui.IconGenerator;
import com.stapp.trovaescape.R;

public class MapMarker {

    private boolean freeRooms = false;
    private String name = "";
    private Context context = null;

    private int side = 10;
    private int outerCorner = 12;
    private int innerCorner = 10;
    private int xText = 0;
    private int yText = 0;
    private int w = 0;
    private int h = 0;

    public MapMarker(boolean freeRooms, String name, Context context) {
        this.freeRooms = freeRooms;
        this.name = name;
        this.context = context;
    }

    public MapMarker(boolean freeRooms, Context context) {
        this.freeRooms = freeRooms;
        this.context = context;
    }

    public Bitmap getMarker(){
        // paint that defines the text color
        // and measures its size
        /*TextPaint textColor = new TextPaint();
        textColor.setTextSize(35);
        textColor.setTextAlign(Paint.Align.LEFT);
        textColor.setColor(Color.BLACK);
        textColor.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        Rect textRect = new Rect();
        textColor.getTextBounds(name, 0, name.length(), textRect);
        setMarkerSize(textRect.width(), textRect.height());


        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
        Canvas canvas = new Canvas(bmp);

        Paint outerColor = new Paint();
        if(freeRooms)
            outerColor.setColor(Color.GREEN);
        else
            outerColor.setColor(Color.RED);
        Paint innerColor = new Paint();
        innerColor.setColor(Color.WHITE);

        RectF outerRect = new RectF(0, h, w, 0);
        canvas.drawRoundRect(outerRect, outerCorner, outerCorner, outerColor);
        RectF innerRect = new RectF(side, h-side, w-side, side);
        canvas.drawRoundRect(innerRect, innerCorner, innerCorner, innerColor);

        canvas.drawText(name, xText, yText, textColor);*/

        IconGenerator marker = new IconGenerator(context);
        marker.setTextAppearance(context, R.style.markerText);
        if(freeRooms)
            marker.setBackground(context.getDrawable(R.drawable.marker_background_green));
        else
            marker.setBackground(context.getDrawable(R.drawable.marker_background_red));

        return marker.makeIcon(name);
    }

    public Bitmap getDefaultMarker(){
        Drawable drawable;
        if(freeRooms)
            drawable = context.getDrawable(R.drawable.default_marker_green);
        else
            drawable = context.getDrawable(R.drawable.default_marker_red);
        Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private void setMarkerSize(int width, int height){
        int wTextMargin = 5;
        int hTextMargin = 8;

        w = 2 * side + 2 * wTextMargin + width;
        h = 2 * side + 2 * hTextMargin + height;

        xText = side + wTextMargin;
        yText = side + hTextMargin + height;
    }

}