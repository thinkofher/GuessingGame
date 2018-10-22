package com.beniamin.guessinggame;

import android.content.DialogInterface;
import android.os.Bundle;
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
    private int range = 100;
    private boolean gameFinished;

    private EditText txtGuess;
    private Button btnGuess;
    private Button playAgainButton;
    private TextView lblOutput;
    private TextView lblRange;

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
                    message = guess + " is too low. Try again.";
                }
                else if (guess > theNumber) {
                    numberOfTries++;
                    message = guess + " is too high. Try again.";
                }
                else {
                    numberOfTries++;
                    message = guess +
                            " is correct. You win after " + numberOfTries
                            + " tries!";

                    // pop-up message
                    Toast.makeText(MainActivity.this, message,
                            Toast.LENGTH_LONG).show();
                    gameFinished = true;
                    playAgainButton.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {
                message = "Enter a whole number between 1 and "
                        + range + ".";
            } finally {
                lblOutput.setText(message);
                txtGuess.requestFocus();
                txtGuess.selectAll();
            }
        }

    }

    private void newGame() {
        String startMessage = "Enter a number, then click Guess!";

        theNumber = (int) (Math.random()*range + 1);
        numberOfTries = 0;
        gameFinished = false;

        playAgainButton.setVisibility(View.INVISIBLE);
        lblOutput.setText(startMessage);
        lblRange.setText(
                "Enter a number between 1 and " + range +"."
        );
    }

    private void aboutMe(){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        playAgainButton = (Button) findViewById(R.id.btnPlayAgain);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.lblRange);

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
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                return true;
            case R.id.action_about:
                aboutMe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
