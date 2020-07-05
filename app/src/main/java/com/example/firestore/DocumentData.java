package com.example.firestore;

import java.util.Map;

public class DocumentData {
    private String title;
    private String thought;

    public DocumentData(String title, String thought) {
        this.title = title;
        this.thought = thought;
    }

    public DocumentData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }
}
