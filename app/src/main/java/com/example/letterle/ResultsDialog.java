package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

public class ResultsDialog {

    Dialog dialog;
    Context context;

    public ResultsDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        initializeResultsDialog(context);
    }


    private void initializeResultsDialog(Context context) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.results);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
    }

    public void showResultsDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();;
            }
        }, 1000);
    }

}
