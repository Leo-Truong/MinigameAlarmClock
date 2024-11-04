package edu.sjsu.android.minigamealarmclock;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import edu.sjsu.android.minigamealarmclock.databinding.FragmentAlarmBinding;

import java.util.ArrayList;
import java.util.List;

public class MyAlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private List<Alarm> alarms;
    private OnToggleAlarmListener listener;
    private FragmentAlarmBinding itemAlarmBinding;

    public MyAlarmRecyclerViewAdapter(OnToggleAlarmListener listener) {
        this.alarms = new ArrayList<>(); // Initialize alarms to an empty list
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemAlarmBinding = FragmentAlarmBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AlarmViewHolder(itemAlarmBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.bind(alarm, listener);
    }

    @Override
    public int getItemCount() {
        return (alarms != null) ? alarms.size() : 0;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms != null ? alarms : new ArrayList<>();
        notifyDataSetChanged();
    }
}