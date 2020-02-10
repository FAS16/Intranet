package com.fahadali.intranet.ui.schedule;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fahadali.intranet.R;
import com.fahadali.intranet.activities.AttendanceRegistrationActivity;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;
import com.fahadali.intranet.other.App;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayScheduleFragment extends Fragment implements ScheduleAdapter.OnLessonListener {


    private static final String TAG = "DayScheduleFragment";

    private View root;
    private TextView noLessonsTextView;


    public DayScheduleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        root = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        initRecyclerView();

        noLessonsTextView = root.findViewById(R.id.no_lessons_today);

        if(Student.getInstance().get_class().getCoursesOfTheDay(App.getDayOfWeek()).isEmpty()) {

            noLessonsTextView.setVisibility(View.VISIBLE);

        }

        return root;
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called.");

        RecyclerView recyclerView = root.findViewById(R.id.dayScheduleRecyclerView);
        ScheduleAdapter adapter = new ScheduleAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onLessonClick(int lessonId, String roomId) {
        Intent i = new Intent(getContext(), AttendanceRegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("lessonId", lessonId);
        bundle.putString("roomId", roomId);
        i.putExtras(bundle);
        startActivity(i);
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
