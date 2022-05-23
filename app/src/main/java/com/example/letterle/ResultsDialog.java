package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultsDialog extends Dialog{

    public Context context;
    private RequestQueue requestQueue;
    int id = 1;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceStace){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.game_end_results);
        readNewDialogData();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public ProgressBar getProgressBar(int index) {
        int id = context.getResources().getIdentifier("progressBar_" + index, "id", context.getPackageName());
        return findViewById(id);
    }


    public void readNewDialogData() {
        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/getStats";

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        for( int i = 0; i < responseArray.length(); i++ )
                        {
                            JSONObject curObject = responseArray.getJSONObject( i );
                            int userID = curObject.getInt("id"); //primary key
                            if(id == userID)
                            {
                                wins1Try = curObject.getInt("Wins1try");
                                wins2Try = curObject.getInt("Wins2try");
                                wins3Try = curObject.getInt("Wins3try");
                                wins4Try = curObject.getInt("Wins4try");
                                wins5Try = curObject.getInt("Wins5try");
                                wins6Try = curObject.getInt("Wins6try");
                                currentStreak = curObject.getInt("CurrStreak");
                                maxStreak = curObject.getInt("MaxStreak");
                                gamesPlayed = curObject.getInt("Played");
                                //System.out.println("-----------------------------------");
                                //System.out.println("1try: "+Wins1try+" 5try: "+Wins5try+" curr: "+CurrentStreak+" -----"+id+userID);
                            }
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

        maxStreak.setText(String.valueOf(this.maxStreak));
        currentStreak.setText(String.valueOf(this.currentStreak));
        float wins = wins1Try + wins2Try + wins3Try + wins4Try + wins5Try + wins6Try;
        System.out.println("Wins: "+wins);
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
                +"/"+id;
        //String requestURL = "https://studev.groept.be/api/a21pt203/updateStatData/1/2/3/4/5/6/3/3/3/3";

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

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
    }



}

