package com.stapp.trovaescape.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.maps.android.ui.IconGenerator;
import com.stapp.trovaescape.BuildConfig;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import java.util.ArrayList;

import static android.content.Context.CONSUMER_IR_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static boolean isFirstRun(Context context) {

        //final String PREFS_NAME = "PrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        //final int DOESNT_EXIST = -1;

        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, Constants.DOESNT_EXIST_INT);

        boolean firstRun = true;
        if (currentVersionCode == savedVersionCode)
            firstRun = false;
        else if (currentVersionCode > savedVersionCode)
            firstRun = false;

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();

        return firstRun;
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*public String getAllTagsFormatted(ArrayList<Room> rooms){
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < rooms.size(); i++) {
            t.append(getTagsFormatted(rooms.get(i).getTags()));
            if(i != rooms.size()-1)
                t.append(" ");
        }
        return t.toString();
    }*/

    public static String[] getTagsFormatted(String tags){
        //StringBuilder t = new StringBuilder();
        //String[] tagsArr = tags.split(Constants.FIELDS_SEP);

        /*for(int i = 0; i < tagsArr.length; i++){
            if(i != 0)
                t.append(" ");
            t.append("#");
            t.append(tagsArr[i]);
        }
        return t.toString();*/
        return tags.split(Constants.FIELDS_SEP);
    }

    public static String getPricesFormatted(Context context, String prices){
        StringBuilder t = new StringBuilder();
        String[] pricesArr = prices.split(Constants.FIELDS_SEP);

        if(pricesArr.length > 0) {
            if(!pricesArr[0].equals("")) {
                for (int i = 0; i < pricesArr.length; i++) {
                    if (i != 0)
                        t.append("\n");
                    t.append("  - ");
                    t.append(pricesArr[i]);
                }
            }
        }
        return t.toString();
    }

    public static String[] getAvailsFormatted(Context context, String avails){
        //StringBuilder t = new StringBuilder();
        //String[] availsArr = avails.split(Constants.FIELDS_SEP);

        /*if(availsArr.length > 0) {
            if(!availsArr[0].equals("")) {
                for (int i = 0; i < availsArr.length; i++) {
                    if (i != 0)
                        t.append("\n");
                    //t.append("  ");
                    t.append(availsArr[i]);
                }
            }
        }
        return t.toString();*/
        return avails.split(Constants.FIELDS_SEP);
    }

    public static String getRoomsDoneRatio(Escape escape){
        StringBuilder t = new StringBuilder();
        int done = 0;
        ArrayList<Room> rooms = escape.getRoom();
        for(int i = 0; i < rooms.size(); i++){
            if(rooms.get(i).getDone())
                done++;
        }
        t.append(done);
        t.append(" / ");
        t.append(rooms.size());

        return t.toString();
    }

    public static Bitmap getHourItem(Context context, String hour){
        IconGenerator hourItem = new IconGenerator(context);
        hourItem.setTextAppearance(context, R.style.hourText);
        hourItem.setBackground(context.getDrawable(R.drawable.hour_background));

        return hourItem.makeIcon(hour);
    }

    public static Bitmap getTagItem(Context context, String tag){
        IconGenerator tagItem = new IconGenerator(context);
        tagItem.setTextAppearance(context, R.style.tagText);
        if(tag.equals(Constants.HORROR_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.red_background));
        else if(tag.equals(Constants.ACTORS_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.pink_background));
        else if(tag.equals(Constants.ADVENTURE_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.green_background));
        else if(tag.equals(Constants.MISTERY_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.yellow_background));
        else if(tag.equals(Constants.ACTION_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.light_blue_background));
        else if(tag.equals(Constants.CAZZEGGIO_TAG))
            tagItem.setBackground(context.getDrawable(R.drawable.violet_background));
        else
            tagItem.setBackground(context.getDrawable(R.drawable.hour_background));

        return tagItem.makeIcon("#"+tag);
    }

}
