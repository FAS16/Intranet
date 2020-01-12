package com.fahadali.intranet.ui.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fahadali.intranet.R;
import com.fahadali.intranet.model.Course;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.other.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private static final String TAG = "ScheduleAdapter";
    private ArrayList<Course> todaysCourses;
    private Context context;
    private OnLessonListener onLessonListener;

    public ScheduleAdapter(Context context, OnLessonListener onLessonListener) {
        this.context = context;
        this.onLessonListener = onLessonListener;
        int dayOfWeek = App.getDayOfWeek();
        todaysCourses = Student.getInstance().get_class().getCoursesOfTheDay(dayOfWeek);


        Collections.sort(todaysCourses, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                return c1.getLessons().get(0).getStartTimeDate().compareTo(c2.getLessons().get(0).getStartTimeDate());
            }
        });

    }

    // Responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onLessonListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Course course = todaysCourses.get(position);

            holder.courseTitle.setText(course.getTitle());
            String timing = course.getLessons().get(0).getStartTime() + " -\n" + course.getLessons().get(0).getEndTime();
            holder.courseTiming.setText(timing);
            holder.roomNumber.setText("Lokale " + course.getLessons().get(0).getClassRoom().getId());
            holder.courseNoteImage.setImageResource(R.drawable.ic_note_green_24dp);
            holder.nameOfTeacher.setText("Underviser: "+course.getTeacher().getName());
            holder.lessonId = course.getLessons().get(0).getId();
            holder.roomId = course.getLessons().get(0).getClassRoom().getRoomIdentifier();

    }

    @Override
    public int getItemCount() {
        return todaysCourses.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView courseTiming;
        private TextView courseTitle;
        private TextView roomNumber;
        private TextView nameOfTeacher;
        private ImageView courseNoteImage;
        private int lessonId;
        private String roomId;
        RelativeLayout parentLayout;

        OnLessonListener onLessonListener;

        public ViewHolder(@NonNull View itemView, OnLessonListener onLessonListener) {
            super(itemView);

            nameOfTeacher = itemView.findViewById(R.id.nameOfTeacher);
            courseTiming = itemView.findViewById(R.id.courseTiming);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            roomNumber = itemView.findViewById(R.id.roomNumber);
            nameOfTeacher = itemView.findViewById(R.id.nameOfTeacher);
            courseNoteImage = itemView.findViewById(R.id.courseNoteImage);
            parentLayout = itemView.findViewById(R.id.list_item_layout);
            lessonId = 0;
            this.onLessonListener = onLessonListener;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            App.shortToast(context, "Clicked on lesson with id = " + lessonId);
            onLessonListener.onLessonClick(lessonId, roomId);
        }
    }

    public interface OnLessonListener{
        void onLessonClick(int lessonId, String roomId);
    }
}
