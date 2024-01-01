package com.example.Reservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Reservation.Models.Reservation;
import com.example.Reservation.Utils.FirebaseUtils;
import com.example.unibus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReservationPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Spinner fromSpinner , toSpinner;
    private String fromLocation , toLocation , bookingPurpose;
    private int busNeeded;
    private TextView date;
    private Button confirmReservation;
    private ImageButton BtnBack;
    private RadioGridGroup busRadioGroup;
    private RadioGroup purposeRadioGroup;
    private RadioButton radioOne , radioTwo , radioThree , radioFour , radioFive , radioSix , purposeRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_page);
        fromSpinner = findViewById(R.id.reservationsPageFromSpinner);
        toSpinner = findViewById(R.id.reservationsPageToSpinner);
        date = findViewById(R.id.reservationsPageDate);
        confirmReservation = findViewById(R.id.reservationsPageConfirm);
        busRadioGroup = findViewById(R.id.reservationsPageBusRadio);
        purposeRadioGroup = findViewById(R.id.reservationsPagePurposeRadio);
        radioOne = findViewById(R.id.radioOne);
        radioTwo = findViewById(R.id.radioTwo);
        radioThree = findViewById(R.id.radioThree);
        radioFour = findViewById(R.id.radioFour);
        radioFive = findViewById(R.id.radioFive);
        radioSix = findViewById(R.id.radioSix);
        BtnBack = findViewById(R.id.reservationsPageBack);
        BtnBack.setOnClickListener(v -> onBackPressed());


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.locations , R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.locations , R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(adapter2);

        date.setOnClickListener(v -> new DatePickerFragment().show(getSupportFragmentManager() , "date"));

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLocation = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fromLocation = (String) parent.getItemAtPosition(0);
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLocation = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fromLocation = (String) parent.getItemAtPosition(1);
            }
        });

        confirmReservation.setOnClickListener(v -> {
            if (radioSix.isChecked()) busNeeded = 6;
            else if (radioTwo.isChecked()) busNeeded = 2;
            else if (radioThree.isChecked()) busNeeded = 3;
            else if (radioFour.isChecked()) busNeeded = 4;
            else if (radioFive.isChecked()) busNeeded = 5;
            else busNeeded = 1;
            String bookingDate = date.getText().toString();
            String id = bookingDate + "_" + fromLocation + "_" + toLocation;
            FirebaseUtils.getReservationCollection().document(id)
                    .set(new Reservation(id , fromLocation , toLocation , bookingDate , bookingPurpose , busNeeded))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ReservationPage.this, "Reservation made", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ReservationPage.this , MainActivity.class));
                                finish();
                            }
                        }
                    });
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR , year);
        calendar.set(Calendar.MONTH , month);
        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
        date.setText(currentDate);
    }

    public void purposeCheck (View v) {
        int radioId = purposeRadioGroup.getCheckedRadioButtonId();
//        Toast.makeText(this, radioId + " ", Toast.LENGTH_SHORT).show();
        purposeRadio = findViewById(radioId);
        bookingPurpose = purposeRadio.getText().toString();
    }
}