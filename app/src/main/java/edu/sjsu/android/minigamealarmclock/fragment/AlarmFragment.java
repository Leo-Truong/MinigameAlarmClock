package edu.sjsu.android.minigamealarmclock.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.sjsu.android.minigamealarmclock.model.Alarm;
import edu.sjsu.android.minigamealarmclock.adapter.MyAlarmRecyclerViewAdapter;
import edu.sjsu.android.minigamealarmclock.R;
import edu.sjsu.android.minigamealarmclock.databinding.FragmentAlarmListBinding;
import edu.sjsu.android.minigamealarmclock.util.OnToggleAlarmListener;
import edu.sjsu.android.minigamealarmclock.viewmodel.AlarmsListViewModel;

/**
 * A fragment representing a list of Items.
 */
public class AlarmFragment extends Fragment implements OnToggleAlarmListener {
    private MyAlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private FloatingActionButton addAlarm;
    private FragmentAlarmListBinding fragmentAlarmsListBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlarmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmRecyclerViewAdapter = new MyAlarmRecyclerViewAdapter(this);
        alarmsListViewModel = new ViewModelProvider(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setAlarms(alarms);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAlarmsListBinding = FragmentAlarmListBinding.inflate(inflater,container,false);
        View view = fragmentAlarmsListBinding.getRoot();

        alarmsRecyclerView = fragmentAlarmsListBinding.fragmentListalarmsRecylerView;
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);

        addAlarm = fragmentAlarmsListBinding.fragmentListalarmsAddAlarm;
        addAlarm.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment));

        return view;
    }

    /**
     * Method used when the switch is toggled. When toggled
     * the user can cancel the alarm or schedule alarm. This
     * will be reflected in the database
     * @param alarm current alarm
     */
    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(requireContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(requireContext());
            alarmsListViewModel.update(alarm);
        }
    }

    /**
     * When the alarm is deleted the method cancels the alarm
     * and removes the deleted alarm from the database.
     * @param alarm current alarm
     */
    @Override
    public void onDelete(Alarm alarm) {
        if (alarm.isStarted())
            alarm.cancelAlarm(requireContext());
        alarmsListViewModel.delete(alarm.getAlarmId());
    }

    /**
     * Method used when an alarm on the alarm list is clicked.
     * The method cancels the current alarm and navigates the
     * user to the createAlarmFragment.
     * @param alarm current alarm
     * @param view current view
     */
    @Override
    public void onItemClick(Alarm alarm,View view) {
        if (alarm.isStarted())
            alarm.cancelAlarm(requireContext());
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.arg_alarm_obj),alarm);
        Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment,args);
    }
}