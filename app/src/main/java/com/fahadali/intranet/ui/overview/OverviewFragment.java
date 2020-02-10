package com.fahadali.intranet.ui.overview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fahadali.intranet.R;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;
import com.fahadali.intranet.other.App;
import com.fahadali.intranet.ui.schedule.ScheduleAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    OverviewFragment extends Fragment {

    private OverviewViewModel dashboardViewModel;
    private static final String TAG = "OverviewFragment";
    private View root;
    private ProgressBar pbar;
    private TextView noRegistrationsTextView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: started.");

         root = inflater.inflate(R.layout.fragment_overview, container, false);
         pbar = root.findViewById(R.id.pbar_ov);
         retrieveStudentData(App.instance.getTokenFromSharedPrefs());

        noRegistrationsTextView = root.findViewById(R.id.no_reg_today);




        return root;
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called.");

        RecyclerView recyclerView = root.findViewById(R.id.overview_recyclerview);
        RegistrationListAdapter adapter = new RegistrationListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        pbar.setVisibility(View.GONE);

    }

    private void retrieveStudentData(Token token) {

        Call<Student> call = App.instance.getUserClient().getStudent(token.getUsername(), token.getBearerToken());

        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if(response.isSuccessful()) {
                    Student s = response.body();

                    Student.getInstance().setStudent(s);

                    Log.i(TAG, "onResponse: Student retrieved = " + Student.getInstance().toString());
                    initRecyclerView();
                    if(Student.getInstance().getAttendances().isEmpty()) {

                        noRegistrationsTextView.setVisibility(View.VISIBLE);

                    }

                }
                else {
                    Log.i(TAG, "onResponse: Response not successful. Code = " + response.body());
                }


            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed to retrieve student data. Message = " + t.getMessage());


            }
        });


    }
}