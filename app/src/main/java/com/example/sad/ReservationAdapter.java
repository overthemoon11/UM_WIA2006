package com.example.sad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sad.Models.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private Context context;
    private List<Reservation> reservationList;

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_item , null , false);
        ReservationViewHolder viewHolder = new ReservationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.date.setText(reservation.getDate());
        holder.to.setText(reservation.getTo());
        holder.from.setText(reservation.getFrom());
        holder.busNeeded.setText(String.valueOf(reservation.getBusNeeded()));
        holder.purpose.setText(reservation.getPurpose());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView date , to , from , busNeeded , purpose;
        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.reservationDate);
            to = itemView.findViewById(R.id.reservationTo);
            from = itemView.findViewById(R.id.reservationFrom);
            busNeeded = itemView.findViewById(R.id.reservationBusNeeded);
            purpose = itemView.findViewById(R.id.reservationPurpose);
        }
    }
}
