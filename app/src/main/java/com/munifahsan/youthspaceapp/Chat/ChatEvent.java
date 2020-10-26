package com.munifahsan.youthspaceapp.Chat;

public class ChatEvent {
    public static final int onSuccess = 0;
    public static final int onError = 1;

    private int eventType;
    private String Error;

    private String image;
    private String nama;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
