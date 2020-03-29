package com.stapp.trovaescape.data_management;

import org.json.JSONObject;

public interface VolleyResponseListener {

    void onErrorResponse(String message);
    void onResponse(JSONObject response);

}
