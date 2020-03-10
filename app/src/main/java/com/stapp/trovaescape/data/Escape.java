package com.stapp.trovaescape.data;

/*import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Hashtable;

import it.trovaescape.R;

 */

public class Escape {

 /*   private Hashtable<LatLng, EscapeData> escapesData = new Hashtable<>();
    private Context ctx;

    public Escape(Context ctx){
        this.ctx = ctx;
        getData();
    }

    private void getData(){
        escapesData.put(new LatLng(41.935091, 12.422353),
                new EscapeData("Nox-Escape Room", "Via Pieve Ligure, 25, 00168", "327 652 4107",
                        "<a href=\"http://www.nox-escape.com/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.909659, 12.462738),
                new EscapeData("Locked Escape Room", "Via degli Scipioni, 236, 00192", "340 523 6953",
                        "<a href=\"http://www.lockedroma.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.894897, 12.472600),
                new EscapeData("Escape Room Campo dei Fiori", "Vicolo delle Grotte, 3, 00186", "334 726 1615",
                        "<a href=\"http://www.escapecampodeifiori.com/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.880152, 12.468753),
                new EscapeData("Game Over Escape Rooms", "Via Angelo Bargoni, 44, 00153", "06 581 0341",
                        "<a href=\"https://www.escapegameover.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.891307, 12.531987),
                new EscapeData("La Casa degli Enigmi", "Via Romanello da Forlì, 18, 00176", "379 104 1282",
                        "<a href=\"http://www.lacasadeglienigmi.com/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.895283, 12.517546),
                new EscapeData("Escape Room Resolute", "Viale dello Scalo S. Lorenzo, 51, 00185", "339 533 8755",
                        "<a href=\"https://www.escaperoomresolute.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.907153, 12.538760),
                new EscapeData("Escape Room Adventure Rooms", "Via Pio Molajoni, 72, 00159", "392 693 8064",
                        "<a href=\"http://adventurerooms.it/roma/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.912715, 12.507315),
                new EscapeData("Questhouse", "Viale Regina Margherita, 239 A, 00198", "06 8765 0586",
                        "<a href=\"https://questhouse.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.917147, 12.548469),
                new EscapeData("Escape Room Roma 2", "Via del Casale Fainelli, 69, 00199", "392 435 9562",
                        "<a href=\"https://www.escaperoomroma.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.921544, 12.515877),
                new EscapeData("Magic Escape Room", "Via Bolzano, 40, 00198", "370 114 1293",
                        "<a href=\"https://magicescape.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.928156, 12.522274),
                new EscapeData("Escape Room Roma", "Via Benadir, 2, 00199", "392 435 9562",
                        "<a href=\"https://www.escaperoomroma.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.948198, 12.556750),
                new EscapeData("Real Game Escape Room", "Via Ugo Falena, 11, 00137", "391 720 6160",
                        "<a href=\"http://www.escaperoomrealgame.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.950390, 12.572140),
                new EscapeData("Escape Room La Notte del Giudizio", "Via Goffredo Ciaralli, 96, 00156", "393 975 3045",
                        "<a href=\"https://panicroomescape.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.860649, 12.553435),
                new EscapeData("Escape Room Extended Intrappola.TO", "Via dei Fontej, 18, 00174", "334 773 3737",
                        "<a href=\"https://intrappola.to/escape-room/roma\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.874835, 12.519645),
                new EscapeData("Escape Room Alberone - Fugacemente", "Via Gino Capponi, 88, 00179", "351 832 1046",
                        "<a href=\"https://eneruescape.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.856632, 12.571523),
                new EscapeData("Escape Room Cinecittà - FugaceMente", "Viale Palmiro Togliatti, 70, 00173", "371 112 6805",
                        "<a href=\"https://eneruescape.it/escape-room-cinecitta/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.892150, 12.628017),
                new EscapeData("Escape Room Colle Prenestino - Fugacemente", "Via Calabritto, 12, 00132", "351 583 3117",
                        "<a href=\"https://www.fugacemente.it/roma-colle-prenestino/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.861522, 12.600286),
                new EscapeData("GetOutRoma", "Via Bernardo Cennini, 44, 00133", "327 105 4978",
                        "<a href=\"https://getoutroma.it/\">"+ctx.getString(R.string.site_string)+"</a>"));
        escapesData.put(new LatLng(41.838554, 12.624080),
                new EscapeData("Escape Room Tor Vergata - Fugacemente", "Via Gioacchino Volpe, 18, 00133", "349 825 4702",
                        "<a href=\"https://www.fugacemente.it/roma-tor-vergata/\">"+ctx.getString(R.string.site_string)+"</a>"));
    }

    public Hashtable<LatLng, EscapeData> getEscapesList(){
        return escapesData;
    }

    public ArrayList<LatLng> getEscapesCoords(){
        return new ArrayList<>(escapesData.keySet());
    }

  */

}
