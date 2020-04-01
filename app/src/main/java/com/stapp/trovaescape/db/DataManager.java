package com.stapp.trovaescape.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final int ESCAPE_TAB_ID_IDX = 0;
    public static final int ESCAPE_TAB_NAME_IDX = 1;
    public static final int ESCAPE_TAB_ADDRESS_IDX = 2;
    public static final int ESCAPE_TAB_PHONE_IDX = 3;
    public static final int ESCAPE_TAB_WEBSITE_IDX = 4;
    public static final int ESCAPE_TAB_LAT_IDX = 5;
    public static final int ESCAPE_TAB_LON_IDX = 6;
    public static final int ESCAPE_TAB_CODE_IDX = 7;

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
    public static final int ROOM_TAB_ESCAPE_ID_IDX = 5;
    public static final int ROOM_TAB_CODE_IDX = 6;

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
                       " WHERE " + LTU_TAB_ID + " =  1;";
        Log.i("getDbTime() = ", query);
        Cursor c = db.rawQuery(query,null);
        String time = c.getString(LTU_TAB_TIME_IDX);
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
                ESCAPE_TAB_CODE + ") " +
                "VALUES ('" + e.getName() + "', " +
                "'" + e.getAddress() + "', " +
                "'" + e.getPhone() + "', " +
                "'" + e.getWebsite() + "', " +
                e.getCoords().latitude + ", " +
                e.getCoords().longitude + ", " +
                "'" + e.getCode() + "');";
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

    public void getEscapes(String pattern){

    }

    public void getEscapeRooms(int escapeId){

    }

    /*

    public Cursor paytabSelectAll(){
        Cursor c = db.rawQuery("SELECT * FROM "+PAYTAB+" ORDER BY "+PAYTAB_DATE+" DESC;", null);
        return c;
    }

    public Cursor paytabSelect(Bundle bundle){
        String query = "SELECT * FROM "+PAYTAB;
        ArrayList<String> arr = new ArrayList<>();
        String date1 = bundle.getString(BaseActivity.START_DATE);
        String date2 = bundle.getString(BaseActivity.END_DATE);
        String fltr_type = bundle.getString(BaseActivity.FILTER_TYPE);
        String fltr_dscr = bundle.getString(BaseActivity.FILTER_DSCR);
        int fltr_ioro = bundle.getInt(BaseActivity.FILTER_IORO);
        if(!fltr_type.equals(context.getResources().getString(R.string.empty_type)))
            arr.add(PAYTAB_TYPE+" = '"+fltr_type+"'");
        if(!fltr_dscr.isEmpty())
            arr.add(PAYTAB_DSCR+" LIKE '%"+fltr_dscr+"%'");
        if(fltr_ioro != BaseActivity.BOTH)
            arr.add(PAYTAB_IORO+" = "+fltr_ioro);
        if(!date1.isEmpty()){
            if(date2.isEmpty())
                arr.add(PAYTAB_DATE+" = '"+date1+"'");
            else
                arr.add(PAYTAB_DATE+" BETWEEN '"+date1+"' AND '"+date2+"'");
        }
        if(arr.size() > 0) {
            query += " WHERE ";
            for (int i = 0; i < arr.size(); i++) {
                if (i == arr.size() - 1)
                    query += arr.get(i);
                else
                    query += arr.get(i) + " AND ";
            }
        }
        query += " ORDER BY " + PAYTAB_DATE + " DESC;";
        Log.i("paytabSelect() = ", query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor paytabSelectById(Integer urno){
        Cursor c = db.rawQuery("SELECT * FROM "+PAYTAB+" WHERE "+PAYTAB_ID+" = "+urno+";",null);
        return c;
    }

    public double paytabSelectTotal(String urnos, int in_out){
        Cursor c = db.rawQuery("SELECT SUM("+PAYTAB_EURO+") FROM "+PAYTAB+" WHERE "+PAYTAB_ID+" IN ("+urnos+") AND "+PAYTAB_IORO+" = "+in_out+";",null);
        if(c.moveToFirst()){
            double sum = c.getDouble(0);
            c.close();
            return sum;
        }else{
            return 0.0;
        }
    }

    public String[] paytabSelectMax(String urnos, int in_out){
        Cursor c = db.rawQuery("SELECT MAX("+PAYTAB_EURO+"), "+PAYTAB_DATE+" FROM "+PAYTAB+" WHERE "+PAYTAB_ID+" IN ("+urnos+") AND "+PAYTAB_IORO+" = "+in_out+";",null);
        if(c.moveToFirst()){
            double max = c.getDouble(0);
            String date = c.getString(1);
            c.close();
            if(date != null)
                return new String[]{String.format("%.2f", max), date};
            else
                return new String[]{"0.0", ""};
        }else{
            return new String[]{"0.0", ""};
        }
    }

    public Cursor paytabSelectEuroGroupByType(String urnos, int in_out){
        Cursor c = db.rawQuery("SELECT SUM("+PAYTAB_EURO+"), "+PAYTAB_TYPE+" FROM "+PAYTAB+" WHERE "+PAYTAB_ID+" IN ("+urnos+")"+
                " AND "+PAYTAB_IORO+" = "+in_out+" GROUP BY "+PAYTAB_TYPE+";",null);
        return c;
    }

    public Cursor paytabSelectEuroGroupByDate(String urnos){
        Cursor c = db.rawQuery("SELECT "+PAYTAB_DATE+", "+PAYTAB_IORO+", SUM("+PAYTAB_EURO+") FROM "+PAYTAB+" WHERE "+PAYTAB_ID+" IN ("+urnos+")"+
                " GROUP BY "+PAYTAB_DATE+", "+PAYTAB_IORO+" ORDER BY "+PAYTAB_DATE+";",null);
        return c;
    }

    public void typtabInsert(String type){
        String query = "INSERT INTO "+TYPTAB+
                " ("+TYPTAB_TYPE+") VALUES ('"+type+"');";
        Log.i("typtabInsert() = ", query);
        db.execSQL(query);
    }

    public Cursor typtabSelectTypes(){
        Cursor c = db.rawQuery("SELECT "+TYPTAB_TYPE+" FROM "+TYPTAB+";", null);
        return c;
    }

    public void updtabInsert(Bundle bndl){
        String query = "INSERT INTO "+UPDTAB+
                " ("+UPDTAB_CDAT+", "+UPDTAB_DATE+", "+UPDTAB_DSCR+", "+UPDTAB_EURO+", "+UPDTAB_TYPE+", "+UPDTAB_IORO+", "+UPDTAB_UPAY+") "+
                "VALUES ("+"'"+bndl.getString(UPDTAB_CDAT)+"', '"+bndl.getString(UPDTAB_DATE)+"', '"+bndl.getString(UPDTAB_DSCR)+"', "+
                bndl.getDouble(UPDTAB_EURO)+", '"+bndl.getString(UPDTAB_TYPE)+"', "+bndl.getInt(UPDTAB_IORO)+", "+bndl.getInt(UPDTAB_UPAY)+");";
        Log.i("updtabInsert() = ", query);
        db.execSQL(query);
    }

    public Cursor updtabSelectIds(Integer urno){
        Cursor c = db.rawQuery("SELECT "+UPDTAB_ID+" FROM "+UPDTAB+" WHERE "+UPDTAB_UPAY+" = "+urno+" ORDER BY "+UPDTAB_CDAT+" DESC;", null);
        return c;
    }*/

    /*public Cursor updtabSelectById(Integer urno){
        Cursor c = db.rawQuery("SELECT * FROM "+UPDTAB+" WHERE "+UPDTAB_ID+" = "+urno+";", null);
        return c;
    }*/

    /*public int countRows(String table){
        Cursor c = db.rawQuery("SELECT * FROM "+table+";", null);
        return c.getCount();
    }*/

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
                    ESCAPE_TAB_CODE + " TEXT NOT NULL);";
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
