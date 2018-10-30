package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

public class PushMessage {
    private String body;
    private String isRead;
    private String senderID;
    public PushMessage() {
    }

    public PushMessage(String body, String isRead, String senderID) {
        this.body = body;
        this.isRead = isRead;
        this.senderID = senderID;
    }

    public String getBody() {
        return body;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
