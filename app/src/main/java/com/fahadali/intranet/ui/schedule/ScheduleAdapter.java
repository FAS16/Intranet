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
import com.fahadali.intranet.model.Subject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private static final String TAG = "ScheduleAdapter";
    private ArrayList<Subject> courses;
    private Context context;

    public ScheduleAdapter(Context context) {
        this.context = context;

        // Dummy data
        this.courses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String note = "";
            if(i % 2 == 0) {

                note = "Note omkring kursus "+(i+1);

            }
            courses.add(new Subject(
                    i+1,
                    "Titel"+(i+1),
                    note
                    ));
        }
    }

    // Responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        String title = courses.get(position).getTitle();
        String note = courses.get(position).getNote();
        holder.courseTiming.setText(String.format("%s - %s", "10:00", "11:00"));
        holder.courseTitle.setText(title);


        if(!note.trim().isEmpty()) {
                holder.courseNoteImage.setImageResource(R.drawable.ic_note_green_24dp);
                note.replace("x", "");
                holder.coursNote.setText(note);

        }

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTiming;
        private TextView courseTitle;
        private TextView coursNote;
        private ImageView courseNoteImage;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseTiming = itemView.findViewById(R.id.courseTiming);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            coursNote = itemView.findViewById(R.id.courseNote);
            courseNoteImage = itemView.findViewById(R.id.courseNoteImage);
            parentLayout = itemView.findViewById(R.id.list_item_layout);


        }
    }
}
