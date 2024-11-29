package edu.sjsu.android.minigamealarmclock.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.databinding.FragmentAlarmBinding;
import edu.sjsu.android.minigamealarmclock.util.DayUtil;
import edu.sjsu.android.minigamealarmclock.util.OnToggleAlarmListener;

public class AlarmViewHolder extends RecyclerView.ViewHolder{
    private TextView alarmTime;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private Switch alarmStarted;
    private ImageButton deleteAlarm;
    private View itemView;
    private TextView alarmDay;

    public AlarmViewHolder(@NonNull FragmentAlarmBinding itemAlarmBinding) {
        super(itemAlarmBinding.getRoot());
        alarmTime = itemAlarmBinding.itemAlarmTime;
        alarmStarted = itemAlarmBinding.itemAlarmStarted;
        alarmRecurringDays = itemAlarmBinding.itemAlarmRecurringDays;
        alarmTitle = itemAlarmBinding.itemAlarmTitle;
        deleteAlarm= itemAlarmBinding.itemAlarmRecurringDelete;
        alarmDay = itemAlarmBinding.itemAlarmDay;
        this.itemView=itemAlarmBinding.getRoot();
    }

    public void bind(Alarm alarm, OnToggleAlarmListener listener) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurringDays.setText(R.string.once_off);
        }

        if (!alarm.getAlarmName().isEmpty()) {
            alarmTitle.setText(alarm.getAlarmName());
        } else {
            alarmTitle.setText("My alarm");
        }
        if(alarm.isRecurring()){
            alarmDay.setText(alarm.getRecurringDaysText());
        }
        else {
            alarmDay.setText(DayUtil.getDay(alarm.getHour(),alarm.getMinute()));
        }
        alarmStarted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isShown() || buttonView.isPressed())
                listener.onToggle(alarm);
        });

        deleteAlarm.setOnClickListener(view -> listener.onDelete(alarm));

        itemView.setOnClickListener(view -> listener.onItemClick(alarm,view));
    }
}
