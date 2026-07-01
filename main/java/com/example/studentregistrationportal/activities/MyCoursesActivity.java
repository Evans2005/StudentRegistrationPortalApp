package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.CourseAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Course;

import java.util.List;

public class MyCoursesActivity extends AppCompatActivity {
    private RecyclerView rvCourses;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

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
        loadCourses();
    }

    private void initViews() {
        rvCourses = findViewById(R.id.rvCourses);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressBar);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadCourses() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Course> courses = dbHelper.getRegisteredCourses(studentId);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (courses != null && !courses.isEmpty()) {
                    CourseAdapter adapter = new CourseAdapter(this, courses,
                            course -> {
                                // Navigate to course details
                                Toast.makeText(MyCoursesActivity.this,
                                        course.getCourseName(),
                                        Toast.LENGTH_SHORT).show();
                            });
                    rvCourses.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                    rvCourses.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvCourses.setVisibility(View.GONE);
                }
            });
        }).start();
    }
}