package com.munifahsan.youthspaceapp.EditProfile;

import android.util.Log;

public class EditProfileEvent {
    public static final int onError = 0;
    public static final int onSuccess = 1;

    private int eventType;
    private String errorMessage;
    private String id;
    private String created_at;
    private String updated_at;
    private String alamat;
    private String gender;
    private String email;
    private String level;
    private String nama;
    private String no_tlp;
    private String token_id;
    private String imageUrl;

    public static int getOnError() {
        return onError;
    }

    public static int getOnSuccess() {
        return onSuccess;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNama() {
        Log.d("nama in getNama: " , nama);
        return nama;

    }

    public void setNama(String nama) {
        this.nama = nama;
        Log.d("nama in setNama: " , nama);
    }

    public String getNo_tlp() {
        return no_tlp;
    }

    public void setNo_tlp(String no_tlp) {
        this.no_tlp = no_tlp;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
