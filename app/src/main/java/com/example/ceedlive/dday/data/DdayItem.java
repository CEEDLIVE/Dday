package com.example.ceedlive.dday.data;

public class DdayItem {
    private String uniqueKey;
    private int _id;
    private String title;
    private String description;
    private String date;
    private String diffDays;
    private int notification;

    private boolean isChecked;
    private boolean isVisibleDetail;

    // TODO 생성자 빌더 패턴 알아보기

    public DdayItem() {

    }

    public DdayItem(String date,
                    String title,
                    String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public DdayItem(String date,
                    String title,
                    String description,
                    int notification) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.notification = notification;
    }

    public DdayItem(int _id,
                    String date,
                    String title,
                    String description,
                    int notification) {
        this._id = _id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.notification = notification;
    }

    public DdayItem(int _id,
                    String title,
                    String description,
                    String date,
                    String diffDays) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.diffDays = diffDays;
    }

    public int get_id() { return _id; }

    public void set_id(int _id) { this._id = _id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(String diffDays) {
        this.diffDays = diffDays;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getIsVisibleDetail() {
        return isVisibleDetail;
    }

    public void setIsVisibleDetail(boolean isVisibleDetail) {
        this.isVisibleDetail = isVisibleDetail;
    }

    @Override
    public String toString() {
        return "DdayItem{" +
                "uniqueKey='" + uniqueKey + '\'' +
                ", _id=" + _id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", diffDays='" + diffDays + '\'' +
                ", notification=" + notification +
                ", isChecked=" + isChecked +
                ", isVisibleDetail=" + isVisibleDetail +
                '}';
    }
}
