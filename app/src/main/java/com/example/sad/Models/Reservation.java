package com.example.sad.Models;

public class Reservation {
    private String id, from , to , date , purpose;
    private int busNeeded;

    public Reservation() {
    }

    public Reservation(String id, String from, String to, String date, String purpose, int busNeeded) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.purpose = purpose;
        this.busNeeded = busNeeded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getBusNeeded() {
        return busNeeded;
    }

    public void setBusNeeded(int busNeeded) {
        this.busNeeded = busNeeded;
    }
}
