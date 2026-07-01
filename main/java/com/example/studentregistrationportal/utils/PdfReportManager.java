package com.example.studentregistrationportal.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.example.studentregistrationportal.models.Attendance;
import com.example.studentregistrationportal.models.Grade;
import com.example.studentregistrationportal.models.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfReportManager {

    private Context context;

    public PdfReportManager(Context context) {
        this.context = context;
    }

    public void generateTranscript(Student student, List<Grade> grades) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Title
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);
        canvas.drawText("ACADEMIC TRANSCRIPT", 200, 50, paint);

        // Student Info
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(12);
        canvas.drawText("Name: " + student.getFullName(), 50, 100, paint);
        canvas.drawText("Reg No: " + student.getRegistrationNumber(), 50, 120, paint);
        canvas.drawText("Course: " + student.getCourse(), 50, 140, paint);

        // Table Header
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Course Code", 50, 180, paint);
        canvas.drawText("Course Name", 150, 180, paint);
        canvas.drawText("Grade", 400, 180, paint);
        canvas.drawText("GP", 480, 180, paint);

        // Table Lines
        canvas.drawLine(50, 185, 550, 185, paint);

        // Grades
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int y = 205;
        for (Grade grade : grades) {
            canvas.drawText(grade.getCourseCode(), 50, y, paint);
            canvas.drawText(grade.getCourseName(), 150, y, paint);
            canvas.drawText(grade.getGrade(), 400, y, paint);
            canvas.drawText(String.valueOf(grade.getGradePoint()), 480, y, paint);
            y += 20;
        }

        // Summary
        y += 20;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        double totalPoints = 0;
        int totalCredits = 0;
        for (Grade g : grades) {
            totalPoints += g.getGradePoint() * g.getCreditHours();
            totalCredits += g.getCreditHours();
        }
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        
        canvas.drawText("Cumulative GPA: " + String.format(java.util.Locale.US, "%.2f", gpa), 50, y, paint);
        y += 20;
        canvas.drawText("Classification: " + getClassification(gpa), 50, y, paint);

        document.finishPage(page);
        saveDocument(document, "Transcript_" + student.getRegistrationNumber() + ".pdf");
    }

    private String getClassification(double gpa) {
        if (gpa >= 3.7) return "First Class Honors";
        if (gpa >= 3.0) return "Second Class Honors (Upper Division)";
        if (gpa >= 2.3) return "Second Class Honors (Lower Division)";
        if (gpa >= 2.0) return "Pass";
        return "Fail";
    }

    public void generateAttendanceReport(Student student, List<Attendance> attendanceList) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);
        canvas.drawText("ATTENDANCE REPORT", 200, 50, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(12);
        canvas.drawText("Student: " + student.getFullName(), 50, 100, paint);
        canvas.drawText("Reg No: " + student.getRegistrationNumber(), 50, 120, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Date", 50, 160, paint);
        canvas.drawText("Course", 150, 160, paint);
        canvas.drawText("Status", 450, 160, paint);

        canvas.drawLine(50, 165, 550, 165, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int y = 185;
        for (Attendance attendance : attendanceList) {
            canvas.drawText(attendance.getDate(), 50, y, paint);
            canvas.drawText(attendance.getCourseCode(), 150, y, paint);
            canvas.drawText(attendance.getStatus(), 450, y, paint);
            y += 20;
            if (y > 800) break; // Simple page limit for sample
        }

        document.finishPage(page);
        saveDocument(document, "Attendance_" + student.getRegistrationNumber() + ".pdf");
    }

    private void saveDocument(PdfDocument document, String fileName) {
        File filePath = new File(context.getExternalFilesDir(null), fileName);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(context, "Report saved to: " + filePath.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error generating report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        document.close();
    }
}