package com.example.letterle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import android.app.Dialog;

import android.util.Log;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountMenu extends Dialog{

    public Context context;
    ArrayList<String> Names = new ArrayList<String>();
    ArrayList<Integer> Ids = new ArrayList<Integer>();
    private RequestQueue requestQueue;

    public AccountMenu(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceStace){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.account_menu);
        getNameAndIdFromDB();
    }



    public void getNameAndIdFromDB()
    {
        Names.clear();
        Ids.clear();
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
                }
        );

        requestQueue.add(submitRequest);

    }

    public int getIdFromName(String name)
    {
        if(Names.contains(name))
        {
            return(Ids.get(Names.indexOf(name)));
        }
        return 0;
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
                }
        );

        requestQueue.add(submitRequest);
    }




}
