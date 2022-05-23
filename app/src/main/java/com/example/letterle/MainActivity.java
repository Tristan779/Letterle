package com.example.letterle;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //------------------------------------------------------------------
    private String word = "";
    public String guessWord = "";
    private final List<String> possibleWords4 = new ArrayList<>();
    private final List<String> possibleWords5 = new ArrayList<>();
    private final List<String> possibleWords6 = new ArrayList<>();
    private List<String> possibleWords = new ArrayList<>();
    //------------------------------------------------------------------

    public int column;
    public int row;
    private ResultsDialog resultsDialog;
    private ConfirmationDialog confirmationDialog;
    public int difficulty;
    private int nextDifficulty;
    public int a;
    public int tries;


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
        resultsDialog.readNewDialogData();
        confirmationDialog = new ConfirmationDialog(this);
        a = 0;
        addPossibleWords("https://studev.groept.be/api/a21pt203/getFiveList", "FiveLetter", possibleWords5);
        addPossibleWords("https://studev.groept.be/api/a21pt203/getSixList","SixLetter", possibleWords6);
        addPossibleWords("https://studev.groept.be/api/a21pt203/getFourList","FourLetter", possibleWords4);

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
            System.out.println(tries);
            if (possibleWords.contains(guessWord)) {
                tries++;
                for (int index = 0; index < difficulty; index++) {
                    setColorKey(index, getColorKey(index));
                    setColorTile(index);
                    showAnimation("revealletter", row);
                }
                if (guessWord.equals(word)) {
                    gameWon();
                }

                else {
                    row++;
                    column = 0;
                    guessWord = "";
                }


                if (tries == 6) {
                    gameLost();
                }
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
        switchKeyBoardWithPlayAgain(0);
        resetGame(false);
        setupNewGame(difficulty);
    }

    public void onBtnClose_Clicked(View caller) {
        resultsDialog.cancel();
    }

    public void onBtnPlayAgainResults_Clicked(View caller) {
        resultsDialog.cancel();
        resetGame(false);
        setupNewGame(difficulty);
    }

    public void onBtnYes_Clicked(View caller) {
        confirmationDialog.cancel();
        gameForciblyLost();
    }

    public void onBtnNo_Clicked(View caller) {
        confirmationDialog.cancel();

    }

    public void onBtnCheckBoard_Clicked(View caller) {
        resultsDialog.cancel();
        switchKeyBoardWithPlayAgain(1);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (Arrays.asList("4", "5", "6").contains(item.getTitle())) {
            int clickedDifficulty = Integer.parseInt(item.getTitle().toString());
            if (difficulty != clickedDifficulty) {
                if (row > 0) {
                    confirmationDialog.show();
                    nextDifficulty = clickedDifficulty;
                } else {
                    changeBoard(clickedDifficulty);
                }
            } else {
                sendToastMessage("Difficulty is already " + clickedDifficulty);
            }
            return true;

        } else {
            resultsDialog.readNewDialogData();
            resultsDialog.show();
            resultsDialog.setContentView(R.layout.results);
            resultsDialog.setStatTiles();
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
        tries = 0;
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

    public void setupNewGame(int diff) {
        Random rn = new Random();
        switch (diff) {
            case 4 -> {
                difficulty = 4;
                possibleWords = possibleWords4;
            }

            case 5 -> {
                difficulty = 5;
                possibleWords = possibleWords5;
            }
            case 6 -> {
                difficulty = 6;
                possibleWords = possibleWords6;
            }

        }
        switchKeyBoardWithPlayAgain(0);
        int num = rn.nextInt(possibleWords.size());
        word = possibleWords.get(num);
        System.out.println(word);

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
         #              Database                #
         ########################################
     */

    public void addPossibleWords(String link, String column, List<String> possibleWords)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, link, null,
                response -> {
                    try {
                        String responseString;
                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );
                            responseString = curObject.getString( column );
                            possibleWords.add(responseString);
                        }
                        a++;
                        if(a==3) {
                            setupNewGame(5);
                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

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

    public void switchKeyBoardWithPlayAgain(int index){
        ViewFlipper vf = findViewById(R.id.vf);
        vf.setDisplayedChild(index);

        if(index == 1){
            TextView correctWord = findViewById(R.id.textViewCorrectWord);
            correctWord.setText(word.toUpperCase());
        }
    }


    public void sendToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }



    public void gameForciblyLost() {
        difficulty = nextDifficulty;
        changeBoard(difficulty);
        resultsDialog.updateDialog(false, 0);
    }

    public void gameWon() {
        sendToastMessage("Great");
        resultsDialog.updateDialog(true, tries);
        resultsDialog.readNewDialogData();
        resultsDialog.show();
        resultsDialog.setContentView(R.layout.game_end_results);
        resultsDialog.setStatTiles();
    }

    public void gameLost() {
        sendToastMessage("No more tries, you lost :(");
        resultsDialog.updateDialog(false, 0);
        resultsDialog.readNewDialogData();
        resultsDialog.show();
        resultsDialog.setContentView(R.layout.game_end_results);
        resultsDialog.setStatTiles();
    }

}