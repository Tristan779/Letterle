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
    String name;
    int id = 2;
    int Wins1try;
    int Wins2try;
    int Wins3try;
    int Wins4try;
    int Wins5try;
    int Wins6try;
    int CurrentStreak;
    int MaxStreak;
    int GamesPlayed;
    ArrayList<String> Names = new ArrayList<String>();
    ArrayList<Integer> Ids = new ArrayList<Integer>();


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



    public ProgressBar getProgressBar(int index) {
        int id = context.getResources().getIdentifier("progressBar_" + index, "id", context.getPackageName());
        return findViewById(id);
    }



    public void readNewDialogData()
    {

        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/getStats";

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        for( int i = 0; i < responseArray.length(); i++ )
                        {
                            JSONObject curObject = responseArray.getJSONObject( i );
                            int ThisID = curObject.getInt("id"); //primary key
                            if(id == ThisID)
                            {
                                Wins1try = curObject.getInt("Wins1try");
                                Wins2try = curObject.getInt("Wins2try");
                                Wins3try = curObject.getInt("Wins3try");
                                Wins4try = curObject.getInt("Wins4try");
                                Wins5try = curObject.getInt("Wins5try");
                                Wins6try = curObject.getInt("Wins6try");
                                CurrentStreak = curObject.getInt("CurrStreak");
                                MaxStreak = curObject.getInt("MaxStreak");
                                GamesPlayed = curObject.getInt("Played");
                                //System.out.println("-----------------------------------");
                                //System.out.println("1try: "+Wins1try+" 5try: "+Wins5try+" curr: "+CurrentStreak+" -----"+id+ThisID);
                            }
                        }



                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> {
                    ;
                }
        );

        requestQueue.add(submitRequest);
    }

    public void setStatTiles() {
        TextView maxStreak = findViewById(R.id.textViewMaxStreak_nr);
        TextView currentStreak = findViewById(R.id.textViewCurrentStreak_nr);
        TextView win = findViewById(R.id.textViewWin_nr);
        TextView played = findViewById(R.id.textViewPlayed_nr);
        maxStreak.setText(String.valueOf(MaxStreak));
        currentStreak.setText(String.valueOf(CurrentStreak));
        float wins = Wins1try+Wins2try+Wins3try+Wins4try+Wins5try+Wins6try;
        //System.out.println(Wins1try+" "+Wins2try+" "+Wins3try+" "+Wins4try+" "+Wins5try+" " + Wins6try);
        System.out.println("Wins: "+wins);
        if(GamesPlayed==0)
        {
            win.setText(String.valueOf(0));
        }
        else{
            float winPercent = wins/GamesPlayed*100;
            win.setText(String.valueOf((int)winPercent));
        }
        played.setText(String.valueOf(GamesPlayed));

        //test lijst
        List<Integer> list = new ArrayList<>(
                Arrays.asList(Wins1try, Wins2try, Wins3try, Wins4try, Wins5try, Wins6try));

        for(int i = 1; i < 7; i++){
            float fill = (float) list.get(i-1)/ (float) Collections.max(list);
            getProgressBar(i).setProgress(Math.round(100 * fill));

        }
    }

    public void updateDialog(boolean won, int tries)
    {
        GamesPlayed++;
        if(won)
        {
            CurrentStreak++;
            if (CurrentStreak>MaxStreak)
            {
                MaxStreak++;
            }
            if(tries ==1)
                Wins1try++;//op een propere manier dit prgrammeren voor alle tries!!
            if(tries ==2)
                Wins2try++;
            if(tries ==3)
                Wins3try++;
            if(tries ==4)
                Wins4try++;
            if(tries ==5)
                Wins5try++;
            if(tries ==6)
                Wins6try++;

        }
        if(!won)
        {
            CurrentStreak=0;
        }
        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/updateStatData/"+Wins1try+"/"
                +Wins2try+"/"+Wins3try+"/"+Wins4try+"/"+Wins5try+"/"+Wins6try+"/"+CurrentStreak+"/"+MaxStreak+"/"+GamesPlayed
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
                    ;
                }
        );

        requestQueue.add(submitRequest);
    }


    public void makeNewAccount(String Nm)
    {

        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/makeNewUser/"+Nm;
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
                    ;
                }
        );

        requestQueue.add(submitRequest);
    }

    public void getNameAndId(String Nm)
    {

        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = "https://studev.groept.be/api/a21pt203/getNameAndIds";

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        for( int i = 0; i < responseArray.length(); i++ )
                        {
                            JSONObject curObject = responseArray.getJSONObject( i );
                            Names.add(curObject.getString("name"));
                            Ids.add(curObject.getInt("id")); //primary key

                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },
                error -> {
                    ;
                }
        );

        requestQueue.add(submitRequest);
    }

}

