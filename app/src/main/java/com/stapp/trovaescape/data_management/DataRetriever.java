package com.stapp.trovaescape.data_management;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DataRetriever implements VolleyResponseListener {

    private Context context;
    //private String time = "";
    //private ArrayList<Escape> escapes;

    public DataRetriever(Context context) {
        this.context = context;
    }

    public void sendJsonRequest(RequestQueue rs, String url, final VolleyResponseListener listener){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { listener.onResponse(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                listener.onErrorResponse(error.toString());
            }
        });
        rs.add(jsonObjectRequest);
    }

    /*public void requestUpdateTime(){
        RequestQueue rs = Volley.newRequestQueue(context);
        sendJsonRequest(rs, Constants.LTU_URL, new VolleyResponseListener() {
            @Override
            public void onErrorResponse(String message) {
                Log.d("respErrorLTU", "responseErrorLTU = " + message);
                time = Constants.NULL_TIME;
            }

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.d("respOKLTU", "responseOKLTU = " + response);
                    String responseStr = response.toString().trim();
                    Pattern regex = Pattern.compile("\"[0-9]{14}\"");
                    if (regex.matcher(responseStr).find()) {
                        setTime(response);
                    }else{
                        Log.d("wrongDateLTU", "responseLTU = " + response);
                        time = Constants.NULL_TIME;
                    }
                } else {
                    Log.d("respNullLTU", "responseLTU = null");
                    time = Constants.NULL_TIME;
                }
            }
        });
    }*/

    public String getTimeString(JSONObject obj){
        try {
            return obj.get(Constants.DB_TIME).toString();
        }catch (JSONException je) {
            Log.d("respKO", "responseKO = " + obj);
            return Constants.NULL_TIME;
        }
    }

    /*public ArrayList<Escape> requestData(){
        RequestQueue rs = Volley.newRequestQueue(context);
        sendJsonRequest(rs, Constants.DATA_URL, new VolleyResponseListener() {
            @Override
            public void onErrorResponse(String message) {
                Log.d("respErrorDATA", "responseErrorDATA = " + message);
                escapes =  new ArrayList<>();
            }

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.d("respOKDATA", "responseOKDATA = " + response);
                    escapes = getEscapes(response);
                } else {
                    Log.d("respNullDATA", "responseDATA = null");
                    escapes =  new ArrayList<>();
                }
            }
        });
        return escapes;
    }*/

    public ArrayList<Escape> getEscapesArray(JSONObject obj){
        try{
            ArrayList<Escape> e = new ArrayList<>();
            JSONArray escapesArr = obj.getJSONArray(Constants.ESCAPE_SECTION);
            for(int i = 0; i < escapesArr.length(); i++){
                JSONObject eObj = escapesArr.getJSONObject(i);
                Escape escape = new Escape();
                escape.setName(eObj.getString(Constants.ESCAPE_NAME));
                escape.setAddress(eObj.getString(Constants.ESCAPE_ADDRESS));
                escape.setPhone(eObj.getString(Constants.ESCAPE_PHONE));
                escape.setWebsite(eObj.getString(Constants.ESCAPE_WEBSITE));
                escape.setCoords(new LatLng(eObj.getDouble(Constants.ESCAPE_LAT), eObj.getDouble(Constants.ESCAPE_LON)));
                escape.setCode(eObj.getString(Constants.ESCAPE_CODE));

                ArrayList<Room> r = new ArrayList<>();
                JSONArray roomArr = eObj.getJSONArray(Constants.ROOM_SECTION);
                for(int j = 0; j < roomArr.length(); j++){
                    JSONObject rObj = roomArr.getJSONObject(j);
                    Room room = new Room();
                    room.setName(rObj.getString(Constants.ROOM_NAME));
                    room.setWebsite(rObj.getString(Constants.ROOM_WEBSITE));
                    room.setPrices(rObj.getString(Constants.ROOM_PRICES));
                    room.setAvailabilities(rObj.getString(Constants.ROOM_AVAIL));
                    room.setCode(rObj.getString(Constants.ROOM_CODE));
                    r.add(room);
                }
                escape.setRooms(r);
                e.add(escape);
            }
            return e;
        }catch (JSONException je){
            Log.d("respKO", "responseKO = " + obj);
            return new ArrayList<>();
        }
    }

    @Override
    public void onErrorResponse(String str){}

    @Override
    public void onResponse(JSONObject jobj){}

}
