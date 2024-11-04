package edu.sjsu.android.minigamealarmclock;

import android.view.View;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onItemClick(Alarm alarm, View view);
}
