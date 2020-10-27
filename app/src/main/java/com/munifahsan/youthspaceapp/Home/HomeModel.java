package com.munifahsan.youthspaceapp.Home;

import com.google.firebase.firestore.DocumentId;

public class HomeModel {
    @DocumentId
    String id;
    String nNama;
    String nImageUrl;
    String nTitle;
    String nDesc;
    String nGreating;
    String nHeaderCardText1;
    String nHeaderCardText2;
    String nWelcomeImageUrl;

    public HomeModel() {
    }

    public HomeModel(String id, String nImageUrl, String nTitle) {
        this.id = id;
        this.nImageUrl = nImageUrl;
        this.nTitle = nTitle;
    }

    public String getId() {
        return id;
    }

    public String getnNama() {
        return nNama;
    }

    public String getnImageUrl() {
        return nImageUrl;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnDesc() {
        return nDesc;
    }

    public String getnGreating() {
        return nGreating;
    }

    public String getnHeaderCardText1() {
        return nHeaderCardText1;
    }

    public String getnHeaderCardText2() {
        return nHeaderCardText2;
    }

    public String getnWelcomeImageUrl() {
        return nWelcomeImageUrl;
    }
}
