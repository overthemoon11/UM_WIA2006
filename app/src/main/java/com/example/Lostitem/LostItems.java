package com.example.Lostitem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unibus.R;

public class LostItems extends AppCompatActivity {
    private ImageButton mainPostButton, mainSearchButton, mainReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostitems);
        setInButton();
        setupBackButton();
    }

    private void setInButton() {
        mainPostButton = findViewById(R.id.selectPostButton);
        mainSearchButton = findViewById(R.id.selectSearchButton);
        mainReportButton = findViewById(R.id.selectReportButton);
        mainPostButton.setOnClickListener(v -> startActivity(new Intent(LostItems.this, Post_LostItems.class)));
        mainSearchButton.setOnClickListener(v -> startActivity(new Intent(LostItems.this, Search_LostItems.class)));
        mainReportButton.setOnClickListener(v -> startActivity(new Intent(LostItems.this, Report_LostItems.class)));
    }

    private void setupBackButton() {
        ImageButton lostBackMain = findViewById(R.id.BtnBackLostMain);
        lostBackMain.setOnClickListener(v -> onBackPressed());
    }
}