package com.example.studentregistrationportal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentregistrationportal.models.Announcement;
import com.example.studentregistrationportal.models.Attendance;
import com.example.studentregistrationportal.models.Course;
import com.example.studentregistrationportal.models.Grade;
import com.example.studentregistrationportal.models.Student;
import com.example.studentregistrationportal.models.Timetable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_portal.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_GRADES = "grades";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_TIMETABLE = "timetable";
    private static final String TABLE_ANNOUNCEMENTS = "announcements";
    private static final String TABLE_STUDENT_COURSES = "student_courses";

    // Common column names
    private static final String COLUMN_ID = "id";

    // Students columns
    private static final String COLUMN_REGISTRATION_NUMBER = "registration_number";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_COURSE = "course";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_DOB = "date_of_birth";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";

    // Courses columns
    private static final String COLUMN_COURSE_CODE = "course_code";
    private static final String COLUMN_COURSE_NAME = "course_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_INSTRUCTOR = "instructor";
    private static final String COLUMN_CREDIT_HOURS = "credit_hours";
    private static final String COLUMN_DEPARTMENT = "department";

    // Grades columns
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_COURSE_ID = "course_id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_GRADE = "grade";
    private static final String COLUMN_GRADE_POINT = "grade_point";

    // Attendance columns
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_LECTURER = "lecturer";

    // Timetable columns
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_ROOM = "room";
    private static final String COLUMN_SEMESTER = "semester";

    // Announcements columns
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_IMPORTANT = "important";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create students table
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REGISTRATION_NUMBER + " TEXT UNIQUE NOT NULL,"
                + COLUMN_FULL_NAME + " TEXT NOT NULL,"
                + COLUMN_COURSE + " TEXT NOT NULL,"
                + COLUMN_PHONE_NUMBER + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_PROFILE_IMAGE + " TEXT"
                + ")";
        db.execSQL(createStudentsTable);

        // Create courses table
        String createCoursesTable = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COURSE_CODE + " TEXT UNIQUE NOT NULL,"
                + COLUMN_COURSE_NAME + " TEXT NOT NULL,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_INSTRUCTOR + " TEXT NOT NULL,"
                + COLUMN_CREDIT_HOURS + " INTEGER NOT NULL,"
                + COLUMN_DEPARTMENT + " TEXT NOT NULL"
                + ")";
        db.execSQL(createCoursesTable);

        // Create grades table
        String createGradesTable = "CREATE TABLE " + TABLE_GRADES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + COLUMN_COURSE_ID + " INTEGER NOT NULL,"
                + COLUMN_SCORE + " REAL NOT NULL,"
                + COLUMN_GRADE + " TEXT NOT NULL,"
                + COLUMN_GRADE_POINT + " REAL NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(createGradesTable);

        // Create attendance table
        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + COLUMN_COURSE_ID + " INTEGER NOT NULL,"
                + COLUMN_DATE + " TEXT NOT NULL,"
                + COLUMN_STATUS + " TEXT NOT NULL,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_LECTURER + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(createAttendanceTable);

        // Create timetable table
        String createTimetableTable = "CREATE TABLE " + TABLE_TIMETABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COURSE_ID + " INTEGER NOT NULL,"
                + COLUMN_DAY + " TEXT NOT NULL,"
                + COLUMN_START_TIME + " TEXT NOT NULL,"
                + COLUMN_END_TIME + " TEXT NOT NULL,"
                + COLUMN_ROOM + " TEXT NOT NULL,"
                + COLUMN_INSTRUCTOR + " TEXT NOT NULL,"
                + COLUMN_SEMESTER + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(createTimetableTable);

        // Create announcements table
        String createAnnouncementsTable = "CREATE TABLE " + TABLE_ANNOUNCEMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT NOT NULL,"
                + COLUMN_CONTENT + " TEXT NOT NULL,"
                + COLUMN_DATE + " TEXT NOT NULL,"
                + COLUMN_AUTHOR + " TEXT NOT NULL,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_IMPORTANT + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(createAnnouncementsTable);

        // Create student_courses junction table
        String createStudentCoursesTable = "CREATE TABLE " + TABLE_STUDENT_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + COLUMN_COURSE_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(createStudentCoursesTable);

        // Insert sample data
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample courses
        insertCourse(db, "CS101", "Introduction to Computer Science",
                "Basic concepts of computer science", "Dr. Smith", 3, "Computer Science");
        insertCourse(db, "CS201", "Data Structures",
                "Advanced data structures and algorithms", "Prof. Johnson", 4, "Computer Science");
        insertCourse(db, "MATH101", "Calculus I",
                "Introduction to calculus and derivatives", "Dr. Williams", 3, "Mathematics");
        insertCourse(db, "PHY101", "Physics I",
                "Basic physics principles", "Prof. Brown", 4, "Physics");
        insertCourse(db, "ENG101", "English Composition",
                "Basic English writing skills", "Dr. Davis", 3, "English");

        // Insert sample student
        insertStudent(db, "STU2026001", "John Doe", "Engineering",
                "0712345678", "password123", "john.doe@university.com",
                "123 Main St", "2006-01-15", null);

        // Insert sample timetable
        insertTimetable(db, 1, "Monday", "09:00", "10:30", "Room 101", "Dr. Smith", "Fall 2026");
        insertTimetable(db, 2, "Monday", "11:00", "12:30", "Room 203", "Prof. Johnson", "Fall 2026");
        insertTimetable(db, 3, "Tuesday", "09:00", "10:30", "Room 102", "Dr. Williams", "Fall 2026");
        insertTimetable(db, 4, "Wednesday", "14:00", "15:30", "Room 301", "Prof. Brown", "Fall 2026");

        // Insert sample grades (8-4-4 University System)
        // 70-100: A, 60-69: B, 50-59: C, 40-49: D, <40: E
        insertGrade(db, 1, 1, 75.0, "A", 4.0);
        insertGrade(db, 1, 2, 62.5, "B", 3.0);
        insertGrade(db, 1, 3, 58.0, "C", 2.0);
        insertGrade(db, 1, 4, 45.0, "D", 1.0);

        // Initialize date for samples (current date in 2026)
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String today = "2026-03-20"; // Hardcoded to 2026 for sample data

        // Insert sample attendance
        insertAttendance(db, 1, 1, today, "Present", "09:00", "Dr. Smith");
        insertAttendance(db, 1, 1, today, "Present", "09:00", "Dr. Smith");
        insertAttendance(db, 1, 1, today, "Absent", "09:00", "Dr. Smith");
        insertAttendance(db, 1, 2, today, "Present", "11:00", "Prof. Johnson");
        insertAttendance(db, 1, 2, today, "Late", "11:15", "Prof. Johnson");

        // Insert sample announcements
        insertAnnouncement(db, "Welcome to Semester",
                "Welcome to the new semester! Please check your timetables and course registrations.",
                today, "Admin", "General", 1);
        insertAnnouncement(db, "Exam Schedule Released",
                "The final examination schedule has been released. Please check your timetable for details.",
                today, "Exams Office", "Exams", 1);
        insertAnnouncement(db, "Library Hours Update",
                "The library will be open 24/7 during examination week.",
                today, "Library", "General", 0);
    }

    private void insertCourse(SQLiteDatabase db, String code, String name,
                              String desc, String instructor, int credits, String dept) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_CODE, code);
        values.put(COLUMN_COURSE_NAME, name);
        values.put(COLUMN_DESCRIPTION, desc);
        values.put(COLUMN_INSTRUCTOR, instructor);
        values.put(COLUMN_CREDIT_HOURS, credits);
        values.put(COLUMN_DEPARTMENT, dept);
        db.insert(TABLE_COURSES, null, values);
    }

    private void insertStudent(SQLiteDatabase db, String regNo, String name,
                               String course, String phone, String pass,
                               String email, String address, String dob, String image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REGISTRATION_NUMBER, regNo);
        values.put(COLUMN_FULL_NAME, name);
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_PASSWORD, pass);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_DOB, dob);
        values.put(COLUMN_PROFILE_IMAGE, image);
        db.insert(TABLE_STUDENTS, null, values);
    }

    private void insertTimetable(SQLiteDatabase db, int courseId, String day,
                                 String start, String end, String room, String instructor, String semester) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_ID, courseId);
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_START_TIME, start);
        values.put(COLUMN_END_TIME, end);
        values.put(COLUMN_ROOM, room);
        values.put(COLUMN_INSTRUCTOR, instructor);
        values.put(COLUMN_SEMESTER, semester);
        db.insert(TABLE_TIMETABLE, null, values);
    }

    private void insertGrade(SQLiteDatabase db, int studentId, int courseId,
                             double score, String grade, double gpa) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_COURSE_ID, courseId);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_GRADE, grade);
        values.put(COLUMN_GRADE_POINT, gpa);
        db.insert(TABLE_GRADES, null, values);
    }

    private void insertAttendance(SQLiteDatabase db, int studentId, int courseId,
                                  String date, String status, String time, String lecturer) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_COURSE_ID, courseId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LECTURER, lecturer);
        db.insert(TABLE_ATTENDANCE, null, values);
    }

    private void insertAnnouncement(SQLiteDatabase db, String title, String content,
                                    String date, String author, String category, int important) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_IMPORTANT, important);
        db.insert(TABLE_ANNOUNCEMENTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANNOUNCEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // STUDENT 

    public boolean registerStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REGISTRATION_NUMBER, student.getRegistrationNumber());
        values.put(COLUMN_FULL_NAME, student.getFullName());
        values.put(COLUMN_COURSE, student.getCourse());
        values.put(COLUMN_PHONE_NUMBER, student.getPhoneNumber());
        values.put(COLUMN_PASSWORD, student.getPassword());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_ADDRESS, student.getAddress());
        values.put(COLUMN_DOB, student.getDateOfBirth());
        values.put(COLUMN_PROFILE_IMAGE, student.getProfileImage());

        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public Student loginStudent(String registrationNumber, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENTS +
                " WHERE " + COLUMN_REGISTRATION_NUMBER + " = ? AND " +
                COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{registrationNumber, password});

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int regIndex = cursor.getColumnIndex(COLUMN_REGISTRATION_NUMBER);
            int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
            int courseIndex = cursor.getColumnIndex(COLUMN_COURSE);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
            int passIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            int dobIndex = cursor.getColumnIndex(COLUMN_DOB);
            int imgIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE);

            if (idIndex != -1) student.setId(cursor.getInt(idIndex));
            if (regIndex != -1) student.setRegistrationNumber(cursor.getString(regIndex));
            if (nameIndex != -1) student.setFullName(cursor.getString(nameIndex));
            if (courseIndex != -1) student.setCourse(cursor.getString(courseIndex));
            if (phoneIndex != -1) student.setPhoneNumber(cursor.getString(phoneIndex));
            if (passIndex != -1) student.setPassword(cursor.getString(passIndex));
            if (emailIndex != -1) student.setEmail(cursor.getString(emailIndex));
            if (addressIndex != -1) student.setAddress(cursor.getString(addressIndex));
            if (dobIndex != -1) student.setDateOfBirth(cursor.getString(dobIndex));
            if (imgIndex != -1) student.setProfileImage(cursor.getString(imgIndex));
            
            cursor.close();
            return student;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, student.getFullName());
        values.put(COLUMN_COURSE, student.getCourse());
        values.put(COLUMN_PHONE_NUMBER, student.getPhoneNumber());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_ADDRESS, student.getAddress());
        values.put(COLUMN_DOB, student.getDateOfBirth());
        values.put(COLUMN_PROFILE_IMAGE, student.getProfileImage());

        int result = db.update(TABLE_STUDENTS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        return result > 0;
    }

    public boolean changePassword(int studentId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int result = db.update(TABLE_STUDENTS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(studentId)});
        return result > 0;
    }

    public boolean resetPassword(String registrationNumber, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int result = db.update(TABLE_STUDENTS, values,
                COLUMN_REGISTRATION_NUMBER + " = ?",
                new String[]{registrationNumber});
        return result > 0;
    }

    public Student getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int regIndex = cursor.getColumnIndex(COLUMN_REGISTRATION_NUMBER);
            int nameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
            int courseIndex = cursor.getColumnIndex(COLUMN_COURSE);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
            int passIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            int dobIndex = cursor.getColumnIndex(COLUMN_DOB);
            int imgIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE);

            if (idIndex != -1) student.setId(cursor.getInt(idIndex));
            if (regIndex != -1) student.setRegistrationNumber(cursor.getString(regIndex));
            if (nameIndex != -1) student.setFullName(cursor.getString(nameIndex));
            if (courseIndex != -1) student.setCourse(cursor.getString(courseIndex));
            if (phoneIndex != -1) student.setPhoneNumber(cursor.getString(phoneIndex));
            if (passIndex != -1) student.setPassword(cursor.getString(passIndex));
            if (emailIndex != -1) student.setEmail(cursor.getString(emailIndex));
            if (addressIndex != -1) student.setAddress(cursor.getString(addressIndex));
            if (dobIndex != -1) student.setDateOfBirth(cursor.getString(dobIndex));
            if (imgIndex != -1) student.setProfileImage(cursor.getString(imgIndex));
            
            cursor.close();
            return student;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    // COURSE

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_COURSES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int descIdx = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int instIdx = cursor.getColumnIndex(COLUMN_INSTRUCTOR);
            int credIdx = cursor.getColumnIndex(COLUMN_CREDIT_HOURS);
            int deptIdx = cursor.getColumnIndex(COLUMN_DEPARTMENT);

            do {
                Course course = new Course();
                if (idIdx != -1) course.setId(cursor.getInt(idIdx));
                if (codeIdx != -1) course.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) course.setCourseName(cursor.getString(nameIdx));
                if (descIdx != -1) course.setDescription(cursor.getString(descIdx));
                if (instIdx != -1) course.setInstructor(cursor.getString(instIdx));
                if (credIdx != -1) course.setCreditHours(cursor.getInt(credIdx));
                if (deptIdx != -1) course.setDepartment(cursor.getString(deptIdx));
                courses.add(course);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return courses;
    }

    public Course getCourseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Course course = new Course();
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int descIdx = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int instIdx = cursor.getColumnIndex(COLUMN_INSTRUCTOR);
            int credIdx = cursor.getColumnIndex(COLUMN_CREDIT_HOURS);
            int deptIdx = cursor.getColumnIndex(COLUMN_DEPARTMENT);

            if (idIdx != -1) course.setId(cursor.getInt(idIdx));
            if (codeIdx != -1) course.setCourseCode(cursor.getString(codeIdx));
            if (nameIdx != -1) course.setCourseName(cursor.getString(nameIdx));
            if (descIdx != -1) course.setDescription(cursor.getString(descIdx));
            if (instIdx != -1) course.setInstructor(cursor.getString(instIdx));
            if (credIdx != -1) course.setCreditHours(cursor.getInt(credIdx));
            if (deptIdx != -1) course.setDepartment(cursor.getString(deptIdx));
            
            cursor.close();
            return course;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<Course> getRegisteredCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.* FROM " + TABLE_COURSES + " c " +
                "INNER JOIN " + TABLE_STUDENT_COURSES + " sc " +
                "ON c." + COLUMN_ID + " = sc." + COLUMN_COURSE_ID + " " +
                "WHERE sc." + COLUMN_STUDENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int descIdx = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int instIdx = cursor.getColumnIndex(COLUMN_INSTRUCTOR);
            int credIdx = cursor.getColumnIndex(COLUMN_CREDIT_HOURS);
            int deptIdx = cursor.getColumnIndex(COLUMN_DEPARTMENT);

            do {
                Course course = new Course();
                if (idIdx != -1) course.setId(cursor.getInt(idIdx));
                if (codeIdx != -1) course.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) course.setCourseName(cursor.getString(nameIdx));
                if (descIdx != -1) course.setDescription(cursor.getString(descIdx));
                if (instIdx != -1) course.setInstructor(cursor.getString(instIdx));
                if (credIdx != -1) course.setCreditHours(cursor.getInt(credIdx));
                if (deptIdx != -1) course.setDepartment(cursor.getString(deptIdx));
                courses.add(course);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return courses;
    }

    public boolean registerCourse(int studentId, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_COURSE_ID, courseId);

        long result = db.insert(TABLE_STUDENT_COURSES, null, values);
        return result != -1;
    }

    public boolean unregisterCourse(int studentId, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENT_COURSES,
                COLUMN_STUDENT_ID + " = ? AND " + COLUMN_COURSE_ID + " = ?",
                new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        return result > 0;
    }

    public boolean isCourseRegistered(int studentId, int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENT_COURSES +
                " WHERE " + COLUMN_STUDENT_ID + " = ? AND " + COLUMN_COURSE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    // GRADE

    public List<Grade> getStudentGrades(int studentId) {
        List<Grade> grades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT g.*, c." + COLUMN_COURSE_CODE + ", c." + COLUMN_COURSE_NAME +
                ", c." + COLUMN_CREDIT_HOURS +
                " FROM " + TABLE_GRADES + " g " +
                "INNER JOIN " + TABLE_COURSES + " c " +
                "ON g." + COLUMN_COURSE_ID + " = c." + COLUMN_ID + " " +
                "WHERE g." + COLUMN_STUDENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int sidIdx = cursor.getColumnIndex(COLUMN_STUDENT_ID);
            int cidIdx = cursor.getColumnIndex(COLUMN_COURSE_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int scoreIdx = cursor.getColumnIndex(COLUMN_SCORE);
            int gradeIdx = cursor.getColumnIndex(COLUMN_GRADE);
            int credIdx = cursor.getColumnIndex(COLUMN_CREDIT_HOURS);
            int gpaIdx = cursor.getColumnIndex(COLUMN_GRADE_POINT);

            do {
                Grade grade = new Grade();
                if (idIdx != -1) grade.setId(cursor.getInt(idIdx));
                if (sidIdx != -1) grade.setStudentId(cursor.getInt(sidIdx));
                if (cidIdx != -1) grade.setCourseId(cursor.getInt(cidIdx));
                if (codeIdx != -1) grade.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) grade.setCourseName(cursor.getString(nameIdx));
                if (scoreIdx != -1) grade.setScore(cursor.getDouble(scoreIdx));
                if (gradeIdx != -1) grade.setGrade(cursor.getString(gradeIdx));
                if (credIdx != -1) grade.setCreditHours(cursor.getInt(credIdx));
                if (gpaIdx != -1) grade.setGradePoint(cursor.getDouble(gpaIdx));
                grades.add(grade);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return grades;
    }

    public double calculateGPA(int studentId) {
        List<Grade> grades = getStudentGrades(studentId);
        if (grades.isEmpty()) return 0.0;

        double totalPoints = 0;
        int totalCredits = 0;

        for (Grade grade : grades) {
            totalPoints += grade.getGradePoint() * grade.getCreditHours();
            totalCredits += grade.getCreditHours();
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public double calculateCGPA(int studentId) {
        return calculateGPA(studentId);
    }

    //ATTENDANCE OPERATIONS

    public List<Attendance> getStudentAttendance(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.*, c." + COLUMN_COURSE_CODE + ", c." + COLUMN_COURSE_NAME +
                " FROM " + TABLE_ATTENDANCE + " a " +
                "INNER JOIN " + TABLE_COURSES + " c " +
                "ON a." + COLUMN_COURSE_ID + " = c." + COLUMN_ID + " " +
                "WHERE a." + COLUMN_STUDENT_ID + " = ? " +
                "ORDER BY a." + COLUMN_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int sidIdx = cursor.getColumnIndex(COLUMN_STUDENT_ID);
            int cidIdx = cursor.getColumnIndex(COLUMN_COURSE_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int dateIdx = cursor.getColumnIndex(COLUMN_DATE);
            int statusIdx = cursor.getColumnIndex(COLUMN_STATUS);
            int timeIdx = cursor.getColumnIndex(COLUMN_TIME);
            int lectIdx = cursor.getColumnIndex(COLUMN_LECTURER);

            do {
                Attendance attendance = new Attendance();
                if (idIdx != -1) attendance.setId(cursor.getInt(idIdx));
                if (sidIdx != -1) attendance.setStudentId(cursor.getInt(sidIdx));
                if (cidIdx != -1) attendance.setCourseId(cursor.getInt(cidIdx));
                if (codeIdx != -1) attendance.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) attendance.setCourseName(cursor.getString(nameIdx));
                if (dateIdx != -1) attendance.setDate(cursor.getString(dateIdx));
                if (statusIdx != -1) attendance.setStatus(cursor.getString(statusIdx));
                if (timeIdx != -1) attendance.setTime(cursor.getString(timeIdx));
                if (lectIdx != -1) attendance.setLecturer(cursor.getString(lectIdx));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByCourse(int studentId, int courseId) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.*, c." + COLUMN_COURSE_CODE + ", c." + COLUMN_COURSE_NAME +
                " FROM " + TABLE_ATTENDANCE + " a " +
                "INNER JOIN " + TABLE_COURSES + " c " +
                "ON a." + COLUMN_COURSE_ID + " = c." + COLUMN_ID + " " +
                "WHERE a." + COLUMN_STUDENT_ID + " = ? AND a." + COLUMN_COURSE_ID + " = ? " +
                "ORDER BY a." + COLUMN_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId), String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int sidIdx = cursor.getColumnIndex(COLUMN_STUDENT_ID);
            int cidIdx = cursor.getColumnIndex(COLUMN_COURSE_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int dateIdx = cursor.getColumnIndex(COLUMN_DATE);
            int statusIdx = cursor.getColumnIndex(COLUMN_STATUS);
            int timeIdx = cursor.getColumnIndex(COLUMN_TIME);
            int lectIdx = cursor.getColumnIndex(COLUMN_LECTURER);

            do {
                Attendance attendance = new Attendance();
                if (idIdx != -1) attendance.setId(cursor.getInt(idIdx));
                if (sidIdx != -1) attendance.setStudentId(cursor.getInt(sidIdx));
                if (cidIdx != -1) attendance.setCourseId(cursor.getInt(cidIdx));
                if (codeIdx != -1) attendance.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) attendance.setCourseName(cursor.getString(nameIdx));
                if (dateIdx != -1) attendance.setDate(cursor.getString(dateIdx));
                if (statusIdx != -1) attendance.setStatus(cursor.getString(statusIdx));
                if (timeIdx != -1) attendance.setTime(cursor.getString(timeIdx));
                if (lectIdx != -1) attendance.setLecturer(cursor.getString(lectIdx));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return attendanceList;
    }

    public AttendanceStats getAttendanceStats(int studentId, int courseId) {
        List<Attendance> attendances = getAttendanceByCourse(studentId, courseId);
        int present = 0, absent = 0, late = 0;

        for (Attendance a : attendances) {
            switch (a.getStatus().toLowerCase()) {
                case "present":
                    present++;
                    break;
                case "absent":
                    absent++;
                    break;
                case "late":
                    late++;
                    break;
            }
        }

        return new AttendanceStats(present, absent, late, attendances.size());
    }

    public static class AttendanceStats {
        public int present;
        public int absent;
        public int late;
        public int total;

        public AttendanceStats(int present, int absent, int late, int total) {
            this.present = present;
            this.absent = absent;
            this.late = late;
            this.total = total;
        }

        public double getAttendancePercentage() {
            return total > 0 ? (double) (present + late) / total * 100 : 0;
        }
    }

    // TIMETABLE OPERATIONS

    public List<Timetable> getStudentTimetable(int studentId) {
        List<Timetable> timetable = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, c." + COLUMN_COURSE_CODE + ", c." + COLUMN_COURSE_NAME +
                " FROM " + TABLE_TIMETABLE + " t " +
                "INNER JOIN " + TABLE_COURSES + " c " +
                "ON t." + COLUMN_COURSE_ID + " = c." + COLUMN_ID + " " +
                "INNER JOIN " + TABLE_STUDENT_COURSES + " sc " +
                "ON c." + COLUMN_ID + " = sc." + COLUMN_COURSE_ID + " " +
                "WHERE sc." + COLUMN_STUDENT_ID + " = ? " +
                "ORDER BY CASE t." + COLUMN_DAY +
                " WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 " +
                "WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 " +
                "WHEN 'Sunday' THEN 7 END, t." + COLUMN_START_TIME;
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int cidIdx = cursor.getColumnIndex(COLUMN_COURSE_ID);
            int codeIdx = cursor.getColumnIndex(COLUMN_COURSE_CODE);
            int nameIdx = cursor.getColumnIndex(COLUMN_COURSE_NAME);
            int dayIdx = cursor.getColumnIndex(COLUMN_DAY);
            int startIdx = cursor.getColumnIndex(COLUMN_START_TIME);
            int endIdx = cursor.getColumnIndex(COLUMN_END_TIME);
            int roomIdx = cursor.getColumnIndex(COLUMN_ROOM);
            int instIdx = cursor.getColumnIndex(COLUMN_INSTRUCTOR);
            int semIdx = cursor.getColumnIndex(COLUMN_SEMESTER);

            do {
                Timetable item = new Timetable();
                if (idIdx != -1) item.setId(cursor.getInt(idIdx));
                if (cidIdx != -1) item.setCourseId(cursor.getInt(cidIdx));
                if (codeIdx != -1) item.setCourseCode(cursor.getString(codeIdx));
                if (nameIdx != -1) item.setCourseName(cursor.getString(nameIdx));
                if (dayIdx != -1) item.setDay(cursor.getString(dayIdx));
                if (startIdx != -1) item.setStartTime(cursor.getString(startIdx));
                if (endIdx != -1) item.setEndTime(cursor.getString(endIdx));
                if (roomIdx != -1) item.setRoom(cursor.getString(roomIdx));
                if (instIdx != -1) item.setInstructor(cursor.getString(instIdx));
                if (semIdx != -1) item.setSemester(cursor.getString(semIdx));
                timetable.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return timetable;
    }

    // ANNOUNCEMENT

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ANNOUNCEMENTS +
                " ORDER BY " + COLUMN_IMPORTANT + " DESC, " + COLUMN_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int titleIdx = cursor.getColumnIndex(COLUMN_TITLE);
            int contIdx = cursor.getColumnIndex(COLUMN_CONTENT);
            int dateIdx = cursor.getColumnIndex(COLUMN_DATE);
            int authIdx = cursor.getColumnIndex(COLUMN_AUTHOR);
            int catIdx = cursor.getColumnIndex(COLUMN_CATEGORY);
            int impIdx = cursor.getColumnIndex(COLUMN_IMPORTANT);

            do {
                Announcement announcement = new Announcement();
                if (idIdx != -1) announcement.setId(cursor.getInt(idIdx));
                if (titleIdx != -1) announcement.setTitle(cursor.getString(titleIdx));
                if (contIdx != -1) announcement.setContent(cursor.getString(contIdx));
                if (dateIdx != -1) announcement.setDate(cursor.getString(dateIdx));
                if (authIdx != -1) announcement.setAuthor(cursor.getString(authIdx));
                if (catIdx != -1) announcement.setCategory(cursor.getString(catIdx));
                if (impIdx != -1) announcement.setImportant(cursor.getInt(impIdx) == 1);
                announcements.add(announcement);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return announcements;
    }

    public List<Announcement> getImportantAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ANNOUNCEMENTS +
                " WHERE " + COLUMN_IMPORTANT + " = 1 " +
                "ORDER BY " + COLUMN_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            int titleIdx = cursor.getColumnIndex(COLUMN_TITLE);
            int contIdx = cursor.getColumnIndex(COLUMN_CONTENT);
            int dateIdx = cursor.getColumnIndex(COLUMN_DATE);
            int authIdx = cursor.getColumnIndex(COLUMN_AUTHOR);
            int catIdx = cursor.getColumnIndex(COLUMN_CATEGORY);

            do {
                Announcement announcement = new Announcement();
                if (idIdx != -1) announcement.setId(cursor.getInt(idIdx));
                if (titleIdx != -1) announcement.setTitle(cursor.getString(titleIdx));
                if (contIdx != -1) announcement.setContent(cursor.getString(contIdx));
                if (dateIdx != -1) announcement.setDate(cursor.getString(dateIdx));
                if (authIdx != -1) announcement.setAuthor(cursor.getString(authIdx));
                if (catIdx != -1) announcement.setCategory(cursor.getString(catIdx));
                announcement.setImportant(true);
                announcements.add(announcement);
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return announcements;
    }
}