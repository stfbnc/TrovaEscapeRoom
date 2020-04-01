package com.stapp.trovaescape.splashscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data_management.DataRetriever;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.main.MainActivity;
import com.stapp.trovaescape.utils.Utils;

import java.util.ArrayList;

public class SplashScreen extends FragmentActivity {

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

        String ltu = dataRetriever.requestUpdateTime();
        String dbTime = dataManager.getDbTime();
        if(!ltu.equals(Constants.NULL_TIME) && (Integer.valueOf(ltu) > Integer.valueOf(dbTime))) {
            ArrayList<Escape> escapes = dataRetriever.requestData();
            if(escapes.size() > 0) {
                dataManager.updateDbTime(ltu);
                dataManager.fillDb(escapes);
            }else{
                dataManager.fillDbWithoutInternet();
            }
        }
        startMainActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
