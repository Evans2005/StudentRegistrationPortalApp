package com.example.studentregistrationportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.models.Grade;

import java.text.DecimalFormat;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private Context context;
    private List<Grade> grades;
    private DecimalFormat df = new DecimalFormat("#.##");

    public GradeAdapter(Context context, List<Grade> grades) {
        this.context = context;
        this.grades = grades;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.tvCourseName.setText(grade.getCourseName());
        holder.tvCourseCode.setText(grade.getCourseCode());
        holder.tvScore.setText("Score: " + df.format(grade.getScore()) + "%");
        holder.tvGrade.setText(grade.getGrade());
        holder.tvCreditHours.setText(grade.getCreditHours() + " Credits");
        holder.tvGradePoint.setText("GP: " + df.format(grade.getGradePoint()));
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvScore, tvGrade, tvCreditHours, tvGradePoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvGrade = itemView.findViewById(R.id.tvGrade);
            tvCreditHours = itemView.findViewById(R.id.tvCreditHours);
            tvGradePoint = itemView.findViewById(R.id.tvGradePoint);
        }
    }
}