package com.example.unibus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class LostItems extends AppCompatActivity {
    private ImageButton mainPostButton, mainSearchButton, mainReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostitems);
        setInButton();
    }

    private void setInButton() {
        mainPostButton = findViewById(R.id.selectPostButton);
        mainSearchButton = findViewById(R.id.selectSearchButton);
        mainReportButton = findViewById(R.id.selectReportButton);
        mainPostButton.setOnClickListener(v -> startActivities(new Intent[]{new Intent(LostItems.this, Post_LostItems.class)}));
        mainSearchButton.setOnClickListener(v -> startActivities(new Intent[]{new Intent(LostItems.this, Search_LostItems.class)}));
        mainReportButton.setOnClickListener(v -> startActivities(new Intent[]{new Intent(LostItems.this, Report_LostItems.class)}));


    }
}