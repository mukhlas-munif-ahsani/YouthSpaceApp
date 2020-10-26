package com.munifahsan.youthspaceapp.EditProfile.repo;

import com.google.firebase.firestore.DocumentId;

public class EditProfileModel {
    @DocumentId
    private String id;
    private String nCreated_at;
    private String nUpdated_at;
    private String nAlamat;
    private String nLokasi;
    private String nEmail;
    private String nLevel;
    private String nNama;
    private String nNoTlp;
    private String nToken_id;
    private String nGender;
    private String nImageUrl;

    public EditProfileModel() {
    }

    public EditProfileModel(String id, String nCreated_at, String nUpdated_at, String nAlamat, String nLokasi, String nEmail, String nLevel, String nNama, String nNoTlp, String nToken_id, String nGender, String nImageUrl) {
        this.id = id;
        this.nCreated_at = nCreated_at;
        this.nUpdated_at = nUpdated_at;
        this.nAlamat = nAlamat;
        this.nLokasi = nLokasi;
        this.nEmail = nEmail;
        this.nLevel = nLevel;
        this.nNama = nNama;
        this.nNoTlp = nNoTlp;
        this.nToken_id = nToken_id;
        this.nGender = nGender;
        this.nImageUrl = nImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getnCreated_at() {
        return nCreated_at;
    }

    public String getnUpdated_at() {
        return nUpdated_at;
    }

    public String getnAlamat() {
        return nAlamat;
    }

    public String getnLokasi() {
        return nLokasi;
    }

    public String getnEmail() {
        return nEmail;
    }

    public String getnLevel() {
        return nLevel;
    }

    public String getnNama() {
        return nNama;
    }

    public String getnNoTlp() {
        return nNoTlp;
    }

    public String getnToken_id() {
        return nToken_id;
    }

    public String getnGender() {
        return nGender;
    }

    public String getnImageUrl() {
        return nImageUrl;
    }
}
