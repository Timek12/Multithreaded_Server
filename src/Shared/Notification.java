package Shared;

import com.google.gson.Gson;

public class Notification {
    private String message;
    private int delayInSeconds;
    private String dateTimeAtCreation = null;
    private String dateTimeAtCompleted = null;

    public Notification(String message, int delayInSeconds, String dateTimeAtCreation) {
        this.message = message;
        this.delayInSeconds = delayInSeconds;
        this.dateTimeAtCreation = dateTimeAtCreation;
    }

    public String getMessage() {
        return message;
    }

    public int getDelayInSeconds() {
        return delayInSeconds;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Notification fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Notification.class);
    }

    public void setDateTimeAtCreation(String dateTimeAtCreation) {
        this.dateTimeAtCreation = dateTimeAtCreation;
    }

    public void setDateTimeAtCompleted(String dateTimeAtCompleted) {
        this.dateTimeAtCompleted = dateTimeAtCompleted;
    }

    public String getDateTimeAtCreation() {
        return dateTimeAtCreation;
    }

    public String getDateTimeAtCompleted() {
        return dateTimeAtCompleted;
    }
}