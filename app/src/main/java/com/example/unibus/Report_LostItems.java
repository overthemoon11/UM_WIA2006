package com.example.unibus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report_LostItems extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private EditText editLocation, editDate, editTime, editItem, editContact, editExtra;
    private Button btnSubmit;
    private ImageButton reportBackMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_items);

        editLocation = findViewById(R.id.reportEditLocation);
        editDate = findViewById(R.id.reportEditDate);
        editTime = findViewById(R.id.reportEditTime);
        editItem = findViewById(R.id.reportEditItem);
        editContact = findViewById(R.id.reportEditContact);
        editExtra = findViewById(R.id.reportEditExtra);
        btnSubmit = findViewById(R.id.submitReportButton);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String location = editLocation.getText().toString();
                String date = editDate.getText().toString();
                String time = editTime.getText().toString();
                String item = editItem.getText().toString();
                String contact = editContact.getText().toString();
                String extra = editExtra.getText().toString();

                if (!location.isEmpty() && !date.isEmpty() && !time.isEmpty() && !item.isEmpty() && !contact.isEmpty() && !extra.isEmpty()) {
                    // Save data to Firebase
                    saveLostItemToFirebase(location, date, time, item, contact, extra);
                } else {
                    // Handle empty input
                }

            }
        });
        setupBackButtons();

    }


    private void setupBackButtons() {
        reportBackMain = findViewById(R.id.reportBackMain);
        reportBackMain.setOnClickListener(v -> startActivities(new Intent[]{new Intent(Report_LostItems.this, LostItems.class)}));
    }

    private void saveLostItemToFirebase(String location, String date, String time, String item, String contact, String extra) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reported_LostItems");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = String.valueOf(snapshot.getChildrenCount());

                // Create a PostedLostItem object with image URL
                ReportedLostItem addLostItem = new ReportedLostItem(location, date, time, item, contact, extra);

                // Save to Firebase
                databaseReference.child(key).setValue(addLostItem).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Report_LostItems.this, "Successfully added Lost Item.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Report_LostItems.this, LostItems.class));
                        finish();
                    } else {
                        Toast.makeText(Report_LostItems.this, "Failed to add Lost Item.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }


}
