package com.example.letterle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //------------------------------------------------------------------
    private String word = "";
    public String guessWord = "";
    private List<String> possibleWords;
    //------------------------------------------------------------------

    public int column;
    public int row;
    private ResultsDialog resultsDialog;
    private ConfirmationDialog confirmationDialog;
    public int difficulty;
    private int nextDifficulty;


    /*
        #############
        #  Getters  #
        #############
    */

    public TextView getTile(int x, int y) {
        int id = getResources().getIdentifier("textViewLetter_" + y + "" + x, "id", this.getPackageName());
        return findViewById(id);
    }

    public Button getKeyButton(char letter) {
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
        setContentView(R.layout.main_activity);
        resultsDialog = new ResultsDialog(this);
        confirmationDialog = new ConfirmationDialog(this);
        setupNewGame(5);
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

    public void onBtnEnter_Clicked(View caller) {
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
        resultsDialog.cancel();
        resetGame(false);
    }

    public void onBtnYes_Clicked(View caller) {
        confirmationDialog.cancel();
        difficulty = nextDifficulty;
        changeBoard(difficulty);
    }

    public void onBtnNo_Clicked(View caller) {
        confirmationDialog.cancel();

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickedDifficulty = Integer.parseInt(item.getTitle().toString());

        if (Arrays.asList(4, 5, 6).contains(clickedDifficulty)) {
            if (difficulty != clickedDifficulty) {
                if (row > 0) {
                    confirmationDialog.show();
                    nextDifficulty = clickedDifficulty;
                } else {
                    changeBoard(clickedDifficulty);
                }
            } else {
                sendToastMessage("Difficulty is already " + clickedDifficulty);
                System.out.println(difficulty + " == " + clickedDifficulty);
            }
            return true;

        } else {

            switch ((item.getItemId())) {

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
        }
        return super.onOptionsItemSelected(item);
    }



    /*
         ########################################
         #               Reset                  #
         ########################################
     */

    private void resetGame(boolean changedDifficulty) {
        if(!changedDifficulty) {
            resetColorBoard();
        }
        resetColorKeyBoard();
        row = 0;
        column = 0;
        guessWord = "";
    }

    public void resetColorBoard() {
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < difficulty; column++) {
                setBackground(getTile(column, row), R.drawable.lightgray_border);
                getTile(column, row).setText("");
                getTile(column, row).setTextColor(Color.BLACK);
            }
        }
    }

    public void resetColorKeyBoard() {
        for (char c = 'a'; c <= 'z'; c++) {
            getKeyButton(c).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.light_gray, null));
            getKeyButton(c).setTextColor(Color.BLACK);
            getTextKeyButton(c).setTextColor(Color.BLACK);
        }
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

    public void setupNewGame(int diff) {
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
        } else if (word.contains(Character.toString(guessWord.charAt(index)))) {
            setBackground(getTile(index, row), R.drawable.yellow_tile);
        } else {
            setBackground(getTile(index, row), R.drawable.gray_tile);
        }
       getTile(index, row).setTextColor(Color.WHITE);
    }

    /*
         ########################################
         #               Other                  #
         ########################################
     */


    public void showAnimation(String type, int row) {
        for (int i = 0; i < difficulty; i++) {
            Animation animation = null;
            switch (type) {
                case "error" -> animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.lefttoright);
                case "won" -> animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                case "revealletter" -> animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.revealletter);
            }
            getTile(i, row).startAnimation(animation);



        }
    }

    public void changeBoard(int difficulty){
        int layoutID = getResources().getIdentifier("board_" + difficulty, "layout", this.getPackageName());
        ConstraintLayout mainLayout = findViewById(R.id.includeBoard);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutID, null);
        mainLayout.removeAllViews();
        mainLayout.addView(layout);

        setupNewGame(difficulty);
        resetGame(true);
    }


    public void sendToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void wordGuessed() {
        sendToastMessage("Great");
        showAnimation("won", row);
        resultsDialog.show();
    }


}