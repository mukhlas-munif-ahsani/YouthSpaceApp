package com.munifahsan.youthspaceapp.Beasiswa;

import com.google.firebase.firestore.DocumentId;

public class BeasiswaModel {
    @DocumentId
    String id;
    String nImage;
    String nImageHeader;
    String nKategori;
    String nTitle;
    String nLokasi;
    String nDeadline;
    String nDesc1;
    String nDesc2;
    String nDesc3;
    String nKriteria;
    String nProsesAplikasi;
    String nFormLink;

    public BeasiswaModel() {
    }

    public BeasiswaModel(String id, String nImage, String nImageHeader, String nKategori, String nTitle, String nLokasi, String nDeadline, String nDesc1, String nDesc2, String nDesc3, String nKriteria, String nProsesAplikasi, String nFormLink) {
        this.id = id;
        this.nImage = nImage;
        this.nImageHeader = nImageHeader;
        this.nKategori = nKategori;
        this.nTitle = nTitle;
        this.nLokasi = nLokasi;
        this.nDeadline = nDeadline;
        this.nDesc1 = nDesc1;
        this.nDesc2 = nDesc2;
        this.nDesc3 = nDesc3;
        this.nKriteria = nKriteria;
        this.nProsesAplikasi = nProsesAplikasi;
        this.nFormLink = nFormLink;
    }

    public String getId() {
        return id;
    }

    public String getnImage() {
        return nImage;
    }

    public String getnImageHeader() {
        return nImageHeader;
    }

    public String getnKategori() {
        return nKategori;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnLokasi() {
        return nLokasi;
    }

    public String getnDeadline() {
        return nDeadline;
    }

    public String getnDesc1() {
        return nDesc1;
    }

    public String getnDesc2() {
        return nDesc2;
    }

    public String getnDesc3() {
        return nDesc3;
    }

    public String getnKriteria() {
        return nKriteria;
    }

    public String getnProsesAplikasi() {
        return nProsesAplikasi;
    }

    public String getnFormLink() {
        return nFormLink;
    }
}

