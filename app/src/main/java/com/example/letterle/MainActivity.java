package com.example.letterle;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String word = "appel";
    private List<String> possibleWords;
    private int column;
    private int row;
    private String guessWord = "";

    public TextView getTextView(int x, int y) {
        Resources res = getResources();
        int id = res.getIdentifier("textViewLetter_"+y+""+x, "id", this.getPackageName());
        return findViewById(id);
    }

    public TextView getButton(String letter) {
        Resources res = getResources();
        int id = getResources().getIdentifier("button"+letter.toUpperCase(), "id", this.getPackageName());
        return findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        possibleWords = Arrays.asList("anker", "kwaad", "speld", "steel", "loper", "plaat", "appel", "lappl");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }


    public void onBtnLetter_Clicked(View caller)
    {
        if (0 <= column && column < 5 ){
            Button keyButton = findViewById(caller.getId());
            String buttonChar = keyButton.getText().toString();
            TextView textViewNow = getTextView(column, row);
            textViewNow.setText(buttonChar);
            textViewNow.setBackground(getResources().getDrawable(R.drawable.darkgray_border, null));
            guessWord = guessWord + buttonChar.toLowerCase();
            column++;
        }
    }

    public void onBtnDelete_Clicked(View caller)
    {
        if (0 < column && column <= 5){
            column--;
            TextView textViewNow = getTextView(column, row);
            textViewNow.setText("");
            textViewNow.setBackground(getResources().getDrawable(R.drawable.lightgray_border, null));
            guessWord = guessWord.substring(0, guessWord.length()-1).toLowerCase();
        }
    }

    public void setColorLetter(int index, int color){
        getTextView(index, row).setBackgroundColor(color);
        getTextView(index, row).setTextColor(Color.WHITE);
        getButton(Character.toString(guessWord.charAt(index))).setBackgroundColor(color);
        getButton(Character.toString(guessWord.charAt(index))).setTextColor(Color.WHITE);
    }

    public void showAnimation(String type) {
        for (int i = 0; i < 5; i++) {

            Animation animation = null;
            if(type == "error") {
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.lefttoright);
            }
            else if(type == "won") {
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
            }
            else if(type == "revealletter") {
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.revealletter);
            }
            getTextView(i, row).startAnimation(animation);
        }
    }

    public void sendToastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public int getColorLetter(int index){
        int color;

        if (word.charAt(index) == guessWord.charAt(index)) {
            color = Color.parseColor("#6aaa64");
        }
        else if (word.contains(Character.toString(guessWord.charAt(index)))){
            color = Color.parseColor("#c8b558");
        }
        else{
            color = Color.parseColor("#787c7e");
        }
        return color;
    }




    public void onBtnEnter_Clicked(View caller)
    {
        if(guessWord.length() == 5) {

            System.out.println(guessWord + " = " + word);
            if(guessWord.equals(word)){
                wordGuessed();
            }

            if (possibleWords.contains(guessWord)) {
                for (int i = 0; i < 5; i++) {
                    setColorLetter(i, getColorLetter(i));
                    showAnimation("revealletter");
                }
                row++;
                column = 0;
                guessWord = "";
            }
            else {
                sendToastMessage("Not in word list");
                showAnimation("error");
            }
        }
        else {
            sendToastMessage("Not enough letters");
            showAnimation("error");
        }
    }

    public void wordGuessed() {
        sendToastMessage("Great");
        showAnimation("won");
        showDialog();

    }

    public void showDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.results);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}