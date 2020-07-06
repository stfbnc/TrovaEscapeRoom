package com.stapp.trovaescape.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.maps.android.ui.IconGenerator;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.utils.Utils;

public class MapMarker {

    private Escape escape;
    private Context context;

    public MapMarker(Escape escape, Context context){
        this.escape = escape;
        this.context = context;
    }

    public Bitmap getMarker(){
        IconGenerator marker = new IconGenerator(context);
        marker.setTextAppearance(context, R.style.markerText);
        if(escape.getFree()){
            marker.setBackground(context.getDrawable(R.drawable.marker_background_green));
        }else{
            if(escape.getUncertain())
                marker.setBackground(context.getDrawable(R.drawable.marker_background_yellow));
            else
                marker.setBackground(context.getDrawable(R.drawable.marker_background_red));
        }

        return marker.makeIcon(escape.getShortName() + " \u25CF " + Utils.getRoomsDoneRatio(escape));
    }

    public Bitmap getDefaultMarker(){
        Drawable drawable;
        if(escape.getFree())
            drawable = context.getDrawable(R.drawable.default_marker_green);
        else
            drawable = context.getDrawable(R.drawable.default_marker_red);
        if(drawable != null) {
            Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bmp;
        }else{
            return null;
        }
    }

}
