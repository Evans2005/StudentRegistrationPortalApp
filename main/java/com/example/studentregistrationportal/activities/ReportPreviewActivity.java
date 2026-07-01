package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Attendance;
import com.example.studentregistrationportal.models.Grade;
import com.example.studentregistrationportal.models.Student;
import com.example.studentregistrationportal.utils.PdfReportManager;

import java.util.List;

public class ReportPreviewActivity extends AppCompatActivity {

    public static final String EXTRA_REPORT_TYPE = "report_type";
    public static final String TYPE_TRANSCRIPT = "transcript";
    public static final String TYPE_ATTENDANCE = "attendance";

    private TextView tvReportContent;
    private Button btnDownload;
    private DatabaseHelper dbHelper;
    private PdfReportManager pdfManager;
    private int studentId;
    private String reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_preview);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        studentId = getIntent().getIntExtra("studentId", -1);
        reportType = getIntent().getStringExtra(EXTRA_REPORT_TYPE);

        if (studentId == -1 || reportType == null) {
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        pdfManager = new PdfReportManager(this);

        initViews();
        generatePreview();
    }

    private void initViews() {
        tvReportContent = findViewById(R.id.tvReportContent);
        btnDownload = findViewById(R.id.btnDownload);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnDownload.setOnClickListener(v -> downloadPdf());
    }

    private void generatePreview() {
        new Thread(() -> {
            Student student = dbHelper.getStudentById(studentId);
            StringBuilder content = new StringBuilder();

            if (student != null) {
                content.append("STUDENT REPORT\n");
                content.append("==============================\n");
                content.append("Name: ").append(student.getFullName()).append("\n");
                content.append("Reg No: ").append(student.getRegistrationNumber()).append("\n");
                content.append("Course: ").append(student.getCourse()).append("\n\n");

                if (TYPE_TRANSCRIPT.equals(reportType)) {
                    content.append("ACADEMIC TRANSCRIPT\n");
                    content.append("------------------------------\n");
                    List<Grade> grades = dbHelper.getStudentGrades(studentId);
                    content.append(String.format("%-10s %-20s %-5s\n", "CODE", "NAME", "GRADE"));
                    for (Grade g : grades) {
                        String name = g.getCourseName();
                        if (name.length() > 18) name = name.substring(0, 15) + "...";
                        content.append(String.format("%-10s %-20s %-5s\n", 
                                g.getCourseCode(), name, g.getGrade()));
                    }

                    double gpa = calculateGPA(grades);
                    content.append("\nCumulative GPA: ").append(String.format(java.util.Locale.US, "%.2f", gpa)).append("\n");
                    content.append("Classification: ").append(getClassification(gpa)).append("\n");
                } else if (TYPE_ATTENDANCE.equals(reportType)) {
                    content.append("ATTENDANCE SUMMARY\n");
                    content.append("------------------------------\n");
                    List<Attendance> attendances = dbHelper.getStudentAttendance(studentId);
                    content.append(String.format("%-12s %-10s %-10s\n", "DATE", "CODE", "STATUS"));
                    for (Attendance a : attendances) {
                        content.append(String.format("%-12s %-10s %-10s\n", 
                                a.getDate(), a.getCourseCode(), a.getStatus()));
                    }
                }
            }

            runOnUiThread(() -> tvReportContent.setText(content.toString()));
        }).start();
    }

    private double calculateGPA(List<Grade> grades) {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Grade g : grades) {
            totalPoints += g.getGradePoint() * g.getCreditHours();
            totalCredits += g.getCreditHours();
        }
        return totalCredits > 0 ? totalPoints / totalCredits : 0;
    }

    private String getClassification(double gpa) {
        if (gpa >= 3.7) return "First Class Honors";
        if (gpa >= 3.0) return "Second Class Honors (Upper Division)";
        if (gpa >= 2.3) return "Second Class Honors (Lower Division)";
        if (gpa >= 2.0) return "Pass";
        return "Fail";
    }

    private void downloadPdf() {
        new Thread(() -> {
            Student student = dbHelper.getStudentById(studentId);
            if (student == null) return;

            if (TYPE_TRANSCRIPT.equals(reportType)) {
                List<Grade> grades = dbHelper.getStudentGrades(studentId);
                runOnUiThread(() -> pdfManager.generateTranscript(student, grades));
            } else if (TYPE_ATTENDANCE.equals(reportType)) {
                List<Attendance> attendances = dbHelper.getStudentAttendance(studentId);
                runOnUiThread(() -> pdfManager.generateAttendanceReport(student, attendances));
            }
        }).start();
    }
}