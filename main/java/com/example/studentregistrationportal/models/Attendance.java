package com.example.studentregistrationportal.models;

public class Attendance {
    private int id;
    private int studentId;
    private int courseId;
    private String courseCode;
    private String courseName;
    private String date;
    private String status; // Present, Absent, Late
    private String time;
    private String lecturer;

    public Attendance() {}

    public Attendance(int id, int studentId, int courseId, String courseCode,
                      String courseName, String date, String status,
                      String time, String lecturer) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.date = date;
        this.status = status;
        this.time = time;
        this.lecturer = lecturer;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }
}