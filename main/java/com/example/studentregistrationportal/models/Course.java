package com.example.studentregistrationportal.models;

public class Course {
    private int id;
    private String courseCode;
    private String courseName;
    private String description;
    private String instructor;
    private int creditHours;
    private String department;

    public Course() {}

    public Course(int id, String courseCode, String courseName,
                  String description, String instructor, int creditHours, String department) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.creditHours = creditHours;
        this.department = department;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}