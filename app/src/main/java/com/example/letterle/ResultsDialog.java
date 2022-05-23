package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class ResultsDialog extends Dialog{

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public Context context;
    private RequestQueue requestQueue;
    int wins1Try;
    int wins2Try;
    int wins3Try;
    int wins4Try;
    int wins5Try;
    int wins6Try;
    int currentStreak;
    int maxStreak;
    int gamesPlayed;




    public ResultsDialog(Context context) {
        super(context);
        this.context = context;
        uniqueID(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceStace){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        //setContentView(R.layout.game_end_results);
        readNewDialogData();
    }

    public synchronized static String uniqueID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public ProgressBar getProgressBar(int index) {
        int id = context.getResources().getIdentifier("progressBar_" + index, "id", context.getPackageName());
        return findViewById(id);
    }


    public void readNewDialogData() {
        requestQueue = Volley.newRequestQueue(getContext());
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
    }

    public void setStatTiles() {

        TextView maxStreak = findViewById(R.id.textViewMaxStreak_nr);
        TextView currentStreak = findViewById(R.id.textViewCurrentStreak_nr);
        TextView win = findViewById(R.id.textViewWin_nr);
        TextView played = findViewById(R.id.textViewPlayed_nr);

        TextView winOne = findViewById(R.id.textViewWins_1);
        TextView winTwo = findViewById(R.id.textViewWins_2);
        TextView winTree = findViewById(R.id.textViewWins_3);
        TextView winFour = findViewById(R.id.textViewWins_4);
        TextView winFive = findViewById(R.id.textViewWins_5);
        TextView winSix = findViewById(R.id.textViewWins_6);

        maxStreak.setText(String.valueOf(this.maxStreak));
        currentStreak.setText(String.valueOf(this.currentStreak));

        winOne.setText(String.valueOf(this.wins1Try));
        winTwo.setText(String.valueOf(this.wins2Try));
        winTree.setText(String.valueOf(this.wins3Try));
        winFour.setText(String.valueOf(this.wins4Try));
        winFive.setText(String.valueOf(this.wins5Try));
        winSix.setText(String.valueOf(this.wins6Try));

        float wins = wins1Try + wins2Try + wins3Try + wins4Try + wins5Try + wins6Try;
        if(gamesPlayed == 0)
        {
            win.setText("0");
        }
        else{
            float winPercent = wins/gamesPlayed * 100;
            win.setText(String.valueOf((int)winPercent));
        }
        played.setText(String.valueOf(gamesPlayed));

        List<Integer> list = new ArrayList<>(
                Arrays.asList(wins1Try, wins2Try, wins3Try, wins4Try, wins5Try, wins6Try));

        for(int i = 1; i < 7; i++){
            float fill = (float) list.get(i-1)/ (float) Collections.max(list);
            getProgressBar(i).setProgress(Math.round(100 * fill));

        }
    }

    public void updateDialog(boolean won, int tries)
    {
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


        requestQueue = Volley.newRequestQueue(getContext());
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

        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/makeNewUser/"+uniqueID;

        Request submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);

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
        readNewDialogData();
    }



}

