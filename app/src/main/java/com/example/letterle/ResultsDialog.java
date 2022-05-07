package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;


public class ResultsDialog extends Dialog{

    public Context context;

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



    public TextView getTest(int index) {
        int id = context.getResources().getIdentifier("textViewBar_" + 1, "id", context.getPackageName());
        return findViewById(id);
    }


    private void updateDialog() {

        TextView maxStreak = findViewById(R.id.textViewMaxStreak_nr);
        TextView currentStreak = findViewById(R.id.textViewCurrentStreak_nr);
        TextView win = findViewById(R.id.textViewWin_nr);
        TextView played = findViewById(R.id.textViewPlayed_nr);

        maxStreak.setText("10");
        currentStreak.setText("2");
        win.setText("4");
        played.setText("30");

        getTest(1).setText("HALL0");

    }

}

