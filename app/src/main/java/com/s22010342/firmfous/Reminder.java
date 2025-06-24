package com.s22010342.firmfous;

public class Reminder {
    private String id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String location;
    private String imageUrl;
    private String status;

    public Reminder() {}
    public Reminder(String id, String title, String description, String date, String time,
                    String location, String imageUrl, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public String getStatus() { return status; }

    // Setter for status
    public void setStatus(String status) { this.status = status; }
}
