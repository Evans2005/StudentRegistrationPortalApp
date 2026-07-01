package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.TimetableAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Timetable;

import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    private RecyclerView rvTimetable;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

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
        loadTimetable();
    }

    private void initViews() {
        rvTimetable = findViewById(R.id.rvTimetable);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressBar);
        rvTimetable.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTimetable() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Timetable> timetable = dbHelper.getStudentTimetable(studentId);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (timetable != null && !timetable.isEmpty()) {
                    TimetableAdapter adapter = new TimetableAdapter(this, timetable);
                    rvTimetable.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                    rvTimetable.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvTimetable.setVisibility(View.GONE);
                }
            });
        }).start();
    }
}