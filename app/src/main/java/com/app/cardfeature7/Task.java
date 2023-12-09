package com.app.cardfeature7;

public class Task {
    private String id;
    private String name;
    private String description;
    private String selectedImageUrl;
    private String date;
    private String voiceRecordePath;
    private String time;
    private boolean completed; // represent completion status
    private String motherId; // store the mother's ID
    private boolean isClickable = true; //when click it first time it can't be clicked again'child interface'
    public Task(String id, String name, String description, String selectedImageUrl, String date, String voiceRecordePath, String time, boolean completed,
                String motherId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.selectedImageUrl = selectedImageUrl;
        this.date = date;
        this.voiceRecordePath = voiceRecordePath;
        this.time = time;
        this.completed = completed;
        this.motherId = motherId; // set the mother's ID
    }

    public Task(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getMotherId() {
        return motherId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectedImageUrl() {
        return selectedImageUrl;
    }

    public void setSelectedImageUrl(String selectedImageUrl) {
        this.selectedImageUrl = selectedImageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoiceRecordePath() {
        return voiceRecordePath;
    }

    public void setVoiceRecordePath(String voiceRecordePath) {
        this.voiceRecordePath = voiceRecordePath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        if (isClickable) {
            this.completed = completed;
            isClickable = false; // Set to false after completion
        }
    }

}