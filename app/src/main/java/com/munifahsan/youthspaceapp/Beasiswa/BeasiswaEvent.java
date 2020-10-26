package com.munifahsan.youthspaceapp.Beasiswa;

public class BeasiswaEvent {
    public static final int onSuccess = 0;
    public static final int onError = 1;

    private int eventType;
    private String Error;

    private String imageHeader;
    private String kategori;
    private String title;
    private String desc1;
    private String desc2;
    private String desc3;
    private String lokasi;
    private String deadline;
    private String kriteria;
    private String prosesAplikasi;
    private String formLink;

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String fromLink) {
        this.formLink = fromLink;
    }

    public static int getOnSuccess() {
        return onSuccess;
    }

    public static int getOnError() {
        return onError;
    }

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

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getKriteria() {
        return kriteria;
    }

    public void setKriteria(String kriteria) {
        this.kriteria = kriteria;
    }

    public String getProsesAplikasi() {
        return prosesAplikasi;
    }

    public void setProsesAplikasi(String prosesAplikasi) {
        this.prosesAplikasi = prosesAplikasi;
    }
}
