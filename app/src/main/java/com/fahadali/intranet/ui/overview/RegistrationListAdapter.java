package com.fahadali.intranet.ui.overview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fahadali.intranet.R;
import com.fahadali.intranet.activities.LoginActivity;
import com.fahadali.intranet.activities.Main2Activity;
import com.fahadali.intranet.model.Attendance;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;
import com.fahadali.intranet.other.App;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationListAdapter extends RecyclerView.Adapter<RegistrationListAdapter.ViewHolder> {

    private static final String TAG = "RegistrationListAdapter";
    private ArrayList<Attendance> registrations;
    private Context context;

    public RegistrationListAdapter(Context context) {
        this.context = context;
        registrations = Student.getInstance().getAttendances();



    }

    // Responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Attendance attendance = registrations.get(position);

            holder.courseName.setText(attendance.getLesson().getCourse().getTitle());
//            String time = App.formatDateString(attendance.getTimestamp());
            holder.timeOfRegistration.setText("Registreret: "+attendance.getTimestamp());
            String lStart = App.formatDate(attendance.getLesson().getStartTimeDate());
            String lEnd = App.formatDate(attendance.getLesson().getEndTimeDate());
            holder.lectureStart.setText("Lektion start: "+lStart);
            holder.lectureEnd.setText("Lektion slut: "+lEnd);
            int checkType = attendance.getCheckType();

            if(checkType == 1) {
                holder.checkTypeImage.setImageResource(R.drawable.ic_check_out_24dp);
            }

    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView courseName;
        private TextView timeOfRegistration;
        private TextView lectureStart;
        private TextView lectureEnd;
        private ImageView checkTypeImage;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.course_name);
            timeOfRegistration = itemView.findViewById(R.id.timeOfRegistration);
            lectureStart = itemView.findViewById(R.id.lectureStart);
            lectureEnd = itemView.findViewById(R.id.lectureEnd);
            checkTypeImage = itemView.findViewById(R.id.checkTypeImage);
            parentLayout = itemView.findViewById(R.id.list_item_ov_layout);

        }
    }

}
