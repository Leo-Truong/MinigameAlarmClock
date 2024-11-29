package edu.sjsu.android.minigamealarmclock.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.sjsu.android.minigamealarmclock.model.Alarm;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> alarmsLiveData;

    // Initialize the alarm database and data
    public AlarmRepository(Application application) {
        AlarmDatabase db = AlarmDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        alarmsLiveData = alarmDao.getAlarms();
    }

    /**
     * Method that inserts the alarm into the database
     * @param alarm alarm to be inserted
     */
    public void insert(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> alarmDao.insert(alarm));
    }

    /**
     * Method that updates the alarm in the database
     * @param alarm alarm to be updated
     */
    public void update(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> alarmDao.update(alarm));
    }

    /**
     * Returns the live data for the alarms
     * @return alarm live data
     */
    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    /**
     * Method that deletes an alarm based on the alarmId from the database
     * @param alarmId current alarmId
     */
    public void delete(int alarmId){
        AlarmDatabase.databaseWriteExecutor.execute(() -> alarmDao.delete(alarmId));
    }
}
