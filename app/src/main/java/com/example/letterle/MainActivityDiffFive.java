package com.example.letterle;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
    private CustomDialog resultsDialog;
    private CustomDialog confirmationDialog;
    private Intent nextIntent;
    public int difficulty;


    /*
        #############
        #  Getters  #
        #############
    */

    public TextView getTile(int x, int y) {
        int id = getResources().getIdentifier("textViewLetter_" + y + "" + x, "id", this.getPackageName());
        return findViewById(id);
    }

    public TextView getKeyButton(char letter) {
        int id = getResources().getIdentifier("button_" + Character.toUpperCase(letter), "id", this.getPackageName());
        return findViewById(id);
    }

    public TextView getTextKeyButton(char letter) {
        int id = getResources().getIdentifier("textViewButton_" + Character.toUpperCase(letter), "id", this.getPackageName());
        return findViewById(id);
    }


    public int getColorKey(int index) {
        int color;

        if (word.charAt(index) == guessWord.charAt(index)) {
            color = ResourcesCompat.getColor(getResources(), R.color.green, null);
        } else if (word.contains(Character.toString(guessWord.charAt(index)))) {
            color = ResourcesCompat.getColor(getResources(), R.color.yellow, null);
        } else {
            color = ResourcesCompat.getColor(getResources(), R.color.dark_gray, null);
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
        resultsDialog = new CustomDialog(this, R.layout.results);
        confirmationDialog = new CustomDialog(this, R.layout.confirmation);
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

    public void onBtnKey_Clicked(View caller) {
        if (0 <= column && column < difficulty) {
            Button keyButton = findViewById(caller.getId());
            String buttonChar = keyButton.getText().toString();
            TextView textViewNow = getTile(column, row);
            textViewNow.setText(buttonChar);
            setBackground(textViewNow, R.drawable.darkgray_border);
            guessWord = guessWord + buttonChar.toLowerCase();
            column++;

        }
    }


    public void onBtnDelete_Clicked(View caller) {
        if (0 < column && column <= difficulty) {
            column--;
            TextView textViewNow = getTile(column, row);
            textViewNow.setText("");
            setBackground(textViewNow, R.drawable.lightgray_border);
            guessWord = guessWord.substring(0, guessWord.length() - 1).toLowerCase();
        }
    }

    /*
    public void revealLetters(){
        int color;
        boolean[] tiles = new boolean[difficulty];
        List<Character> map = new ArrayList();

        for (int index = 0; index < difficulty; index++) {
            if (word.charAt(index) == guessWord.charAt(index)) {
                color = Color.parseColor("#6aaa64");
                setColorLetter(index, color);
                tiles[index] = true;
                map.add(word.charAt(index));
            }
        }

        for (int index = 0; index < difficulty; index++) {
            if (!tiles[index] && word.charAt(index) == guessWord.charAt(index)) {
                if (!map.contains(word.charAt(index))){
                    color = Color.parseColor("#c8b558");
                    setColorLetter(index, color);
                }
            }
        }

        for (int index = 0; index < difficulty; index++) {
            if (!tiles[index]) {
                if (!map.contains(word.charAt(index))) {
                    color = Color.parseColor("#787c7e");
                    setColorLetter(index, color);
                }
            }
        }
    }

     */

    public void onBtnEnter_Clicked(View caller) {
        String type;
        if (guessWord.length() == difficulty) {

            if (guessWord.equals(word)) {
                wordGuessed();
                showAnimation("won", row);

            }

            if (possibleWords.contains(guessWord)) {
                for (int index = 0; index < difficulty; index++) {
                    setColorKey(index, getColorKey(index));
                    setColorTile(index);
                }
                showAnimation("revealletter", row);
                row++;
                column = 0;
                guessWord = "";
            } else {
                sendToastMessage("Not in word list");
                showAnimation("error", row);
            }
        } else {
            sendToastMessage("Not enough letters");
            showAnimation("error", row);
        }
    }

    public void onBtnPlayAgain_Clicked(View caller) {
        resultsDialog.dialog.cancel();
        resetGame();
    }

    public void onBtnYes_Clicked(View caller) {
        confirmationDialog.dialog.cancel();
        startActivity(nextIntent);
    }

    public void onBtnNo_Clicked(View caller) {
        confirmationDialog.dialog.cancel();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())) {
            case (R.id.fourSquare) -> {
                if (difficulty != 4) {
                    nextIntent = new Intent(this, ActivityDiffFour.class);
                    if(row > 0){
                        confirmationDialog.showDialog();
                    } else {
                        startActivity(nextIntent);
                    }
                }
                return true;
            }
            case (R.id.fiveSquare) -> {
                if (difficulty != 5) {
                    nextIntent = new Intent(this, MainActivityDiffFive.class);
                    if(row > 0){
                        confirmationDialog.showDialog();
                    } else {
                        startActivity(nextIntent);
                    }
                }
                return true;
            }
            case (R.id.sixSquare) -> {
                if (difficulty != 6) {
                    nextIntent = new Intent(this, ActivityDiffSix.class);
                    if(row > 0){
                        confirmationDialog.showDialog();
                    } else {
                        startActivity(nextIntent);
                    }
                }
                return true;
            }
            case (R.id.colourOptionVintage) -> {
                setColourOption(2);
                return true;
            }
            case (R.id.colourOptionDark) -> {
                setColourOption(1);
                return true;
            }
            case (R.id.colourOptionOriginal) -> {
                setColourOption(0);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    /*
         ########################################
         #               Reset                  #
         ########################################
     */

    private void resetGame() {
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < difficulty; column++) {
                resetColorBoard(column, row);

            }
        }

        for (char c = 'a'; c <= 'z'; c++) {
            resetColorKeyBoard(c);

        }
        row = 0;
        column = 0;
    }

    public void resetColorBoard(int x, int y) {
        setBackground(getTile(x, y), R.drawable.lightgray_border);
        getTile(x, y).setText("");
        getTile(x, y).setTextColor(Color.BLACK);
    }

    public void resetColorKeyBoard(char c) {
        getKeyButton(c).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.light_gray, null));
        getKeyButton(c).setTextColor(Color.BLACK);
        getTextKeyButton(c).setTextColor(Color.BLACK);
    }

    /*
         ########################################
         #                 Set                  #
         ########################################
     */

    public void setColorKey(int index, int color) {
        getKeyButton(guessWord.charAt(index)).setBackgroundColor(color);
        getKeyButton(guessWord.charAt(index)).setTextColor(Color.WHITE);
        getTextKeyButton(guessWord.charAt(index)).setTextColor(Color.WHITE);
    }

    public void setColourOption(int option) {
        //vintage = 2, dark = 1, original = 0
    }

    public void setupGame(int diff) {
        switch (diff) {
            case 4 -> {
                word = "kaka";
                possibleWords = Arrays.asList("tril", "kunt", "ramp", "pomp", "kort", "kaka");
                difficulty = 4;
            }

            case 5 -> {
                word = "appel";
                possibleWords = Arrays.asList("anker", "kwaad", "speld", "steel", "loper", "plaat", "appel", "lappl", "aaaaa");
                difficulty = 5;
            }
            case 6 -> {
                word = "zwavel";
                possibleWords = Arrays.asList("ronder", "zwiert", "wolken", "imkers", "zwavel");
                difficulty = 6;
            }
        }
    }

    public void setBackground(TextView view, int drawable) {
        view.setBackground(ResourcesCompat.getDrawable(getResources(), drawable, null));
    }

    public void setColorTile(int index) {
        if (word.charAt(index) == guessWord.charAt(index)) {
            setBackground(getTile(index, row), R.drawable.green_tile);
            getTile(index, row).setTextColor(Color.WHITE);
        } else if (word.contains(Character.toString(guessWord.charAt(index)))) {
            setBackground(getTile(index, row), R.drawable.yellow_tile);
            getTile(index, row).setTextColor(Color.WHITE);
        } else {
            setBackground(getTile(index, row), R.drawable.gray_tile);
            getTile(index, row).setTextColor(Color.WHITE);
        }
    }

    /*
         ########################################
         #               Other                  #
         ########################################
     */


    public void showAnimation(String type, int row) {
        for (int i = 0; i < difficulty; i++) {
            System.out.println("o");
            Animation animation = null;
            switch (type) {
                case "error" -> animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.lefttoright);
                case "won" -> animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.bounce);
                case "revealletter" -> animation = AnimationUtils.loadAnimation(MainActivityDiffFive.this, R.anim.revealletter);
            }
            getTile(i, row).startAnimation(animation);



        }
    }


    public void sendToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void wordGuessed() {
        sendToastMessage("Great");
        showAnimation("won", row);
        resultsDialog.showDialog();
    }


}