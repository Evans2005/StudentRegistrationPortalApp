package com.example.studentregistrationportal.activities;

import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.adapters.AnnouncementAdapter;
import com.example.studentregistrationportal.database.DatabaseHelper;
import com.example.studentregistrationportal.models.Announcement;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {
    private RecyclerView rvAnnouncements;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rvAnnouncements = findViewById(R.id.rvAnnouncements);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        ImageButton btnBack = findViewById(R.id.btnBack);
        
        btnBack.setOnClickListener(v -> finish());
        dbHelper = new DatabaseHelper(this);

        loadAnnouncements();
    }

    private void loadAnnouncements() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Announcement> announcements = dbHelper.getAllAnnouncements();
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                if (announcements != null && !announcements.isEmpty()) {
                    AnnouncementAdapter adapter = new AnnouncementAdapter(this, announcements);
                    rvAnnouncements.setLayoutManager(new LinearLayoutManager(this));
                    rvAnnouncements.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}