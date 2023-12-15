package com.example.unibus;

public class ReportedLostItem {
    private String location;
    private String date;
    private String time;
    private String item;
    private String contact;
    private String extra;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ReportedLostItem() {
    }

    public ReportedLostItem(String location, String date, String time, String item, String contact, String extra) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.item = item;
        this.contact = contact;
        this.extra = extra;
    }
}
