package edu.sjsu.android.minigamealarmclock.util;

import java.util.Calendar;

public final class DayUtil {
    /**
     * Method to convert integer value into a representing day of the week
     * @param day integer value
     * @return the representing day
     * @throws Exception when the integer value is not a valid representation of a day
     */
    public static String toDay(int day) throws Exception {
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        throw new Exception("Could not locate day");
    }

    /**
     * Method to determine if the specified time is on today or tomorrow
     * @param hour the given hour
     * @param minute the given minute
     * @return Tomorrow if the given time is on today otherwise Today
     */
    public static String getDay(int hour,int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // if alarm time has already passed
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            return "Tomorrow";
        }
        else{
            return "Today";
        }
    }
}
