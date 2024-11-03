package edu.sjsu.android.minigamealarmclock;

import android.media.Ringtone;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sjsu.android.minigamealarmclock.databinding.FragmentCreateAlarmBinding;

public class CreateAlarm extends Fragment {
    FragmentCreateAlarmBinding createAlarmBinding;
    boolean isVibrate = false;
    String tone;
    Alarm alarm;
    Ringtone ringtone;

    public CreateAlarm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_alarm, container, false);
    }
}