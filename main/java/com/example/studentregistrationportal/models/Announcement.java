package com.example.studentregistrationportal.models;

public class Announcement {
    private int id;
    private String title;
    private String content;
    private String date;
    private String author;
    private String category;
    private boolean isImportant;

    public Announcement() {}

    public Announcement(int id, String title, String content, String date,
                        String author, String category, boolean isImportant) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
        this.category = category;
        this.isImportant = isImportant;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isImportant() { return isImportant; }
    public void setImportant(boolean important) { isImportant = important; }
}