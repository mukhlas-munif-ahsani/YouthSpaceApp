package com.munifahsan.youthspaceapp.Chat.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class ChatListModel {
    @DocumentId
    String id;
    String nImage;
    String nNama;
    String nPeakMsg;
    String nTime;
    String nFrom;
    String nTo;
    Date nUpdatedAt;

    public ChatListModel() {
    }

    public ChatListModel(String id, String nImage, String nNama, String nPeakMsg, String nTime, String nFrom, String nTo) {
        this.id = id;
        this.nImage = nImage;
        this.nNama = nNama;
        this.nPeakMsg = nPeakMsg;
        this.nTime = nTime;
        this.nFrom = nFrom;
        this.nTo = nTo;
    }

    public String getId() {
        return id;
    }

    public String getnImage() {
        return nImage;
    }

    public String getnNama() {
        return nNama;
    }

    public String getnPeakMsg() {
        return nPeakMsg;
    }

    public String getnTime() {
        return nTime;
    }

    public String getnFrom() {
        return nFrom;
    }

    public String getnTo() {
        return nTo;
    }

    public Date getnUpdatedAt() {
        return nUpdatedAt;
    }
}
