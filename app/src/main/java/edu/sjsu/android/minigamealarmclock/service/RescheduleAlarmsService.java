package edu.sjsu.android.minigamealarmclock.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.data.AlarmRepository;

public class RescheduleAlarmsService extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Method that observes the alarm data and schedules the alarms that
    // are marked as started.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        AlarmRepository alarmRepository = new AlarmRepository(getApplication());

        alarmRepository.getAlarmsLiveData().observe(this, alarms -> {
            for (Alarm a : alarms) {
                if (a.isStarted()) {
                    a.schedule(getApplicationContext());
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return null;
    }
}
