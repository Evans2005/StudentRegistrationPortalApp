package com.example.studentregistrationportal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Student;

public class LoginActivity extends AppCompatActivity {
    private EditText etRegistrationNumber, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences("StudentPortal", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            int studentId = prefs.getInt("studentId", -1);
            if (studentId != -1) {
                navigateToDashboard(studentId);
                return;
            }
        }

        initViews();
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> validateAndLogin());
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        etRegistrationNumber = findViewById(R.id.etRegistrationNumber);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void validateAndLogin() {
        String regNumber = etRegistrationNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(regNumber)) {
            etRegistrationNumber.setError("Registration number is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Perform login in background thread
        new Thread(() -> {
            Student student = dbHelper.loginStudent(regNumber, password);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (student != null) {
                    // Save login state
                    SharedPreferences prefs = getSharedPreferences("StudentPortal", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("studentId", student.getId());
                    editor.putString("studentName", student.getFullName());
                    editor.apply();

                    Toast.makeText(LoginActivity.this,
                            "Welcome, " + student.getFullName() + "!",
                            Toast.LENGTH_SHORT).show();
                    navigateToDashboard(student.getId());
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid credentials. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void navigateToDashboard(int studentId) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("studentId", studentId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}