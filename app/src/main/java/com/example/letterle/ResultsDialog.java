package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ResultsDialog extends Dialog{

    private final Context context;
    private final Database db;


    public ResultsDialog(Context context) {
        super(context);
        this.context = context;
        db = new Database(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceStace){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    public Database getDb() {
        return db;
    }

    public ProgressBar getProgressBar(int index) {
        int id = context.getResources().getIdentifier("progressBar_" + index, "id", context.getPackageName());
        return findViewById(id);
    }



    public void setStatDialog() {

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

        maxStreak.setText(String.valueOf(db.getMaxStreak()));
        currentStreak.setText(String.valueOf(db.getCurrentStreak()));

        winOne.setText(String.valueOf(db.getWins1Try()));
        winTwo.setText(String.valueOf(db.getWins2Try()));
        winTree.setText(String.valueOf(db.getWins3Try()));
        winFour.setText(String.valueOf(db.getWins4Try()));
        winFive.setText(String.valueOf(db.getWins5Try()));
        winSix.setText(String.valueOf(db.getWins6Try()));

        float wins = db.getWins1Try() + db.getWins2Try() + db.getWins3Try() +db.getWins4Try() + db.getWins5Try() + db.getWins6Try();
        if(db.getGamesPlayed() == 0)
        {
            win.setText("0");
        }
        else{
            float winPercent = wins/db.getGamesPlayed() * 100;
            win.setText(String.valueOf((int)winPercent));
        }
        played.setText(String.valueOf(db.getGamesPlayed()));

        List<Integer> list = new ArrayList<>(
                Arrays.asList(db.getWins1Try(), db.getWins2Try(), db.getWins3Try(), db.getWins4Try(), db.getWins5Try(), db.getWins6Try()));

        for(int i = 1; i < 7; i++){
            float fill = (float) list.get(i-1)/ (float) Collections.max(list);
            getProgressBar(i).setProgress(Math.round(100 * fill));

        }
    }





}

