package edu.sjsu.android.minigamealarmclock.util;

import android.view.View;

import edu.sjsu.android.minigamealarmclock.model.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onItemClick(Alarm alarm, View view);
}
