package com.stapp.trovaescape.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import java.util.ArrayList;

public class DataManager {

    private SQLiteDatabase db;
    private Context context;

    // db escape table columns
    public static final String ESCAPE_TAB_ID = "ID"; // id
    public static final String ESCAPE_TAB_NAME = "NAME"; // name
    public static final String ESCAPE_TAB_ADDRESS = "ADDRESS"; // address
    public static final String ESCAPE_TAB_PHONE = "PHONE"; // phone
    public static final String ESCAPE_TAB_WEBSITE = "WEBSITE"; // website
    public static final String ESCAPE_TAB_LAT = "LAT"; // latitude
    public static final String ESCAPE_TAB_LON = "LON"; // longitude
    public static final String ESCAPE_TAB_CODE = "CODE"; // code identifier
    public static final String ESCAPE_TAB_SHORT_NAME = "SHORT_NAME"; // short name
    public static final String ESCAPE_TAB_TAGS = "TAGS"; // tags
    public static final int ESCAPE_TAB_ID_IDX = 0;
    public static final int ESCAPE_TAB_NAME_IDX = 1;
    public static final int ESCAPE_TAB_ADDRESS_IDX = 2;
    public static final int ESCAPE_TAB_PHONE_IDX = 3;
    public static final int ESCAPE_TAB_WEBSITE_IDX = 4;
    public static final int ESCAPE_TAB_LAT_IDX = 5;
    public static final int ESCAPE_TAB_LON_IDX = 6;
    public static final int ESCAPE_TAB_CODE_IDX = 7;
    public static final int ESCAPE_TAB_SHORT_NAME_IDX = 8;
    public static final int ESCAPE_TAB_TAGS_IDX = 9;

    // db room table columns
    public static final String ROOM_TAB_ID = "ID"; // id
    public static final String ROOM_TAB_NAME = "NAME"; // name
    public static final String ROOM_TAB_WEBSITE = "WEBSITE"; // website
    public static final String ROOM_TAB_PRICES = "PRICES"; // prices
    public static final String ROOM_TAB_AVAIL = "AVAIL"; // availabilities
    public static final String ROOM_TAB_CODE = "CODE"; // code identifier
    public static final int ROOM_TAB_ID_IDX = 0;
    public static final int ROOM_TAB_NAME_IDX = 1;
    public static final int ROOM_TAB_WEBSITE_IDX = 2;
    public static final int ROOM_TAB_PRICES_IDX = 3;
    public static final int ROOM_TAB_AVAIL_IDX = 4;
    public static final int ROOM_TAB_CODE_IDX = 5;

    // db ltu table columns
    public static final String LTU_TAB_ID = "ID"; // id
    public static final String LTU_TAB_TIME = "TIME"; // last time modified
    public static final int LTU_TAB_ID_IDX = 0;
    public static final int LTU_TAB_TIME_IDX = 1;

    //db info
    private static final String DB_NAME = "APP_DB";
    private static final int DB_VERSION = 1;
    private static final String ESCAPE_TAB = "ESCAPE_TAB"; // table with the escapes
    private static final String ROOM_TAB = "ROOM_TAB"; // table with the rooms
    private static final String LTU_TAB = "LTU_TAB"; // table with last time update info

    public DataManager(Context context){
        this.context = context;
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void setDbTime(String time){
        String query = "INSERT INTO " + LTU_TAB +
                       " (" + LTU_TAB_TIME + ") " +
                       "VALUES ('" + time + "');";
        Log.i("setDbTime() = ", query);
        db.execSQL(query);
    }

    public String getDbTime(){
        String query = "SELECT " + LTU_TAB_TIME +
                       " FROM " + LTU_TAB +
                       " WHERE " + LTU_TAB_ID + " = 1;";
        Log.i("getDbTime() = ", query);
        Cursor c = db.rawQuery(query,null);
        String time = Constants.NULL_TIME;
        if(c.moveToFirst()) {
            time = c.getString(0);
        }
        c.close();
        return time;
    }

    public void updateDbTime(String time){
        String query = "UPDATE " + LTU_TAB +
                       " SET " + LTU_TAB_TIME + " = '" + time + "'" +
                       " WHERE " + LTU_TAB_ID + " = 1;";
        Log.i("updateDbTime() = ", query);
        db.execSQL(query);
    }

    public void fillDb(ArrayList<Escape> e){
        db.execSQL("DELETE FROM "+ ESCAPE_TAB);
        db.execSQL("DELETE FROM "+ ROOM_TAB);
        for(int i = 0; i < e.size(); i++) {
            fillDbWithEscape(e.get(i));
            fillDbWithRooms(e.get(i).getRoom());
        }
    }

    public void fillDbWithoutInternet(){
        String query = "UPDATE " + ROOM_TAB +
                " SET " + ROOM_TAB_PRICES + " = '', " +
                ROOM_TAB_AVAIL + " = '';";
        Log.i("fillDbW/oInternet() = ", query);
        db.execSQL(query);
    }

    private void fillDbWithEscape(Escape e){
        String query = "INSERT INTO " + ESCAPE_TAB +
                " (" + ESCAPE_TAB_NAME + ", " +
                ESCAPE_TAB_ADDRESS + ", " +
                ESCAPE_TAB_PHONE + ", " +
                ESCAPE_TAB_WEBSITE + ", " +
                ESCAPE_TAB_LAT + ", " +
                ESCAPE_TAB_LON + ", " +
                ESCAPE_TAB_CODE + ", " +
                ESCAPE_TAB_SHORT_NAME + ", " +
                ESCAPE_TAB_TAGS + ") " +
                "VALUES ('" + e.getName() + "', " +
                "'" + e.getAddress() + "', " +
                "'" + e.getPhone() + "', " +
                "'" + e.getWebsite() + "', " +
                e.getCoords().latitude + ", " +
                e.getCoords().longitude + ", " +
                "'" + e.getCode() + "', " +
                "'" + e.getShortName() + "', " +
                "'" + e.getTags() + "');";
        Log.i("fillDbWithEscape() = ", query);
        db.execSQL(query);
    }

    private void fillDbWithRooms(ArrayList<Room> r){
        for(int i = 0; i < r.size(); i++) {
            Room room = r.get(i);
            String query = "INSERT INTO " + ROOM_TAB +
                    " (" + ROOM_TAB_NAME + ", " +
                    ROOM_TAB_WEBSITE + ", " +
                    ROOM_TAB_PRICES + ", " +
                    ROOM_TAB_AVAIL + ", " +
                    ROOM_TAB_CODE + ") " +
                    "VALUES ('" + room.getName() + "', " +
                    "'" + room.getWebsite() + "', " +
                    "'" + room.getPrices() + "', " +
                    "'" + room.getAvailabilities() + "', " +
                    "'" + room.getCode() + "');";
            Log.i("fillDbWithRooms() = ", query);
            db.execSQL(query);
        }
    }

    public ArrayList<Escape> getEscapes(String str){
        ArrayList<Escape> escapes = new ArrayList<>();
        String query = "SELECT * FROM " + ESCAPE_TAB;
        if(!str.equals(""))
            query += " WHERE LOWER(" + ESCAPE_TAB_NAME + ") LIKE '%" + str.toLowerCase() + "%'";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()) {
            do {
                Escape e = new Escape(context);
                e.setName(c.getString(ESCAPE_TAB_NAME_IDX));
                e.setShortName(c.getString(ESCAPE_TAB_SHORT_NAME_IDX));
                e.setAddress(c.getString(ESCAPE_TAB_ADDRESS_IDX));
                e.setPhone(c.getString(ESCAPE_TAB_PHONE_IDX));
                e.setWebsite(c.getString(ESCAPE_TAB_WEBSITE_IDX));
                e.setCode(c.getString(ESCAPE_TAB_CODE_IDX));
                e.setCoords(new LatLng(c.getDouble(ESCAPE_TAB_LAT_IDX), c.getDouble(ESCAPE_TAB_LON_IDX)));
                e.setTags(c.getString(ESCAPE_TAB_TAGS_IDX));
                e.setRooms(getEscapeRooms(e.getCode()));
                escapes.add(e);
            } while(c.moveToNext());
        }
        c.close();
        return escapes;
    }

    public ArrayList<Room> getEscapeRooms(String escapeId){
        ArrayList<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM " + ROOM_TAB + " WHERE " + ROOM_TAB_CODE +
                       " LIKE '" + escapeId + "_R%' ORDER BY " + ROOM_TAB_NAME;
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()) {
            do {
                Room r = new Room();
                r.setName(c.getString(ROOM_TAB_NAME_IDX));
                r.setWebsite(c.getString(ROOM_TAB_WEBSITE_IDX));
                r.setPrices(c.getString(ROOM_TAB_PRICES_IDX));
                r.setAvailabilities(c.getString(ROOM_TAB_AVAIL_IDX));
                r.setCode(c.getString(ROOM_TAB_CODE_IDX));
                rooms.add(r);
            } while (c.moveToNext());
        }
        c.close();
        return rooms;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            String escapeTable = "CREATE TABLE " + ESCAPE_TAB +
                    " (" + ESCAPE_TAB_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    ESCAPE_TAB_NAME + " TEXT NOT NULL, " +
                    ESCAPE_TAB_ADDRESS + " TEXT NOT NULL, " +
                    ESCAPE_TAB_PHONE + " TEXT NOT NULL, " +
                    ESCAPE_TAB_WEBSITE + " TEXT NOT NULL, " +
                    ESCAPE_TAB_LAT + " REAL NOT NULL, " +
                    ESCAPE_TAB_LON + " REAL NOT NULL, " +
                    ESCAPE_TAB_CODE + " TEXT NOT NULL, " +
                    ESCAPE_TAB_SHORT_NAME + " TEXT NOT NULL, " +
                    ESCAPE_TAB_TAGS + " TEXT NOT NULL);";
            db.execSQL(escapeTable);

            String roomTable = "CREATE TABLE " + ROOM_TAB +
                    " (" + ROOM_TAB_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    ROOM_TAB_NAME + " TEXT NOT NULL, " +
                    ROOM_TAB_WEBSITE + " TEXT NOT NULL, " +
                    ROOM_TAB_PRICES + " TEXT NOT NULL, " +
                    ROOM_TAB_AVAIL + " TEXT NOT NULL, " +
                    ROOM_TAB_CODE + " TEXT NOT NULL);";
            db.execSQL(roomTable);

            String ltuTable = "CREATE TABLE " + LTU_TAB +
                    " (" + LTU_TAB_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    LTU_TAB_TIME + " TEXT NOT NULL);";
            db.execSQL(ltuTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    }

}
