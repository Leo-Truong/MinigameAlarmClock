package edu.sjsu.android.minigamealarmclock.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.data.AlarmRepository;

public class AlarmsListViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private LiveData<List<Alarm>> alarmsLiveData;

    public AlarmsListViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
        alarmsLiveData = alarmRepository.getAlarmsLiveData();
    }

    /**
     * Method to update the alarm in the database
     * @param alarm alarm to be updated
     */
    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }

    /**
     * Method that returns the live data of the alarms
     * @return LiveData of alarms
     */
    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    /**
     * Method to delete an alarm with the corresponding alarmId
     * @param alarmId Primary key that represents the alarm
     */
    public void delete(int alarmId){
        alarmRepository.delete(alarmId);
    }
}
