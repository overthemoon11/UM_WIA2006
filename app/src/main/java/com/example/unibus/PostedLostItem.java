package com.example.unibus;

public class PostedLostItem {
    private String location;
    private String date;
    private String time;
    private String item;
    private String point;
    private String extra;
    private String imageURl;

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
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

    public PostedLostItem(String imageURL, String location, String date, String time, String item, String point, String extra) {
        this.imageURl = imageURL;
        this.location = location;
        this.date = date;
        this.time = time;
        this.item = item;
        this.point = point;
        this.extra = extra;
    }
}
