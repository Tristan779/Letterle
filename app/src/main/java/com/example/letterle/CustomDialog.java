package com.example.letterle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class CustomDialog {

    android.app.Dialog dialog;
    Context context;

    public CustomDialog(Context context, int activity) {
        this.context = context;
        dialog = new android.app.Dialog(context);
        initializeDialog(activity);
    }


    private void initializeDialog(int activity) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
    }

    public void showDialog() {
        dialog.show();
    }

}

