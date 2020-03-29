package com.stapp.trovaescape.data_management;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DataRetriever implements VolleyResponseListener {

    private Context context;
    private String time;
    private ArrayList<Escape> escapes;

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

    public String requestUpdateTime(){
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
                    Pattern regex = Pattern.compile("^\\d{14}$");
                    if (regex.matcher(responseStr).matches()) {
                        time = getTime(response);
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
        return time;
    }

    private String getTime(JSONObject obj){
        String t;
        try {
            t = obj.get(Constants.DB_TIME).toString();
        }catch (JSONException je) {
            Log.d("respKO", "responseKO = " + obj);
            t = Constants.NULL_TIME;
        }
        return t;
    }

    public ArrayList<Escape> requestData(){
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
    }

    private ArrayList<Escape> getEscapes(JSONObject obj){
        ArrayList<Escape> e = new ArrayList<>();

        return e;
    }

    @Override
    public void onErrorResponse(String str){}

    @Override
    public void onResponse(JSONObject jobj){}

}
