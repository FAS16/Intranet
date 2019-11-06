package com.fahadali.intranet.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fahadali.intranet.R;
import com.fahadali.intranet.other.App;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel scheduleViewModel;
    private Button dateButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        dateButton = root.findViewById(R.id.dateButton);
        dateButton.setText(String.format("MAN %s", App.getTodaysDate()));

        Fragment dayScheduleFragment = new DayScheduleFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.dayScheduleContainer, dayScheduleFragment)
                .commit();

        return root;
    }



}











//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });