package com.uima.cogs;

import java.util.ArrayList;

public class Meetings {

    private String name;
    private String location;
    private String decription;
    private ArrayList<String> attendess = new ArrayList<>();
    private int day;
    private int year;
    private int month;
    private int hour;
    private int minute;

    public ArrayList<String> getAttendess() {
        return attendess;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getDecription() {
        return decription;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setAttendess(ArrayList<String> attendess) {
        this.attendess = attendess;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
