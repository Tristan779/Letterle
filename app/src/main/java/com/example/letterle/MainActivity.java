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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.volley.RequestQueue;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //------------------------------------------------------------------
    private String word = "";
    public String guessWord = "";
    private List<String> possibleWords4 = new ArrayList<>();
    private List<String> possibleWords5 = new ArrayList<>();
    private List<String> possibleWords6 = new ArrayList<>();
    //------------------------------------------------------------------

    public int column;
    public int row;
    private ResultsDialog resultsDialog;
    private ConfirmationDialog confirmationDialog;
    private AccountMenu accountMenu;
    public int difficulty;
    private int nextDifficulty;
    private RequestQueue requestQueue;
    public int a;
    public int tries;
    public int statState = 1;
    public String username;


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

    public List<String> getRightPossibleWords()
    {
        if(difficulty == 4)
            return possibleWords4;
        if(difficulty == 5)
            return possibleWords5;
        if(difficulty == 6)
            return possibleWords6;
        return null;
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
        accountMenu = new AccountMenu(this);
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
            if (guessWord.equals(word)) {
                tries++;
                resultsDialog.updateDialog(true, tries);
                wordGuessed();
                showAnimation("won", row);
            }
            if(tries == 5)
            {
                resultsDialog.updateDialog(false, 0);
                sendToastMessage("No more tries, you lost :(");
                resultsDialog.readNewDialogData();
                resultsDialog.show();
                resultsDialog.setStatTiles();

            }
            if (getRightPossibleWords().contains(guessWord)) {
                for (int index = 0; index < difficulty; index++) {
                    setColorKey(index, getColorKey(index));
                    setColorTile(index);
                }
                showAnimation("revealletter", row);
                row++;
                column = 0;
                guessWord = "";
                tries++;
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
        if (statState == 0){
            resetGame(false);
            setupNewGame(difficulty);
        }

    }

    public void onBtnYes_Clicked(View caller) {
        confirmationDialog.cancel();
        resultsDialog.updateDialog(false, 0);
        difficulty = nextDifficulty;
        changeBoard(difficulty);
    }

    public void onBtnNo_Clicked(View caller) {
        confirmationDialog.cancel();

    }

    public void onBtnLogIn_Clicked(View caller) {
        EditText thisTxtUsername = accountMenu.findViewById(R.id.txtUsername);
        username = thisTxtUsername.getText().toString();
        changeAccountToName(username);
    }


    public void changeAccountToName(String name)
    {
        if(accountMenu.getIdFromName(name)!=0) {
            resultsDialog.setId(accountMenu.getIdFromName(name));
            sendToastMessage("Logged into: "+name);
        }
        else
        {sendToastMessage("Dit account bestaat dus niet");}
    }

    public void onBtnMakeAccount_Clicked(View caller) {
        EditText thisTxtNewUsername = accountMenu.findViewById(R.id.txtNewUsername);
        username = thisTxtNewUsername.getText().toString();
        accountMenu.makeNewAccount(username);
        accountMenu.getNameAndIdFromDB();
    }

    public void onBtnExit_Clicked(View caller) {
        changeAccountToName(username);
        {sendToastMessage("Account is"+username);}
        accountMenu.cancel();
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
                case (R.id.statMenu) -> {
                    resultsDialog.readNewDialogData();
                    resultsDialog.show();
                    resultsDialog.setStatTiles();
                    statState = 1; //is 1 voor op menu te klikken, is 0 voor gewoon einde spel
                }

                case (R.id.accountChangeMenu) -> {
                    accountMenu.show();
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

    public void setColourOption(int option) {
        //vintage = 2, dark = 1, original = 0
    }

    public void setupNewGame(int diff) {
        Random rn = new Random();
        switch (diff) {
            case 4 -> {
                difficulty = 4;
                int num = rn.nextInt(possibleWords4.size());
                word = possibleWords4.get(num);
                System.out.println(word);
            }

            case 5 -> {
                difficulty = 5;
                int num = rn.nextInt(possibleWords5.size());
                word = possibleWords5.get(num);
                System.out.println(word);
            }
            case 6 -> {
                difficulty = 6;
                int num = rn.nextInt(possibleWords6.size());
                word = possibleWords6.get(num);
                System.out.println(word);
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
         #              Database                #
         ########################################
     */

    public void addPossibleWords(String link, String kolom, List<String> ThisPossibleWords)
    {
        requestQueue = Volley.newRequestQueue(getContext());
        String requestURL = link;

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        String responseString = "";
                        for( int i = 0; i < responseArray.length(); i++ )
                        {
                            JSONObject curObject = responseArray.getJSONObject( i );
                            responseString = curObject.getString( kolom );
                            ThisPossibleWords.add(responseString);

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
                    ;
                }
        );

        requestQueue.add(submitRequest);

    }


    private MainActivity getContext() {
        return this;
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
        statState = 0;
        resultsDialog.readNewDialogData();
        resultsDialog.show();
        resultsDialog.setStatTiles();

    }


}