package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.GradeAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Grade;

import java.text.DecimalFormat;
import java.util.List;

public class GradesActivity extends AppCompatActivity {
    private RecyclerView rvGrades;
    private TextView tvGPA, tvCGPA, tvTotalCredits, tvEmpty, tvClassification;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        studentId = getIntent().getIntExtra("studentId", -1);
        if (studentId == -1) {
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadGrades();
    }

    private void initViews() {
        rvGrades = findViewById(R.id.rvGrades);
        tvGPA = findViewById(R.id.tvGPA);
        tvCGPA = findViewById(R.id.tvCGPA);
        tvTotalCredits = findViewById(R.id.tvTotalCredits);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvClassification = findViewById(R.id.tvClassification);
        progressBar = findViewById(R.id.progressBar);
        rvGrades.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadGrades() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Grade> grades = dbHelper.getStudentGrades(studentId);
            double gpa = dbHelper.calculateGPA(studentId);
            double cgpa = dbHelper.calculateCGPA(studentId);

            int totalCredits = 0;
            for (Grade grade : grades) {
                totalCredits += grade.getCreditHours();
            }

            final int finalTotalCredits = totalCredits;
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (grades != null && !grades.isEmpty()) {
                    GradeAdapter adapter = new GradeAdapter(this, grades);
                    rvGrades.setAdapter(adapter);
                    tvGPA.setText(df.format(gpa));
                    tvCGPA.setText(df.format(cgpa));
                    tvTotalCredits.setText(String.valueOf(finalTotalCredits));
                    tvClassification.setText("Classification: " + getClassification(cgpa));
                    tvEmpty.setVisibility(View.GONE);
                    rvGrades.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvGrades.setVisibility(View.GONE);
                    tvGPA.setText("N/A");
                    tvCGPA.setText("N/A");
                    tvTotalCredits.setText("0");
                    tvClassification.setText("Classification: N/A");
                }
            });
        }).start();
    }

    private String getClassification(double gpa) {
        if (gpa >= 3.7) return "First Class Honors";
        if (gpa >= 3.0) return "Second Class Honors (Upper Division)";
        if (gpa >= 2.3) return "Second Class Honors (Lower Division)";
        if (gpa >= 2.0) return "Pass";
        return "Fail";
    }
}