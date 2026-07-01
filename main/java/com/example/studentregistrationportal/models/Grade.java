package com.example.studentregistrationportal.models;

public class Grade {
    private int id;
    private int studentId;
    private int courseId;
    private String courseCode;
    private String courseName;
    private double score;
    private String grade;
    private int creditHours;
    private double gradePoint;

    public Grade() {}

    public Grade(int id, int studentId, int courseId, String courseCode,
                 String courseName, double score, String grade,
                 int creditHours, double gradePoint) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.score = score;
        this.grade = grade;
        this.creditHours = creditHours;
        this.gradePoint = gradePoint;
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

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public double getGradePoint() { return gradePoint; }
    public void setGradePoint(double gradePoint) { this.gradePoint = gradePoint; }
}