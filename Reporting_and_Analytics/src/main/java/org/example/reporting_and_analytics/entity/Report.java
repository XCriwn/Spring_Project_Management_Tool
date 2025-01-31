package org.example.reporting_and_analytics.entity;

public class Report {
    private String title;
    private Object details;

    // Constructor, Getters, Setters
    public Report(String title, Object details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}