package com.stapp.trovaescape.data_management;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataRetriever implements VolleyResponseListener {

    private Context context;

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

    public String getTimeString(JSONObject obj){
        try {
            return obj.get(Constants.DB_TIME).toString();
        }catch (JSONException je) {
            Log.d("respKO", "responseKO = " + obj);
            return Constants.NULL_TIME;
        }
    }

    public ArrayList<Escape> getEscapesArray(JSONObject obj){
        try{
            ArrayList<Escape> e = new ArrayList<>();
            JSONArray escapesArr = obj.getJSONArray(Constants.ESCAPE_SECTION);
            for(int i = 0; i < escapesArr.length(); i++){
                JSONObject eObj = escapesArr.getJSONObject(i);
                Escape escape = new Escape(context);
                escape.setName(eObj.getString(Constants.ESCAPE_NAME).replace("'", "\""));
                escape.setShortName(eObj.getString(Constants.ESCAPE_SHORT_NAME).replace("'", "\""));
                escape.setAddress(eObj.getString(Constants.ESCAPE_ADDRESS).replace("'", "\""));
                escape.setPhone(eObj.getString(Constants.ESCAPE_PHONE));
                escape.setWebsite(eObj.getString(Constants.ESCAPE_WEBSITE).replace("'", "\""));
                escape.setCoords(new LatLng(eObj.getDouble(Constants.ESCAPE_LAT), eObj.getDouble(Constants.ESCAPE_LON)));
                escape.setTags(eObj.getString(Constants.ESCAPE_TAGS));
                escape.setCode(eObj.getString(Constants.ESCAPE_CODE));

                ArrayList<Room> r = new ArrayList<>();
                JSONArray roomArr = eObj.getJSONArray(Constants.ROOM_SECTION);
                for(int j = 0; j < roomArr.length(); j++){
                    JSONObject rObj = roomArr.getJSONObject(j);
                    Room room = new Room();
                    room.setName(rObj.getString(Constants.ROOM_NAME).replace("'", "\""));
                    room.setWebsite(rObj.getString(Constants.ROOM_WEBSITE).replace("'", "\""));
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
