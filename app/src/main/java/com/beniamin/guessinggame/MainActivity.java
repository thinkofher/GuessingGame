package com.beniamin.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

// my imports
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    
    private int theNumber;
    private int numberOfTries;
    private int range;
    private int chances;
    private boolean gameFinished;

    private EditText txtGuess;
    private Button btnGuess;
    private Button playAgainButton;
    private TextView lblOutput;
    private TextView lblRange;
    private TextView lblChances;

    public void checkGuess() {

        // if game is finished clicking the button
        // should does nothing
        if (!gameFinished) {

            String guessText = txtGuess.getText().toString();
            String message = "";

            try {
                int guess = Integer.parseInt(guessText);

                if (guess < theNumber) {
                    numberOfTries++;
                    chances--;
                    message = guess + " is too low. Try again.";
                }
                else if (guess > theNumber) {
                    numberOfTries++;
                    chances--;
                    message = guess + " is too high. Try again.";
                }
                else {
                    numberOfTries++;
                    message = guess +
                            " is correct. You win after " + numberOfTries
                            + " tries!";

                    gameWon(message);
                }

                // loosing game
                if (chances <= 0 && !gameFinished) {
                    message = "You loose. The number was " + theNumber;
                    String messagePop = guess +
                            " is not correct. You loose. The number was " +
                            theNumber;
                    gameLoose(messagePop);
                }
            } catch (Exception e) {
                message = "Enter a whole number between 1 and "
                        + range + ".";
            } finally {
                lblOutput.setText(message);
                txtGuess.requestFocus();
                txtGuess.selectAll();
                updateRange();
            }
        } else {
            String tryAgain = "Start new game to try again!";
            Toast.makeText(MainActivity.this, tryAgain,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void newGame() {
        String startMessage = "Enter a number, then click Guess!";

        theNumber = (int) (Math.random()*range + 1);
        numberOfTries = 0;
        gameFinished = false;

        // setting chances
        switch (range) {
            case 10:
                chances = 15;
                break;
            case 100:
                chances = 20;
                break;
            case 1000:
                chances = 25;
                break;
        }

        playAgainButton.setVisibility(View.INVISIBLE);
        lblOutput.setText(startMessage);
        lblRange.setText(
                "Enter a number between 1 and " + range +"."
        );
        lblChances.setText(
                "You have " + chances + " chances left."
        );

        txtGuess.setText("" + range/2);
        txtGuess.requestFocus();
        txtGuess.selectAll();
    }

    private  void updateRange(){
        String message = "You have " + chances + " chances left.";
        lblChances.setText(
                message
        );
    }

    private  void gameLoose(String message) {

        // pop-up message
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_LONG).show();
        gameFinished = true;
        playAgainButton.setVisibility(View.VISIBLE);

        SharedPreferences preferences=
                PreferenceManager.getDefaultSharedPreferences(this);

        // if there is no gamesWon preference in SharedPreferences
        // just create it and set it to 1
        int gamesLoose = preferences.getInt("gamesLoose", 0) + 1;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesLoose", gamesLoose);
        editor.apply();
    }

    private void gameWon(String message) {

        // pop-up message
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_LONG).show();
        gameFinished = true;
        playAgainButton.setVisibility(View.VISIBLE);

        SharedPreferences preferences=
                PreferenceManager.getDefaultSharedPreferences(this);

        // if there is no gamesWon preference in SharedPreferences
        // just create it and set it to 1
        int gamesWon = preferences.getInt("gamesWon", 0) + 1;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesWon", gamesWon);
        editor.apply();
    }

    // AlertDialog with information about app author
    private void aboutMe() {
        AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
        aboutDialog.setTitle("About Guessing Game");
        aboutDialog.setMessage(
                "My first android App\n" +
                        "(c)2018 Beniamin Dudek"
        );
        aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        aboutDialog.show();
    }

    // Alert Dialog with select range menu
    private void selectRange() {
        final CharSequence[] items =  {"1 to 10", "1 to 100", "1 to 1000"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the Range");
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item) {
                            case 0:
                                range = 10;
                                storeRange(range);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(range);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(range);
                                newGame();
                                break;
                        }
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Alert Dialog with game stats
    private void gameStats() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        int gamesWon = preferences.getInt("gamesWon", 0);
        int gamesLoose = preferences.getInt("gamesLoose", 0);

        AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
        statDialog.setTitle("Guessing Game Stats");

        double winToLoose = (double) gamesWon/ (double) gamesLoose;


        String ratioString = String.format("Your Win/Lose ratio is %.2f", winToLoose);
        statDialog.setMessage(
                "You have won " + gamesWon + " and have loose " + gamesLoose +
                        " games. Way to go!\n\n" +
                        ratioString
        );
        statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        statDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        playAgainButton = (Button) findViewById(R.id.btnPlayAgain);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.lblRange);
        lblChances = (TextView) findViewById(R.id.lblChances);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Loading preferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);

        // Starting new game after setting up app
        newGame();

        // pressing guess button
        btnGuess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        checkGuess();
                    }
                }
        );

        // hitting enter in number keyboard in txtGuess
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkGuess();
                return true;
            }
        });

        // pressing play again button
        playAgainButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newGame();
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                selectRange();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                gameStats();
                return true;
            case R.id.action_about:
                aboutMe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void storeRange(int newRange) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }
}
