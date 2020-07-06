package com.stapp.trovaescape.splashscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data_management.DataRetriever;
import com.stapp.trovaescape.data_management.VolleyResponseListener;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.main.MainActivity;
import com.stapp.trovaescape.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SplashScreen extends FragmentActivity {

    RequestQueue rs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DataRetriever dataRetriever = new DataRetriever(getApplicationContext());
        final DataManager dataManager = new DataManager(this);

        setContentView(R.layout.splashscreen);

        if(Utils.isFirstRun(this)) {
            if (!Utils.isConnected(this)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.internet_alert_title)
                        .setMessage(R.string.internet_alert_text)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            dataManager.setDbTime(Constants.TIME_0);
        }

        if(dataManager.countDbTime() != 1){
            dataManager.deleteDbTime();
            dataManager.setDbTime(Constants.TIME_0);
        }

        rs = Volley.newRequestQueue(this);

        // get time
        dataRetriever.sendJsonRequest(rs, Constants.LTU_URL, new VolleyResponseListener() {
            @Override
            public void onErrorResponse(String message) {
                Log.d("respErrorLTU", "responseErrorLTU = " + message);
                finish();
            }

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.d("respOKLTU", "responseOKLTU = " + response);
                    String responseStr = response.toString().trim();
                    Pattern regex = Pattern.compile("\"[0-9]{14}\"");
                    if (regex.matcher(responseStr).find()) {
                        final String ltu = dataRetriever.getTimeString(response);
                        String dbTime = dataManager.getDbTime();

                        // get data
                        if(!ltu.equals(Constants.NULL_TIME)){
                            if(Long.parseLong(ltu) > Long.parseLong(dbTime)) {
                                dataRetriever.sendJsonRequest(rs, Constants.DATA_URL, new VolleyResponseListener() {
                                    @Override
                                    public void onErrorResponse(String message) {
                                        Log.d("respErrorDATA", "responseErrorDATA = " + message);
                                        dataManager.fillDbWithoutInternet();
                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response != null) {
                                            Log.d("respOKDATA", "responseOKDATA = " + response);
                                            ArrayList<Escape> escapes = dataRetriever.getEscapesArray(response);
                                            if (escapes.size() > 0) {
                                                dataManager.updateDbTime(ltu);
                                                dataManager.fillDb(escapes);
                                            } else {
                                                dataManager.fillDbWithoutInternet();
                                            }
                                            startMainActivity();
                                        } else {
                                            Log.d("respNullDATA", "responseDATA = null");
                                            dataManager.fillDbWithoutInternet();
                                            startMainActivity();
                                        }
                                    }
                                });
                            }else{
                                startMainActivity();
                            }
                        }else {
                            finish();
                        }

                    }else{
                        Log.d("wrongDateLTU", "responseLTU = " + response);
                        finish();
                    }
                } else {
                    Log.d("respNullLTU", "responseLTU = null");
                    finish();
                }
            }
        });
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
