package com.example.sad.Utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sad.Models.Reservation;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EventListeners {
    public static void ReservationEventChangeListener (List<Reservation> reservationList , RecyclerView.Adapter adapter) {
        FirebaseUtils.getReservationCollection().addSnapshotListener((value, error) -> {
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    reservationList.add(dc.getDocument().toObject(Reservation.class));
                    adapter.notifyItemInserted(reservationList.size() - 1);
                }
            }
        });
    }
}
