package com.example.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Lostitem.LostItems;
import com.example.Reservation.Models.Reservation;
import com.example.Reservation.Utils.FirebaseUtils;
import com.example.unibus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView reservationsTitle , noReservationsText;
    private List<Reservation> reservationList;
    private View line2;
    private RecyclerView reservationsRecView;
    private ReservationAdapter adapter;
    private LinearLayout reservationNav , lostItemNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reservationsTitle = findViewById(R.id.landingPageReservationsTitle);
        noReservationsText = findViewById(R.id.landingPageReservations);
        line2 = findViewById(R.id.landingPageLine2);
        reservationList = new ArrayList<>();
        reservationsRecView = findViewById(R.id.landingPageReservationsRecView);
        reservationNav = findViewById(R.id.navToReservation);
        lostItemNav = findViewById(R.id.navToLostItem);

        adapter = new ReservationAdapter(this , reservationList);
        reservationsRecView.setAdapter(adapter);
        reservationsRecView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUtils.getReservationCollection().addSnapshotListener((value, error) -> {
            if (!value.isEmpty()) {
                noReservationsText.setVisibility(View.GONE);
                reservationsRecView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line2.getLayoutParams();
                params.addRule(RelativeLayout.BELOW , R.id.landingPageReservationsRecView);
                line2.setLayoutParams(params);
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Reservation reservation = doc.toObject(Reservation.class);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        c.add(Calendar.DATE, -1);
                        date = c.getTime();
                        if (date.after(format.parse(reservation.getDate()))) {
                            FirebaseUtils.getReservationCollection().document(reservation.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    Reservation reservation = dc.getDocument().toObject(Reservation.class);
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        reservationList.add(dc.getDocument().toObject(Reservation.class));
                        adapter.notifyItemInserted(reservationList.size() - 1);
                    }
                    else if (dc.getType() == DocumentChange.Type.REMOVED && reservationList.contains(reservation)) {
                        int index = reservationList.indexOf(reservation);
                        reservationList.remove(reservation);
                        adapter.notifyItemRemoved(index);
                    }
                }
            }
        });

        reservationNav.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this , ReservationPage.class));
        });

        lostItemNav.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this , LostItems.class));
        });
    }
}