package com.example.studentregistrationportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.models.Attendance;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private Context context;
    private List<Attendance> attendanceList;

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.tvCourseName.setText(attendance.getCourseName());
        holder.tvCourseCode.setText(attendance.getCourseCode());
        holder.tvDate.setText(attendance.getDate());
        holder.tvStatus.setText(attendance.getStatus());

        // Set color based on status
        int color = android.R.color.darker_gray;
        switch (attendance.getStatus().toLowerCase()) {
            case "present":
                color = android.R.color.holo_green_dark;
                break;
            case "absent":
                color = android.R.color.holo_red_dark;
                break;
            case "late":
                color = android.R.color.holo_orange_dark;
                break;
        }
        holder.tvStatus.setTextColor(context.getResources().getColor(color));

        if (attendance.getLecturer() != null) {
            holder.tvLecturer.setText("Lecturer: " + attendance.getLecturer());
            holder.tvLecturer.setVisibility(View.VISIBLE);
        } else {
            holder.tvLecturer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvDate, tvStatus, tvLecturer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLecturer = itemView.findViewById(R.id.tvLecturer);
        }
    }
}