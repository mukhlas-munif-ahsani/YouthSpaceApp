package com.munifahsan.youthspaceapp.Register;

public class RegisterEvent {
    public static final int onError = 0;
    public static final int onSuccess = 1;

    private int eventType;
    private String errorMessage;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
