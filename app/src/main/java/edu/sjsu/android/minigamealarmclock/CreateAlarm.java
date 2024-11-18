package edu.sjsu.android.minigamealarmclock;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.Random;

import edu.sjsu.android.minigamealarmclock.databinding.FragmentCreateAlarmBinding;
import edu.sjsu.android.minigamealarmclock.util.TimePickerUtil;

public class CreateAlarm extends Fragment {
    FragmentCreateAlarmBinding createAlarmBinding;
    private CreateAlarmViewModel createAlarmViewModel;
    boolean isVibrate = false;
    boolean isRecurring;
    String tone;
    Alarm alarm;
    Ringtone ringtone;

    public CreateAlarm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alarm= (Alarm) getArguments().getSerializable(getString(R.string.arg_alarm_obj));
        }
        createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createAlarmBinding = FragmentCreateAlarmBinding.inflate(inflater,container,false);
        View v = createAlarmBinding.getRoot();
        tone = RingtoneManager.getActualDefaultRingtoneUri(this.getContext(), RingtoneManager.TYPE_ALARM).toString();
        ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
        createAlarmBinding.alarmSoundText.setText(ringtone.getTitle(getContext()));
        if(alarm!=null){
            updateAlarmInfo(alarm);
        }

        createAlarmBinding.createAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm!=null) {
                    updateAlarm();
                }
                else{
                    scheduleAlarm();
                }

                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        createAlarmBinding.cardAlarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) Uri.parse(tone));
                startActivityForResult(intent, 5);
            }
        });

        createAlarmBinding.vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isVibrate=true;
                }
                else{
                    isVibrate=false;
                }
            }
        });

//        createAlarmBinding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//                createAlarmBinding.fragmentCreatealarmScheduleAlarmHeading.setText(DayUtil.getDay(TimePickerUtil.getTimePickerHour(timePicker),TimePickerUtil.getTimePickerMinute(timePicker)));
//            }
//        });

        return v;
    }

    private void scheduleAlarm() {
        String alarmName = getString(R.string.alarm_name);
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
        isRecurring = createAlarmBinding.checkMon.isChecked() ||
                createAlarmBinding.checkTue.isChecked() ||
                createAlarmBinding.checkWed.isChecked() ||
                createAlarmBinding.checkThu.isChecked() ||
                createAlarmBinding.checkFri.isChecked() ||
                createAlarmBinding.checkSat.isChecked() ||
                createAlarmBinding.checkSun.isChecked();
        if(!createAlarmBinding.createAlarmName.getText().toString().isEmpty()){
            alarmName = createAlarmBinding.createAlarmName.getText().toString();
        }
        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(createAlarmBinding.timePicker),
                TimePickerUtil.getTimePickerMinute(createAlarmBinding.timePicker),
                alarmName,
                true,
                isRecurring,
                createAlarmBinding.checkMon.isChecked(),
                createAlarmBinding.checkTue.isChecked(),
                createAlarmBinding.checkWed.isChecked(),
                createAlarmBinding.checkThu.isChecked(),
                createAlarmBinding.checkFri.isChecked(),
                createAlarmBinding.checkSat.isChecked(),
                createAlarmBinding.checkSun.isChecked(),
                tone,
                isVibrate
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }

    private void updateAlarm(){
        String alarmName = getString(R.string.alarm_name);
//        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
        if(!createAlarmBinding.createAlarmName.getText().toString().isEmpty()){
            alarmName=createAlarmBinding.createAlarmName.getText().toString();
        }
        isRecurring = createAlarmBinding.checkMon.isChecked() ||
                createAlarmBinding.checkTue.isChecked() ||
                createAlarmBinding.checkWed.isChecked() ||
                createAlarmBinding.checkThu.isChecked() ||
                createAlarmBinding.checkFri.isChecked() ||
                createAlarmBinding.checkSat.isChecked() ||
                createAlarmBinding.checkSun.isChecked();
        Alarm updatedAlarm = new Alarm(
                alarm.getAlarmId(),
                TimePickerUtil.getTimePickerHour(createAlarmBinding.timePicker),
                TimePickerUtil.getTimePickerMinute(createAlarmBinding.timePicker),
                alarmName,
                true,
                true,
                createAlarmBinding.checkMon.isChecked(),
                createAlarmBinding.checkTue.isChecked(),
                createAlarmBinding.checkWed.isChecked(),
                createAlarmBinding.checkThu.isChecked(),
                createAlarmBinding.checkFri.isChecked(),
                createAlarmBinding.checkSat.isChecked(),
                createAlarmBinding.checkSun.isChecked(),
                tone,
                isVibrate
        );
        createAlarmViewModel.update(updatedAlarm);
        updatedAlarm.schedule(getContext());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            if (uri != null) {
                tone=uri.toString();
                if(title!=null && !title.isEmpty())
                    createAlarmBinding.alarmSoundText.setText(title);
            } else {
                createAlarmBinding.alarmSoundText.setText("");
            }
        }
    }

    private void updateAlarmInfo(Alarm alarm){
        createAlarmBinding.createAlarmName.setText(alarm.getAlarmName());
        createAlarmBinding.timePicker.setHour(alarm.getHour());
        createAlarmBinding.timePicker.setMinute(alarm.getMinute());
        if(alarm.isRecurring()){
            if(alarm.isMonday())
                createAlarmBinding.checkMon.setChecked(true);
            if(alarm.isTuesday())
                createAlarmBinding.checkTue.setChecked(true);
            if(alarm.isWednesday())
                createAlarmBinding.checkWed.setChecked(true);
            if(alarm.isThursday())
                createAlarmBinding.checkThu.setChecked(true);
            if(alarm.isFriday())
                createAlarmBinding.checkFri.setChecked(true);
            if(alarm.isSaturday())
                createAlarmBinding.checkSat.setChecked(true);
            if(alarm.isSunday())
                createAlarmBinding.checkSun.setChecked(true);
            tone = alarm.getAlarmSound();
            ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
            createAlarmBinding.alarmSoundText.setText(ringtone.getTitle(getContext()));
            if(alarm.isVibration())
                createAlarmBinding.vibrationSwitch.setChecked(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createAlarmBinding = null;
    }
}