package com.example.studentregistrationportal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.AnnouncementAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Announcement;
import com.example.studentregistrationportal.models.Student;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvStudentName, tvRegistrationNumber, tvCourse;
    private RecyclerView rvAnnouncements;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Hide action bar for custom design
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        studentId = getIntent().getIntExtra("studentId", -1);
        if (studentId == -1) {
            SharedPreferences prefs = getSharedPreferences("StudentPortal", MODE_PRIVATE);
            studentId = prefs.getInt("studentId", -1);
            if (studentId == -1) {
                navigateToLogin();
                return;
            }
        }

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadStudentInfo();
        loadAnnouncements();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentInfo();
    }

    private void initViews() {
        tvStudentName = findViewById(R.id.tvStudentName);
        tvRegistrationNumber = findViewById(R.id.tvRegistrationNumber);
        tvCourse = findViewById(R.id.tvCourse);
        rvAnnouncements = findViewById(R.id.rvAnnouncements);

        rvAnnouncements.setLayoutManager(new LinearLayoutManager(this));
        rvAnnouncements.setNestedScrollingEnabled(false);
    }

    private void loadStudentInfo() {
        new Thread(() -> {
            Student student = dbHelper.getStudentById(studentId);
            runOnUiThread(() -> {
                if (student != null) {
                    tvStudentName.setText(student.getFullName());
                    tvRegistrationNumber.setText("Reg: " + student.getRegistrationNumber());
                    tvCourse.setText(student.getCourse());
                }
            });
        }).start();
    }

    private void loadAnnouncements() {
        new Thread(() -> {
            List<Announcement> announcements = dbHelper.getAllAnnouncements();
            runOnUiThread(() -> {
                // Show only latest 3 announcements
                List<Announcement> latest = announcements;
                if (announcements.size() > 3) {
                    latest = announcements.subList(0, 3);
                }

                AnnouncementAdapter adapter = new AnnouncementAdapter(
                        DashboardActivity.this, latest);
                rvAnnouncements.setAdapter(adapter);
            });
        }).start();
    }

    private void setupClickListeners() {
        // Menu button click listener
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> showPopupMenu(v));

        // Card click listeners
        findViewById(R.id.cardProfile).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardCourses).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MyCoursesActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardGrades).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, GradesActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardAttendance).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardTimetable).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TimetableActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardRegistration).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CourseRegistrationActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });

        findViewById(R.id.cardReports).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ReportsActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            return onOptionsItemSelected(item);
        });
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_announcements) {
            Intent intent = new Intent(DashboardActivity.this, AnnouncementsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_change_password) {
            Intent intent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("StudentPortal", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
