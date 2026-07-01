package com.example.studentregistrationportal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Student;

public class ProfileActivity extends AppCompatActivity {
    private EditText etFullName, etCourse, etPhone, etEmail, etAddress, etDob;
    private Button btnSave, btnChangePassword;
    private ImageButton btnBack;
    private ImageView ivProfile;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        loadProfile();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etCourse = findViewById(R.id.etCourse);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etDob = findViewById(R.id.etDob);
        btnSave = findViewById(R.id.btnSave);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        ivProfile = findViewById(R.id.ivProfile);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveProfile());
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        });
    }

    private void loadProfile() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            Student student = dbHelper.getStudentById(studentId);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (student != null) {
                    etFullName.setText(student.getFullName());
                    etCourse.setText(student.getCourse());
                    etPhone.setText(student.getPhoneNumber());
                    etEmail.setText(student.getEmail() != null ? student.getEmail() : "");
                    etAddress.setText(student.getAddress() != null ? student.getAddress() : "");
                    etDob.setText(student.getDateOfBirth() != null ? student.getDateOfBirth() : "");

                    // Load profile image if available
                    if (student.getProfileImage() != null) {
                        // Load image from path
                    }
                }
            });
        }).start();
    }

    private void saveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String course = etCourse.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            return;
        }

        Student student = new Student();
        student.setId(studentId);
        student.setFullName(fullName);
        student.setCourse(course);
        student.setPhoneNumber(phone);
        student.setEmail(email);
        student.setAddress(address);
        student.setDateOfBirth(dob);

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        new Thread(() -> {
            boolean success = dbHelper.updateStudent(student);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                if (success) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}