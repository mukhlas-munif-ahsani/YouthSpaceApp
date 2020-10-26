package com.munifahsan.youthspaceapp.Home;

public class PromoModel {
    String nTitle;
    String nImage;

    public PromoModel() {
    }

    public PromoModel(String nTitle, String nImage) {
        this.nTitle = nTitle;
        this.nImage = nImage;
    }

    public String getnImage() {
        return nImage;
    }

    public String getnTitle() {
        return nTitle;
    }
}
