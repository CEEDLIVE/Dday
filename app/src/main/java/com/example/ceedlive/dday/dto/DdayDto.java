package com.example.ceedlive.dday.dto;

public class DdayDto {
    private int id;
    private String title;
    private String date;
    private String day;

    public DdayDto(int id, String title, String date, String day) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.day = day;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
