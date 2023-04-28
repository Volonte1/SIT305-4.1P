package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        //variable declaration
        private EditText workoutEditText, restEditText;
        private TextView workoutTextView, restTextView;
        private ProgressBar timerProgressBar;

        private CountDownTimer workoutTimer, restTimer;
        private final Handler handler = new Handler();
        private boolean isWorkoutPhase = true;
        private long workoutTime, restTime;
        private long workoutTimeRemaining, restTimeRemaining;


        //findviewby Id functions
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                workoutEditText = findViewById(R.id.workout_time_edit_text);
                restEditText = findViewById(R.id.rest_time_edit_text);
                workoutTextView = findViewById(R.id.workout_time_text_view);
                restTextView = findViewById(R.id.rest_time_text_view);
                timerProgressBar = findViewById(R.id.progress_bar);
                Button startButton = findViewById(R.id.start_button);
                Button stopButton = findViewById(R.id.stop_button);


                //onclick functioons
                startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                start();
                        }
                });

                stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                stop();
                        }
                });
        }
                //gets the string for seconds and converts it
        private String getTimeString(long millis) {
                int seconds = (int) millis / 1000;
                        return String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        }

        //start workout function is the timer ticking down, basically updates UI and ticks down once rest is finished
        private void startWorkout() {
                isWorkoutPhase = true;
                        workoutTimeRemaining = workoutTime;
                                updateUI();
                                        workoutTimer = new CountDownTimer(workoutTimeRemaining, 1000) {
                        @Override
                                public void onTick(long millisUntilFinished) {
                                        workoutTimeRemaining = millisUntilFinished;
                                                updateUI();
                        }

                        @Override
                                public void onFinish() {
                                        workoutTimer = null;
                                                startRest();

                        }
                }.start();
        }

        //start rest function is the timer ticking down, basically updates UI and ticks down once workout is finished
        private void startRest() {
                isWorkoutPhase = false;
                        restTimeRemaining = restTime;
                                updateUI();
                                        restTimer = new CountDownTimer(restTimeRemaining, 1000) {
                        @Override
                                public void onTick(long millisUntilFinished) {
                                        restTimeRemaining = millisUntilFinished;
                                                updateUI();
                        }

                        @Override
                        public void onFinish() {
                                restTimer = null;
                                        startWorkout();

                        }
                }.start();
        }    private void updateUI() {
                if (isWorkoutPhase) {
                        workoutTextView.setText(getTimeString(workoutTimeRemaining));
                                int progress = (int) (workoutTimeRemaining * 100 / workoutTime);
                                        timerProgressBar.setProgress(progress);
                        } else {
                                restTextView.setText(getTimeString(restTimeRemaining));
                                        int progress = (int) (restTimeRemaining * 100 / restTime);
                                                timerProgressBar.setProgress(progress);
                }
        }


        //This is the starting function, it gets the input values from the edit texts and validates them for a value.
        //It then converrts the input values to longs so that the start timer function can recognize them and run
        private void start() {

                String workoutTimeInput = workoutEditText.getText().toString();
                String restTimeInput = restEditText.getText().toString();


                if (workoutTimeInput.isEmpty() || restTimeInput.isEmpty()) {
                        Toast.makeText(this, "You need to enter times for both rest and workout", Toast.LENGTH_SHORT).show();
                                return;
                }


                workoutTime = Long.parseLong(workoutTimeInput) * 1000;
                        restTime = Long.parseLong(restTimeInput) * 1000;


                startWorkout();
        }


        //This is the stop function, it the timer does not = null, it will cancel the timer, same goes for rest
        private void stop() {
                if (workoutTimer != null) {
                        workoutTimer.cancel();
                                workoutTimer = null;
                }
                if (restTimer != null) {
                        restTimer.cancel();
                                restTimer = null;
                }
                handler.removeCallbacksAndMessages(null);
                        workoutTextView.setText("");
                                restTextView.setText("");
                                        timerProgressBar.setProgress(0);
        }



}



