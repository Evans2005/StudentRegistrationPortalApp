package com.example.studentregistrationportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.models.Timetable;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private Context context;
    private List<Timetable> timetable;

    public TimetableAdapter(Context context, List<Timetable> timetable) {
        this.context = context;
        this.timetable = timetable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Timetable item = timetable.get(position);
        holder.tvCourseName.setText(item.getCourseName());
        holder.tvCourseCode.setText(item.getCourseCode());
        holder.tvDay.setText(item.getDay());
        holder.tvTime.setText(item.getStartTime() + " - " + item.getEndTime());
        holder.tvRoom.setText("Room: " + item.getRoom());
        holder.tvInstructor.setText("Instructor: " + item.getInstructor());

        if (item.getSemester() != null) {
            holder.tvSemester.setText(item.getSemester());
            holder.tvSemester.setVisibility(View.VISIBLE);
        } else {
            holder.tvSemester.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return timetable.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvDay, tvTime, tvRoom, tvInstructor, tvSemester;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvSemester = itemView.findViewById(R.id.tvSemester);
        }
    }
}