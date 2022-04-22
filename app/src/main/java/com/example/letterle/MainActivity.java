package com.example.letterle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class MainActivity extends AppCompatActivity {

    String word = "appel";
    int lettercounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public int onBtnEnter_Clicked(String wordEntered)
    {
        for(int count = 0; count < word.length(); count++) {
            String letter = String.valueOf(word.charAt(count));
            CharacterIterator iterator = new StringCharacterIterator(word);
            while (iterator.current() != CharacterIterator.DONE) {
                if (letter.equals(iterator.current())) {
                    TextView textViewNow = (TextView) findViewById(R.id.textViewLetter0); //moet natuurlijk altijd de juiste zijn (iterator.getindex() ipv gwn 1
                    textViewNow.setText(letter);
                    iterator.next();
                }
            }
        }





        return 1;
    }
}