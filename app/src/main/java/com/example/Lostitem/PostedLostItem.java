package com.example.Lostitem;

import java.util.List;

public class PostedLostItem {
    private String location;
    private String date;
    private String time;
    private String item;
    private String point;
    private String extra;
    private List<String> imageUrls;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public PostedLostItem() {
    }

    public PostedLostItem(List<String> imageUrls, String location, String date, String time, String item, String point, String extra) {
        this.imageUrls = imageUrls;
        this.location = location;
        this.date = date;
        this.time = time;
        this.item = item;
        this.point = point;
        this.extra = extra;
    }
}
