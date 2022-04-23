package com.example.letterle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    String word = "appel";
    int lettercounter;
    ArrayList<String> possibleWords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Collections.addAll(possibleWords, "anker", "kwaad", "speld", "steel", "loper");
    }

    public void onBtnLetter_Clicked(View caller)
    {
        Button letterButton = (Button) findViewById(R.id.buttonA);
        String buttonText = letterButton.getText().toString();
        TextView textViewNow = (TextView) findViewById(R.id.textViewLetter0);
        textViewNow.setText(buttonText);
    }


    public int onBtnEnter_Clicked(View caller)
    {
        for(int count = 0; count < word.length(); count++) {
            String letter = String.valueOf(word.charAt(count));
            CharacterIterator iterator = new StringCharacterIterator(word);
            while (iterator.current() != CharacterIterator.DONE) {
                if (letter.equals(iterator.current())) {
                    TextView textViewNow = (TextView) findViewById(R.id.textViewLetter0); //moet natuurlijk altijd de juiste zijn (iterator.getindex() ipv gwn 1
                    iterator.next();
                }
            }
        }





        return 1;
    }
}