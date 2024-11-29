package edu.sjsu.android.minigamealarmclock.util;

import android.os.Build;
import android.widget.TimePicker;

public final class TimePickerUtil {
    /**
     * Method to get the hour from the timer picker
     * @param tp the timepicker
     * @return hour
     */
    public static int getTimePickerHour(TimePicker tp) {
        return tp.getHour();
    }

    /**
     * Method to get the minute from the timepicker
     * @param tp the timepicker
     * @return minute
     */
    public static int getTimePickerMinute(TimePicker tp) {
        return tp.getMinute();
    }
}
