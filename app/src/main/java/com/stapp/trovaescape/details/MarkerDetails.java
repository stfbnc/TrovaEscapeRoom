package com.stapp.trovaescape.details;

/*import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import it.trovaescape.R;
import it.trovaescape.data.Escape;
import it.trovaescape.data.EscapeData;*/

public class MarkerDetails {

 /*   private PopupWindow mpopup;
    private Context ctx;
    private Marker marker;

    public MarkerDetails(Context ctx, Marker marker){
        this.ctx = ctx;
        this.marker = marker;
    }

    public void showDetails(){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View popupView = inflater.inflate(R.layout.marker_details, null);
        setDetailData(popupView);

        mpopup = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        WindowManager wm = (WindowManager) popupView.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        mpopup.setWidth(width*8/10);
        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private void setDetailData(View v){
        Escape escapes = new Escape(ctx);
        EscapeData md = escapes.getEscapesList().get(marker.getPosition());
        TextView name = v.findViewById(R.id.name);
        name.setText(md.getName());
        TextView street = v.findViewById(R.id.street);
        street.setText(md.getStreet());
        TextView tel = v.findViewById(R.id.tel);
        tel.setText(md.getTel());
        TextView website = v.findViewById(R.id.website);
        website.setText(Html.fromHtml(md.getWebsite()));
        website.setMovementMethod(LinkMovementMethod.getInstance());
        website.setLinksClickable(true);
    }

  */

}
