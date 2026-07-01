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
import com.example.studentregistrationportal.adapters.CourseRegistrationAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Course;

import java.util.List;
import java.util.ArrayList;

public class CourseRegistrationActivity extends AppCompatActivity {
    private RecyclerView rvAvailableCourses;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;
    private List<Course> availableCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registration);

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
        rvAvailableCourses = findViewById(R.id.rvAvailableCourses);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressBar);
        rvAvailableCourses.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadCourses() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Course> allCourses = dbHelper.getAllCourses();
            List<Course> registeredCourses = dbHelper.getRegisteredCourses(studentId);

            // Filter out already registered courses
            availableCourses = new ArrayList<>();
            for (Course course : allCourses) {
                boolean isRegistered = false;
                for (Course registered : registeredCourses) {
                    if (registered.getId() == course.getId()) {
                        isRegistered = true;
                        break;
                    }
                }
                if (!isRegistered) {
                    availableCourses.add(course);
                }
            }

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (availableCourses != null && !availableCourses.isEmpty()) {
                    CourseRegistrationAdapter adapter = new CourseRegistrationAdapter(
                            this, availableCourses,
                            course -> registerCourse(course));
                    rvAvailableCourses.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                    rvAvailableCourses.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvAvailableCourses.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private void registerCourse(Course course) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            boolean success = dbHelper.registerCourse(studentId, course.getId());
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (success) {
                    Toast.makeText(CourseRegistrationActivity.this,
                            "Registered for " + course.getCourseName(),
                            Toast.LENGTH_SHORT).show();
                    // Refresh the list
                    loadCourses();
                } else {
                    Toast.makeText(CourseRegistrationActivity.this,
                            "Failed to register for " + course.getCourseName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}