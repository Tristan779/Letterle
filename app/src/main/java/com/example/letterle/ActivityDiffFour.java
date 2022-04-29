package com.example.letterle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Arrays;
import java.util.List;

public class ActivityDiffFour extends MainActivityDiffFive {

    private String wordFour = "kaka";
    private List<String> possibleWordsDiffFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_four);
        possibleWordsDiffFour = Arrays.asList("tril", "kunt", "ramp", "pomp", "kort", "kaka");
        difficulty = 4;
    }

    @Override
    public int getColorLetter(int index)
    {
        int color;

        if (wordFour.charAt(index) == guessWord.charAt(index)) {
            color = Color.parseColor("#6aaa64");
        }
        else if (wordFour.contains(Character.toString(guessWord.charAt(index)))){
            color = Color.parseColor("#c8b558");
        }
        else{
            color = Color.parseColor("#787c7e");
        }
        return color;
    }

    @Override
    public void onBtnEnter_Clicked(View caller) {
        if (guessWord.length() == difficulty) {

            System.out.println(guessWord + " = " + wordFour);
            if (guessWord.equals(wordFour)) {
                wordGuessed();
            }

            if (possibleWordsDiffFour.contains(guessWord)) {
                for (int i = 0; i < difficulty; i++) {
                    setColorLetter(i, getColorLetter(i));
                    showAnimation("revealletter");
                }
                row++;
                column = 0;
                guessWord = "";
            } else {
                sendToastMessage("Not in word list");
                showAnimation("error");
            }
        } else {
            sendToastMessage("Not enough letters");
            showAnimation("error");
        }


    }
}

