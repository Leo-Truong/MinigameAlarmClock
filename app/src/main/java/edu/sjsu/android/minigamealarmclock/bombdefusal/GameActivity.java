package edu.sjsu.android.minigamealarmclock.bombdefusal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import edu.sjsu.android.minigamealarmclock.AlarmService;
import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView wiresToCutTextView;
    private CountDownTimer countDownTimer;
    private final long timerDuration = 30000; // 30 seconds
    private final ArrayList<String> wires = new ArrayList<>(Arrays.asList("G", "R", "O1", "Y1", "B", "P", "O2", "Y2"));
    private ArrayList<String> tempwires = new ArrayList<String>();
    private ArrayList<String> wiresToCut = new ArrayList<String>();
    Random random;
    private int success = 0; // # of successful defusals
    private ActivityGameBinding binding;
    private boolean wrongWire = false;
    private Drawable originalBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        originalBackground = binding.BButton.getBackground();

        timerTextView = findViewById(R.id.timerTextView);
        wiresToCutTextView = findViewById(R.id.wiresToCutTextView);

        startGame();
    }

    /**
     * Reusable method to handle button click
     * @param b Button that corresponds to its respective wire
     * @param wireColor Color of wire the corresponds to each button
     */
    private void onButtonClicked(Button b, String wireColor) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks if user chose correct wire
                if (Objects.equals(wiresToCut.get(0), wireColor)) {
                    wiresToCut.remove(wireColor);
                    b.setBackgroundColor(Color.GREEN);
                    b.setEnabled(false);
                    if (wiresToCut.isEmpty()) // Disable buttons on the event user completes game
                        disableAllButtons();
                    Log.d("GameActivty", "Removed: " + wireColor);
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }else{
                    b.setBackgroundColor(Color.RED);
                    disableAllButtons();
                    wrongWire = true;
                }

                // Used to update the wire visual
                checkWire(wireColor, "G", binding.wire1, binding.wirecut1);
                checkWire(wireColor, "R", binding.wire2, binding.wirecut2);
                checkWire(wireColor, "O1", binding.wire3, binding.wirecut3);
                checkWire(wireColor, "Y1", binding.wire4, binding.wirecut4);
                checkWire(wireColor, "B", binding.wire5, binding.wirecut5);
                checkWire(wireColor, "P", binding.wire6, binding.wirecut6);
                checkWire(wireColor, "O2", binding.wire7, binding.wirecut7);
                checkWire(wireColor, "Y2", binding.wire8, binding.wirecut8);

            }
        });
    }

    /**
     * Method to update the wire visuals depending on what button is pressed
     * @param wireColor the color of the wire that corresponds with the button pressed
     * @param matchWireColor the color of the wire to be updated
     * @param wire png of original uncut wire
     * @param wirecut png of cut wire
     */
    private void checkWire(String wireColor, String matchWireColor, ImageView wire, ImageView wirecut){
        if(Objects.equals(wireColor, matchWireColor)){
            wire.setVisibility(View.INVISIBLE);
            wirecut.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets up all the buttons for the game
     */
    private void handleButtonLogic(){
        onButtonClicked(binding.GButton, "G");
        onButtonClicked(binding.RButton, "R");
        onButtonClicked(binding.O1Button, "O1");
        onButtonClicked(binding.Y1Button, "Y1");
        onButtonClicked(binding.BButton, "B");
        onButtonClicked(binding.PButton, "P");
        onButtonClicked(binding.O2Button, "O2");
        onButtonClicked(binding.Y2Button, "Y2");

    }

    /**
     * Method to choose wires to cut
     */
    private void startGame() {
        handleButtonLogic();
        disableAllButtons();
        random = new Random();
        tempwires = new ArrayList<>(wires); // Shallow copy of wires

        // Prepare visibility of wiresToCut and timer
        if (wiresToCutTextView.getVisibility() == View.INVISIBLE)
            wiresToCutTextView.setVisibility(View.VISIBLE);
        if (timerTextView.getVisibility() == View.VISIBLE)
            timerTextView.setVisibility(View.INVISIBLE);

        new CountDownTimer(5000, 1000) { // Total time of 5 seconds, tick every 1 second

            @Override
            public void onTick(long millisUntilFinished) {
                if (!tempwires.isEmpty()) {
                    int randInt = random.nextInt(tempwires.size());
                    String wireToCut = tempwires.get(randInt);  // Get the wire to cut
                    wiresToCutTextView.setText(wireToCut);  // Display the wire
                    wiresToCut.add(wireToCut);  // Add to the wiresToCut list
                    tempwires.remove(randInt);  // Remove the wire from the list
                } else {
                    Log.e("RandomError", "Bound must be positive!");
                }
            }

            @Override
            public void onFinish() {
                wiresToCutTextView.setVisibility(View.INVISIBLE); // Makes the the TextView invisible
                enableAllButtons();  // Enables all buttons

                Log.d("GameActivity", "wires: " + tempwires.toString());
                Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                startTimer(); // Begins timer for game
            }
        }.start();
    }

    /**
     * Method to keep track of the timer used in the bomb
     */
    private void startTimer() {
        if (timerTextView.getVisibility() == View.INVISIBLE)
            timerTextView.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timerDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("" + millisUntilFinished / 1000);

                // User successfully chose the correct wires in the the correct order
                if(wiresToCut.isEmpty()){
                    success += 1;
                    if (success == 1 && binding.greendot1.getVisibility() == View.INVISIBLE){
                        binding.greendot1.setVisibility(View.VISIBLE);
                    }
                    if (success == 2 && binding.greendot2.getVisibility() == View.INVISIBLE){
                        binding.greendot2.setVisibility(View.VISIBLE);
                    }
                    if (success == 3 && binding.greendot3.getVisibility() == View.INVISIBLE){
                        binding.greendot3.setVisibility(View.VISIBLE);
                        // stop alarm
                        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                        getApplicationContext().stopService(intentService);
                        finish();
                    }
                    Log.d("GameActivity", "success: " + success);
                    cancel();
                    resetGame();
                }
                // Cancels the timer if wrong wire is cut
                if (wrongWire){
                    wrongWire = false;
                    cancel();
                    resetGame();
                }
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        };
        countDownTimer.start();
    }

    private void onTimerFinished() {
        // Game over logic here
        resetGame();
        Log.d("GameActivity", "Timer finished!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the timer if the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Method to reset game logic
     */
    private void resetGame(){
        wiresToCut.clear();
        resetAllButtons();
        resetAllWires();
        startGame();
    }

    /**
     * Method to disable all buttons that correlates with the wires
     */
    private void disableAllButtons(){
        binding.GButton.setEnabled(false);
        binding.RButton.setEnabled(false);
        binding.O1Button.setEnabled(false);
        binding.Y1Button.setEnabled(false);
        binding.BButton.setEnabled(false);
        binding.PButton.setEnabled(false);
        binding.O2Button.setEnabled(false);
        binding.Y2Button.setEnabled(false);
    }

    /**
     * Method to enable all buttons that correlates with the wires
     */
    private void enableAllButtons(){
        binding.GButton.setEnabled(true);
        binding.RButton.setEnabled(true);
        binding.O1Button.setEnabled(true);
        binding.Y1Button.setEnabled(true);
        binding.BButton.setEnabled(true);
        binding.PButton.setEnabled(true);
        binding.O2Button.setEnabled(true);
        binding.Y2Button.setEnabled(true);
    }

    /**
     * Method to reset all button backgrounds
     */
    private void resetAllButtons(){
        binding.GButton.setBackground(originalBackground);
        binding.RButton.setBackground(originalBackground);
        binding.O1Button.setBackground(originalBackground);
        binding.Y1Button.setBackground(originalBackground);
        binding.BButton.setBackground(originalBackground);
        binding.PButton.setBackground(originalBackground);
        binding.O2Button.setBackground(originalBackground);
        binding.Y2Button.setBackground(originalBackground);
    }

    /**
     * Method to reset all wires
     */
    private void resetAllWires(){

        // Set all wire pngs to VISIBLE
        binding.wire1.setVisibility(View.VISIBLE);
        binding.wire2.setVisibility(View.VISIBLE);
        binding.wire3.setVisibility(View.VISIBLE);
        binding.wire4.setVisibility(View.VISIBLE);
        binding.wire5.setVisibility(View.VISIBLE);
        binding.wire6.setVisibility(View.VISIBLE);
        binding.wire7.setVisibility(View.VISIBLE);
        binding.wire8.setVisibility(View.VISIBLE);

        // Set all wirecut pngs to INVISIBLE
        binding.wirecut1.setVisibility(View.INVISIBLE);
        binding.wirecut2.setVisibility(View.INVISIBLE);
        binding.wirecut3.setVisibility(View.INVISIBLE);
        binding.wirecut4.setVisibility(View.INVISIBLE);
        binding.wirecut5.setVisibility(View.INVISIBLE);
        binding.wirecut6.setVisibility(View.INVISIBLE);
        binding.wirecut7.setVisibility(View.INVISIBLE);
        binding.wirecut8.setVisibility(View.INVISIBLE);
    }
}