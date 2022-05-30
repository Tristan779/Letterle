package com.example.letterle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Database {

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    private final Context context;
    private RequestQueue requestQueue;

    public int wins1Try;
    public int wins2Try;
    public int wins3Try;
    public int wins4Try;
    public int wins5Try;
    public int wins6Try;
    public int currentStreak;
    public int maxStreak;
    public int gamesPlayed;

    public Database(Context context) {
        this.context = context;
        uniqueID(context);
    }

    public int getWins1Try() {
        return wins1Try;
    }

    public int getWins2Try() {
        return wins2Try;
    }

    public int getWins3Try() {
        return wins3Try;
    }

    public int getWins4Try() {
        return wins4Try;
    }

    public int getWins5Try() {
        return wins5Try;
    }

    public int getWins6Try() {
        return wins6Try;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }



    public synchronized static void uniqueID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
    }


    public void grabUserStats() {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a21pt203/getStatsUser/" + uniqueID;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        if(!response.isNull(0)){
                            for( int i = 0; i < response.length(); i++ ) {
                                JSONObject curObject = response.getJSONObject(i);
                                wins1Try = curObject.getInt("Wins1try");
                                wins2Try = curObject.getInt("Wins2try");
                                wins3Try = curObject.getInt("Wins3try");
                                wins4Try = curObject.getInt("Wins4try");
                                wins5Try = curObject.getInt("Wins5try");
                                wins6Try = curObject.getInt("Wins6try");
                                currentStreak = curObject.getInt("CurrStreak");
                                maxStreak = curObject.getInt("MaxStreak");
                                gamesPlayed = curObject.getInt("Played");
                            }
                        }
                        else{
                            makeNewAccount();
                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);
        System.out.println(uniqueID + " / " + wins1Try);
    }


    public void updateUserStats(boolean won, int tries) {
        gamesPlayed++;
        if(won) {
            currentStreak++;
            if (currentStreak > maxStreak) {
                maxStreak++;
            }
            switch (tries) {
                case 1 -> wins1Try++;
                case 2 -> wins2Try++;
                case 3 -> wins3Try++;
                case 4 -> wins4Try++;
                case 5 -> wins5Try++;
                case 6 -> wins6Try++;
            }
        }
        if(!won)
        {
            currentStreak = 0;
        }
        updateUserDatabase();
    }

    public void updateUserDatabase(){
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a21pt203/updateStatData/"+ wins1Try +"/"
                + wins2Try +"/"+ wins3Try +"/"+ wins4Try +"/"+ wins5Try +"/"+ wins6Try +"/"+ currentStreak +"/"+ maxStreak +"/"+ gamesPlayed
                +"/"+uniqueID;

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        new JSONArray(response);

                    } catch (JSONException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                },
                error -> {
                }
        );

        requestQueue.add(submitRequest);
    }

    public void makeNewAccount()
    {

        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a21pt203/makeNewUser/"+uniqueID;

        Request<String> submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        new JSONArray(response);

                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },
                error -> {
                }
        );

        requestQueue.add(submitRequest);
        grabUserStats();
    }
}
