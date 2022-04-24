package com.example.letterle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String word = "appel";
    List<String> possibleWords;
    int column;
    int row;
    String guessWord = "";

    public TextView getTextView(int x, int y) {
        Resources res = getResources();
        int id = res.getIdentifier("textViewLetter_"+y+""+x, "id", this.getPackageName());
        return findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        possibleWords = Arrays.asList("anker", "kwaad", "speld", "steel", "loper", "plaat", "appel");
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
        System.out.println(guessWord);
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


    public void onBtnEnter_Clicked(View caller)
    {
        if(possibleWords.contains(guessWord)){
            for (int i = 0; i < 5; i++) {
                if(word.charAt(i) == guessWord.charAt(i)){
                    getTextView(i, row).setBackgroundColor(Color.parseColor("#6aaa64"));
                }
                else if(word.contains(Character.toString(guessWord.charAt(i)))){
                    getTextView(i, row).setBackgroundColor(Color.parseColor("#c8b558"));
                }
                else{
                    getTextView(i, row).setBackgroundColor(Color.parseColor("#787c7e"));
                }
                getTextView(i, row).setTextColor(Color.WHITE);
            }


            row++;
            column = 0;
            guessWord = "";
        }else{
            Toast.makeText(getApplicationContext(),"Not in word list",Toast.LENGTH_LONG).show();
            //Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.lefttoright);
            //getTextView(0, 0).startAnimation(animation);
        }
    }
}