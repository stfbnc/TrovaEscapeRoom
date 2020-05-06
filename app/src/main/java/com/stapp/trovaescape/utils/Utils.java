package com.stapp.trovaescape.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.stapp.trovaescape.BuildConfig;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Room;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static boolean isFirstRun(Context context) {

        final String PREFS_NAME = "PrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        boolean firstRun = true;
        if (currentVersionCode == savedVersionCode)
            firstRun = false;
        else if (savedVersionCode == DOESNT_EXIST)
            firstRun = true;
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

    public String getAllTagsFormatted(ArrayList<Room> rooms){
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < rooms.size(); i++) {
            t.append(getTagsFormatted(rooms.get(i).getTags()));
            if(i != rooms.size()-1)
                t.append(" ");
        }
        return t.toString();
    }

    public String getTagsFormatted(String tags){
        StringBuilder t = new StringBuilder();
        String[] tagsArr = tags.split(Constants.FIELDS_SEP);

        for(int i = 0; i < tagsArr.length; i++){
            if(i != 0)
                t.append(" ");
            t.append("#");
            t.append(tagsArr[i]);
        }
        return t.toString();
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

    public static String getAvailsFormatted(Context context, String avails){
        StringBuilder t = new StringBuilder();
        String[] availsArr = avails.split(Constants.FIELDS_SEP);

        if(availsArr.length > 0) {
            if(!availsArr[0].equals("")) {
                for (int i = 0; i < availsArr.length; i++) {
                    if (i != 0)
                        t.append("\n");
                    t.append("  ");
                    t.append(availsArr[i]);
                }
            }
        }
        return t.toString();
    }

}
