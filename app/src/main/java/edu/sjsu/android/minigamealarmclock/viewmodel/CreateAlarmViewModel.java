package edu.sjsu.android.minigamealarmclock.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.data.AlarmRepository;

public class CreateAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    /**
     * Method that inserts an alarm into the database
     * @param alarm alarm to be inserted
     */
    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }

    /**
     * Method that updates an alarm in the database
     * @param alarm alarm to be updated
     */
    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }
}
