package com.example.letterle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivityDiffFive extends AppCompatActivity {

    //------------------------------------------------------------------
    private String word = "";
    public String guessWord = "";
    private List<String> possibleWords;
    //------------------------------------------------------------------

    public int column;
    public int row;
    private ResultsDialog resultsDialog;
    public int difficulty;


    /*
        #############
        #  Getters  #
        #############
    */

    public TextView getTextView(int x, int y) {
        Resources res = getResources();
        int id = res.getIdentifier("textViewLetter_"+y+""+x, "id", this.getPackageName());
        return findViewById(id);
    }

    public TextView getButton(char letter) {
        Resources res = getResources();
        int id = getResources().getIdentifier("button_"+Character.toUpperCase(letter), "id", this.getPackageName());
        System.out.println(id);
        return findViewById(id);
    }

    public TextView getTextViewButton(char letter) {
        Resources res = getResources();
        int id = getResources().getIdentifier("textViewButton_"+Character.toUpperCase(letter), "id", this.getPackageName());
        System.out.println(id);
        return findViewById(id);
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

    /*
        ##################
        #   Overrides    #
        ##################
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_five_main);
        resultsDialog = new ResultsDialog(this);
        setupGame(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    /*
         ########################################
         #   Letter, Delete, Enter, PlayAgain   #
         ########################################
     */

    public void onBtnLetter_Clicked(View caller)
    {
        if (0 <= column && column < difficulty ){
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
        if (0 < column && column <= difficulty){
            column--;
            TextView textViewNow = getTextView(column, row);
            textViewNow.setText("");
            textViewNow.setBackground(getResources().getDrawable(R.drawable.lightgray_border, null));
            guessWord = guessWord.substring(0, guessWord.length()-1).toLowerCase();
        }
    }

    public void onBtnEnter_Clicked(View caller)
    {
        if(guessWord.length() == difficulty) {

            if(guessWord.equals(word)){
                wordGuessed();
            }

            if (possibleWords.contains(guessWord)) {
                for (int i = 0; i < difficulty; i++) {
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

    public void onBtnPlayAgain_Clicked(View caller) {
        resultsDialog.dialog.cancel();
        resetGame();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( (item.getItemId())){
            case (R.id.fourSquare):
                if(difficulty != 4)
                {
                    Intent intent = new Intent(this, ActivityDiffFour.class);
                    startActivity(intent);
                }
                return true;
            case (R.id.fiveSquare):

                if(difficulty != 5){
                    Intent intent = new Intent(this, MainActivityDiffFive.class);
                    startActivity(intent);
                }
                return true;
            case (R.id.sixSquare):
                if(difficulty != 6)
                {
                    Intent intent = new Intent(this, ActivityDiffSix.class);
                    startActivity(intent);}
                return true;
            case (R.id.colourOptionVintage):
                setColourOption(2);
                return true;
            case (R.id.colourOptionDark):
                setColourOption(1);
                return true;
            case (R.id.colourOptionOriginal):
                setColourOption(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
         ########################################
         #               Reset                  #
         ########################################
     */

    private void resetGame() {
        for (int row = 0; row < 6; row++){
            for (int column = 0; column < difficulty; column++){
                resetColorBoard(column, row);

            }
        }

        for (char c = 'a'; c <= 'z'; c++){
            resetColorKeyBoard(c);

        }
        row = 0;
        column = 0;
    }

    public void resetColorBoard(int x, int y){
        getTextView(x, y).setBackground(getResources().getDrawable(R.drawable.lightgray_border, null));;
        getTextView(x, y).setText("");
        getTextView(x, y).setTextColor(Color.BLACK);
    }

    public void resetColorKeyBoard(char c){
        getButton(c).setBackgroundColor(getResources().getColor(R.color.light_gray));
        getButton(c).setTextColor(Color.BLACK);
        getTextViewButton(c).setTextColor(Color.BLACK);
    }

    /*
         ########################################
         #                 Set                  #
         ########################################
     */

    public void setColorLetter(int index, int color){
        getTextView(index, row).setBackgroundColor(color);
        getTextView(index, row).setTextColor(Color.WHITE);
        getButton(guessWord.charAt(index)).setBackgroundColor(color);
        getButton(guessWord.charAt(index)).setTextColor(Color.WHITE);
        getTextViewButton(guessWord.charAt(index)).setTextColor(Color.WHITE);
    }

    public void setColourOption(int option)
    {
        //vintage = 2, dark = 1, original = 0
    }

    public void setupGame(int diff){
        if(diff == 4){
            word = "kaka";
            possibleWords = Arrays.asList("tril", "kunt", "ramp", "pomp", "kort", "kaka");
            difficulty = 4;
        }
        else if(diff == 5){
            word = "appel";
            possibleWords = Arrays.asList("anker", "kwaad", "speld", "steel", "loper", "plaat", "appel", "lappl", "aaaaa");
            difficulty = 5;
        }
        else if(diff == 6){
            word = "zwavel";
            possibleWords = Arrays.asList("ronder", "zwiert", "wolken", "imkers", "zwavel");
            difficulty = 6;
        }
    }

    /*
         ########################################
         #               Other                  #
         ########################################
     */

    public void showAnimation(String type) {
        for (int i = 0; i < difficulty; i++) {



            Animation animation = null;
            if(type.equals("error")) {
                animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.lefttoright);
            }
            else if(type.equals("won")) {
                animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.bounce);
            }
            else if(type.equals("revealletter")) {
                animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.revealletter);
            }
            getTextView(i, row).startAnimation(animation);
        }
    }

    public void sendToastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void wordGuessed() {
        sendToastMessage("Great");
        showAnimation("won");
        resultsDialog.showResultsDialog();
    }

}