package com.fahadali.intranet.ui.schedule;


import android.media.DrmInitData;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahadali.intranet.R;
import com.fahadali.intranet.model.Course;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayScheduleFragment extends Fragment {


    private static final String TAG = "DayScheduleFragment";

    private ArrayList<Course> courses;
    private View root;


    public DayScheduleFragment() {
        courses = new ArrayList<>(); // FIll with data from backend when ready
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        root = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        initRecyclerView();

        return root;
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called.");

        RecyclerView recyclerView = root.findViewById(R.id.dayScheduleRecyclerView);
        ScheduleAdapter adapter = new ScheduleAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }

}
