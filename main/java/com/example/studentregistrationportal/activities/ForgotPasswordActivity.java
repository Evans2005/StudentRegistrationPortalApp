package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.database.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etRegNumber, etNewPassword, etConfirmPassword;
    private Button btnResetPassword, btnBackToLogin;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        dbHelper = new DatabaseHelper(this);

        btnResetPassword.setOnClickListener(v -> validateAndReset());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etRegNumber = findViewById(R.id.etRegNumber);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void validateAndReset() {
        String regNumber = etRegNumber.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(regNumber)) {
            etRegNumber.setError("Registration number is required");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("New password is required");
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnResetPassword.setEnabled(false);

        new Thread(() -> {
            boolean success = dbHelper.resetPassword(regNumber, newPassword);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnResetPassword.setEnabled(true);

                if (success) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Password reset successful! Please login with your new password.",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Registration number not found. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}