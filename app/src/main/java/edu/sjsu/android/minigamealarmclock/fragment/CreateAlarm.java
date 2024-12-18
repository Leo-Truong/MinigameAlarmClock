package edu.sjsu.android.minigamealarmclock.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Random;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.databinding.FragmentCreateAlarmBinding;
import edu.sjsu.android.minigamealarmclock.util.TimePickerUtil;
import edu.sjsu.android.minigamealarmclock.viewmodel.CreateAlarmViewModel;

public class CreateAlarm extends Fragment {
    FragmentCreateAlarmBinding createAlarmBinding;
    private CreateAlarmViewModel createAlarmViewModel;
    boolean isVibrate = false;
    boolean isMaxVolume = false;
    boolean isRecurring;
    String tone;
    Alarm alarm;
    Ringtone ringtone;
    String minigame;

    public CreateAlarm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alarm= getArguments().getParcelable(getString(R.string.arg_alarm_obj));
        }
        createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createAlarmBinding = FragmentCreateAlarmBinding.inflate(inflater,container,false);
        View v = createAlarmBinding.getRoot();

        tone = RingtoneManager.getActualDefaultRingtoneUri(this.getContext(), RingtoneManager.TYPE_ALARM).toString();
        ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
        createAlarmBinding.alarmSoundText.setText(ringtone.getTitle(getContext()));

        // Default minigame
        createAlarmBinding.minigameText.setText(R.string.bomb_defusal);
        minigame = "Bomb Defusal";

        if(alarm!=null){
            updateAlarmInfo(alarm);
        }

        // onClick for Minigames
        createAlarmBinding.cardMinigame.setOnClickListener(this::showPopupMenu);

        // onClick for creating an alarm
        createAlarmBinding.createAlarm.setOnClickListener(v1 -> {
            // if alarm isn't null we can update the alarm, otherwise we schedule a new alarm
            if(alarm!=null) {
                updateAlarm();
            }
            else{
                scheduleAlarm();
            }

            Navigation.findNavController(v1).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
        });

        // onClick for selecting an alarm sound
        createAlarmBinding.cardAlarmSound.setOnClickListener(view -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(tone));
            startActivityForResult(intent, 5);
        });

        createAlarmBinding.vibrationSwitch.setOnCheckedChangeListener((compoundButton, b) -> isVibrate = b);
        createAlarmBinding.extraLoudSwitch.setOnCheckedChangeListener((compoundButton, b) -> isMaxVolume = b);

        return v;
    }

    /**
     * Method to show a popup menu to choose a game
     * @param view current view
     */
    private void showPopupMenu(View view) {
        // Create PopupMenu using the view where the menu will appear
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);

        // Inflate the menu from the XML file using the activity's MenuInflater
        requireActivity().getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());

        // Set listener for menu item selection
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.minigame1) {
                Toast.makeText(getActivity(), "Bomb Defusal selected", Toast.LENGTH_SHORT).show();
                createAlarmBinding.minigameText.setText(R.string.bomb_defusal);
                minigame = "Bomb Defusal";
                return true;
            }
            return false;
        });

        // Show the PopupMenu
        popupMenu.show();
    }

    /**
     * Method used to schedule an alarm.
     */
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
                isVibrate,
                minigame,
                isMaxVolume
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(requireContext());
        Log.d("CreateAlarm", "minigame: " + minigame);
    }

    /**
     * Method to update the alarm
     */
    private void updateAlarm(){
        String alarmName = getString(R.string.alarm_name);
        if(!createAlarmBinding.createAlarmName.getText().toString().isEmpty()){
            alarmName = createAlarmBinding.createAlarmName.getText().toString();
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
                isVibrate,
                minigame,
                isMaxVolume
        );
        createAlarmViewModel.update(updatedAlarm);
        updatedAlarm.schedule(requireContext());
    }

    // Is called when user selects a ringtone to use for the alarm sound
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

    /**
     * This method is used to ensure that when updating an existing alarm,
     * the create alarm fragment shows the saved information of the existing alarm.
     * @param alarm The alarm the user is currently updating
     */
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
            if(alarm.isMaxVolume())
                createAlarmBinding.extraLoudSwitch.setChecked(true);
        }
        createAlarmBinding.minigameText.setText(alarm.getAlarmMinigame());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createAlarmBinding = null;
    }
}