package com.example.Reservation.Utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    public static CollectionReference getReservationCollection () {
        return FirebaseFirestore.getInstance().collection("Reservations");
    }
}
