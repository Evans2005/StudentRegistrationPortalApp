package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Student;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etRegNumber, etFullName, etCourse, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister, btnBackToLogin;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> validateAndRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etRegNumber = findViewById(R.id.etRegNumber);
        etFullName = findViewById(R.id.etFullName);
        etCourse = findViewById(R.id.etCourse);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void validateAndRegister() {
        String regNumber = etRegNumber.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String course = etCourse.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(regNumber)) {
            etRegNumber.setError("Registration number is required");
            return;
        }

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(course)) {
            etCourse.setError("Course is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        Student student = new Student();
        student.setRegistrationNumber(regNumber);
        student.setFullName(fullName);
        student.setCourse(course);
        student.setPhoneNumber(phone);
        student.setPassword(password);

        new Thread(() -> {
            boolean success = dbHelper.registerStudent(student);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);

                if (success) {
                    Toast.makeText(RegistrationActivity.this,
                            "Registration successful! Please login.",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this,
                            "Registration failed. Registration number may already exist.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}