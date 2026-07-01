package com.example.studentregistrationportal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Attendance;
import com.example.studentregistrationportal.models.Grade;
import com.example.studentregistrationportal.models.Student;
import com.example.studentregistrationportal.utils.PdfReportManager;

import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private PdfReportManager pdfManager;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        studentId = getIntent().getIntExtra("studentId", -1);
        if (studentId == -1) {
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        pdfManager = new PdfReportManager(this);
        initViews();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        ImageButton btnBack = findViewById(R.id.btnBack);
        
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnTranscript).setOnClickListener(v -> generateTranscript());
        findViewById(R.id.btnAttendanceReport).setOnClickListener(v -> generateAttendance());
        findViewById(R.id.btnRegistrationSlip).setOnClickListener(v -> generateRegistrationSlip());
    }

    private void generateTranscript() {
        Intent intent = new Intent(this, ReportPreviewActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra(ReportPreviewActivity.EXTRA_REPORT_TYPE, ReportPreviewActivity.TYPE_TRANSCRIPT);
        startActivity(intent);
    }

    private void generateAttendance() {
        Intent intent = new Intent(this, ReportPreviewActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra(ReportPreviewActivity.EXTRA_REPORT_TYPE, ReportPreviewActivity.TYPE_ATTENDANCE);
        startActivity(intent);
    }

    private void generateRegistrationSlip() {
        // For sample, we'll reuse transcript logic or create a simple one
        Toast.makeText(this, "Registration Slip generation coming soon", Toast.LENGTH_SHORT).show();
    }
}