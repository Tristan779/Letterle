package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class ResultsDialog extends Dialog{

    public Context context;
    private RequestQueue requestQueue;

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
        setContentView(R.layout.results);
        updateDialog();
    }



    public ProgressBar getProgressBar(int index) {
        int id = context.getResources().getIdentifier("progressBar_" + index, "id", context.getPackageName());
        return findViewById(id);
    }



    public void updateDialog() {

        TextView maxStreak = findViewById(R.id.textViewMaxStreak_nr);
        TextView currentStreak = findViewById(R.id.textViewCurrentStreak_nr);
        TextView win = findViewById(R.id.textViewWin_nr);
        TextView played = findViewById(R.id.textViewPlayed_nr);

        maxStreak.setText("10");
        currentStreak.setText("2");
        win.setText("4");
        played.setText("30");

        //test lijst
        List<Integer> list = new ArrayList<>(
                Arrays.asList(1, 3, 5, 9, 4, 8));

        for(int i = 1; i < 7; i++){
            float fill = (float) list.get(i-1)/ (float) Collections.max(list);
            getProgressBar(i).setProgress(Math.round(100 * fill));

        }

    }

}

