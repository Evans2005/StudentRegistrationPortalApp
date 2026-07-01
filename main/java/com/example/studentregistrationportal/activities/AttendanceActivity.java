package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.AttendanceAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Attendance;

import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    private RecyclerView rvAttendance;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

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
        loadAttendance();
    }

    private void initViews() {
        rvAttendance = findViewById(R.id.rvAttendance);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressBar);
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadAttendance() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Attendance> attendanceList = dbHelper.getStudentAttendance(studentId);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (attendanceList != null && !attendanceList.isEmpty()) {
                    AttendanceAdapter adapter = new AttendanceAdapter(this, attendanceList);
                    rvAttendance.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                    rvAttendance.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvAttendance.setVisibility(View.GONE);
                }
            });
        }).start();
    }
}