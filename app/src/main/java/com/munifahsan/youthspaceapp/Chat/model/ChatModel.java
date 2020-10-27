package com.munifahsan.youthspaceapp.Chat.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChatModel {

    @DocumentId
    private String id;
    private String nSenderId;
    private String nMessage;

    @ServerTimestamp
    private Date nSentTime;

    public ChatModel() {
    }

    public ChatModel(String nSenderId, String nMessage) {
        this.nSenderId = nSenderId;
        this.nMessage = nMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnSenderId() {
        return nSenderId;
    }

    public void setnSenderId(String nSenderId) {
        this.nSenderId = nSenderId;
    }

    public String getnMessage() {
        return nMessage;
    }

    public void setnMessage(String nMessage) {
        this.nMessage = nMessage;
    }

    public Date getnSentTime() {
        return nSentTime;
    }

    public void setnSentTime(Date nSentTime) {
        this.nSentTime = nSentTime;
    }
}
