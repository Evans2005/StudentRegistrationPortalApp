package com.example.studentregistrationportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.models.Course;

import java.util.List;

public class CourseRegistrationAdapter extends RecyclerView.Adapter<CourseRegistrationAdapter.ViewHolder> {
    private Context context;
    private List<Course> courses;
    private OnRegisterClickListener listener;

    public interface OnRegisterClickListener {
        void onRegister(Course course);
    }

    public CourseRegistrationAdapter(Context context, List<Course> courses, OnRegisterClickListener listener) {
        this.context = context;
        this.courses = courses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_registration, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvCourseCode.setText(course.getCourseCode());
        holder.tvInstructor.setText("Instructor: " + course.getInstructor());
        holder.tvCredits.setText(course.getCreditHours() + " Credits");
        holder.tvDepartment.setText(course.getDepartment());

        holder.btnRegister.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRegister(course);
                holder.btnRegister.setEnabled(false);
                holder.btnRegister.setText("Registered");
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvInstructor, tvCredits, tvDepartment;
        Button btnRegister;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvCredits = itemView.findViewById(R.id.tvCredits);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            btnRegister = itemView.findViewById(R.id.btnRegister);
        }
    }
}