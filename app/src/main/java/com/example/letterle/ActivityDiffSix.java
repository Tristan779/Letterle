package com.example.letterle;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class ActivityDiffSix extends MainActivityDiffFive {

    private String wordSix = "zwavel";
    private List<String> possibleWordsDiffSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_six);
        possibleWordsDiffSix = Arrays.asList("ronder", "zwiert", "wolken", "imkers", "zwavel");
        difficulty = 6;
    }
}