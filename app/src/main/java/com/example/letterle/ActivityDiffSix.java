package com.example.letterle;

import android.os.Bundle;


public class ActivityDiffSix extends MainActivityDiffFive {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_six);
        setupGame(6);
    }
}