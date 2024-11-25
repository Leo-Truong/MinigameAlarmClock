package edu.sjsu.android.minigamealarmclock;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import edu.sjsu.android.minigamealarmclock.bombdefusal.GameActivity;
import edu.sjsu.android.minigamealarmclock.databinding.ActivityRingBinding;

public class RingActivity extends AppCompatActivity {
    Alarm alarm;
    private AlarmsListViewModel alarmsListViewModel;
    private ActivityRingBinding ringActivityViewBinding;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringActivityViewBinding = ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(ringActivityViewBinding.getRoot());

        // Displays current date
        String currentDate = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(new Date());
        ringActivityViewBinding.ringactivityDate.setText(currentDate);

        // Displays real-time clock
        runnable = new Runnable() {
            @Override
            public void run() {
                // Update the time every second
                String currentTime = new SimpleDateFormat("h:mm", Locale.getDefault()).format(new Date());
                ringActivityViewBinding.ringactivityTime.setText(currentTime);

                handler.postDelayed(this, 1000); // Repeat every 1 second
            }
        };
        handler.post(runnable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        alarmsListViewModel = new ViewModelProvider(this).get(AlarmsListViewModel.class);
        Bundle bundle = getIntent().getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle != null)
            alarm = bundle.getParcelable(getString(R.string.arg_alarm_obj));

        ringActivityViewBinding.startMinigame.setOnClickListener(v -> startGame());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false);
            setTurnScreenOn(false);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
    }

    private void startGame(){
        if(alarm!=null) {
            alarm.setStarted(false);
            alarm.cancelAlarm(getBaseContext());
            alarmsListViewModel.update(alarm);
            if (Objects.equals(alarm.getAlarmMinigame(), "Bomb Defusal")){
                Intent intentGame = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intentGame);
            }
        }
        finish();
    }
}