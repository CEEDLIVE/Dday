package com.example.ceedlive.dday.dto;

public class DdayItem {
    public String uniqueKey;
    public String title;
    public String description;
    public String date;
    public String diffDays;
    public boolean isChecked;
    public boolean isVisibleDetail;

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
}