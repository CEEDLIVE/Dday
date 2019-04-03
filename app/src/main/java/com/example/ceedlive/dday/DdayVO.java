package com.example.ceedlive.dday;

public class DdayVO {
    private String subject;
    private String author;
    private int hitCount;

    public DdayVO(String subject, String author, int hitCount) {
        this.subject = subject;
        this.author = author;
        this.hitCount = hitCount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}
