package com.app.cardfeature7;
public class NewsItem {
    private String id;  // Add ID field
    private String text;

    public NewsItem(String id, String text) {
        this.id = id;
        this.text = text;
    }
    // Add a public no-argument constructor
    public NewsItem() {
        // Default constructor required for Firebase
    }

    public NewsItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
