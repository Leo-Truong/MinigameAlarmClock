package edu.sjsu.android.minigamealarmclock.broadcrastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.service.AlarmService;
import edu.sjsu.android.minigamealarmclock.service.RescheduleAlarmsService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    Alarm alarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Used when device is rebooted and reschedules alarms that might've been lost
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = "Alarm Reboot";
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {
            Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            if (bundle != null)
                alarm = bundle.getParcelable(context.getString(R.string.arg_alarm_obj));

            // Show the alarm has been received and start alarm service
            String toastText = "Alarm Received";
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if(alarm!=null) {
                if (!alarm.isRecurring()) {
                        startAlarmService(context, alarm);
                } else {
                    if (isAlarmToday(alarm)) {
                        startAlarmService(context, alarm);
                    }
                }
            }
        }
    }

    /**
     * Method to determine if the alarm is set for the current day
     * @param alarm1 alarm that is being checked
     * @return return true if the alarm is for today otherwise false
     */
    private boolean isAlarmToday(Alarm alarm1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                return alarm1.isMonday();
            case Calendar.TUESDAY:
                return alarm1.isTuesday();
            case Calendar.WEDNESDAY:
                return alarm1.isWednesday();
            case Calendar.THURSDAY:
                return alarm1.isThursday();
            case Calendar.FRIDAY:
                return alarm1.isFriday();
            case Calendar.SATURDAY:
                return alarm1.isSaturday();
            case Calendar.SUNDAY:
                return alarm1.isSunday();
        }
        return false;
    }

    /**
     * Method to start the alarm service
     * @param context current context
     * @param alarm1 current alarm
     */
    private void startAlarmService(Context context, Alarm alarm1) {
        Intent intentService = new Intent(context, AlarmService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.arg_alarm_obj), alarm1);
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    /**
     * Method to begin the reschedule alarm service
     * @param context current context
     */
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

}
