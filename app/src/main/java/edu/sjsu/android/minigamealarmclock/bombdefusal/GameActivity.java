package edu.sjsu.android.minigamealarmclock.bombdefusal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView wiresToCutTextView;
    private CountDownTimer countDownTimer;
    private final long timerDuration = 30000; // 30 seconds
    private final ArrayList<String> wires = new ArrayList<>(Arrays.asList("G", "R", "O1", "Y1", "B", "P", "O2", "Y2"));
    private ArrayList<String> wiresToCut = new ArrayList<String>();
    private ArrayList<String> tempwires = new ArrayList<String>();
    Random random;
    private int success = 0; // # of successful defusals
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timerTextView = findViewById(R.id.timerTextView);
        wiresToCutTextView = findViewById(R.id.wiresToCutTextView);

        // Disable buttons initially
        binding.GButton.setEnabled(false);
        binding.RButton.setEnabled(false);
        binding.O1Button.setEnabled(false);
        binding.Y1Button.setEnabled(false);
        binding.BButton.setEnabled(false);
        binding.PButton.setEnabled(false);
        binding.O2Button.setEnabled(false);
        binding.Y2Button.setEnabled(false);

        startGame();
        binding.GButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("G")){
                    wiresToCut.remove("G");
                    binding.GButton.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed G");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.RButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("R")){
                    wiresToCut.remove("R");
                    binding.RButton.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed R");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.O1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("O1")){
                    wiresToCut.remove("O1");
                    binding.O1Button.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed O1");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.Y1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("Y1")){
                    wiresToCut.remove("Y1");
                    binding.Y1Button.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed Y1");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.BButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("B")){
                    wiresToCut.remove("B");
                    binding.BButton.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed B");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.PButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("P")){
                    wiresToCut.remove("P");
                    binding.PButton.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed P");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.O2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("O2")){
                    wiresToCut.remove("O2");
                    binding.O2Button.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed O2");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });

        binding.Y2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiresToCut.contains("Y2")){
                    wiresToCut.remove("Y2");
                    binding.Y2Button.setBackgroundColor(Color.GREEN);
                    Log.d("GameActivty", "Removed Y2");
                    Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                }

            }
        });


    }

    /**
     * Method to choose wires to cut
     */
    private void startGame() {
        random = new Random();
        tempwires = wires;

        new CountDownTimer(5000, 1000) { // Total time of 5 seconds, tick every 1 second

            @Override
            public void onTick(long millisUntilFinished) {
                int randInt = random.nextInt(tempwires.size());  // Generate random index
                String wireToCut = tempwires.get(randInt);  // Get the wire to cut
                wiresToCutTextView.setText(wireToCut);  // Display the wire
                wiresToCut.add(wireToCut);  // Add to the wiresToCut list
                tempwires.remove(randInt);  // Remove the wire from the list
            }

            @Override
            public void onFinish() {
                wiresToCutTextView.setVisibility(View.INVISIBLE);
                // Enables all buttons
                binding.GButton.setEnabled(true);
                binding.RButton.setEnabled(true);
                binding.O1Button.setEnabled(true);
                binding.Y1Button.setEnabled(true);
                binding.BButton.setEnabled(true);
                binding.PButton.setEnabled(true);
                binding.O2Button.setEnabled(true);
                binding.Y2Button.setEnabled(true);

                Log.d("GameActivity", "wires: " + tempwires.toString());
                Log.d("GameActivity", "wiresToCut: " + wiresToCut.toString());
                startTimer();
            }
        }.start();
    }



    /**
     * Method to keep track of the timer used in the bomb
     */
    private void startTimer() {
        countDownTimer = new CountDownTimer(timerDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("" + millisUntilFinished / 1000);

                if(wiresToCut.isEmpty()){
                    cancel();
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
}