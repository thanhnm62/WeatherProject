package com.example.weatherproject.model;

public class Chat {
    private String sender;
    private String reciver;
    private String edtsent;
    private boolean isseenSender;
    private boolean isseenReceiver;

    public Chat(){

    }

    public Chat(String sender, String reciver, String edtsent, boolean isseenSender, boolean isseenReceiver) {
        this.sender = sender;
        this.reciver = reciver;
        this.edtsent = edtsent;
        this.isseenSender = isseenSender;
        this.isseenReceiver = isseenReceiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getEdtsent() {
        return edtsent;
    }

    public void setEdtsent(String edtsent) {
        this.edtsent = edtsent;
    }

    public boolean getIsseenSender() {
        return isseenSender;
    }

    public void setIsseenSender(boolean isseenSender) {
        this.isseenSender = isseenSender;
    }

    public boolean getIsseenReceiver() {
        return isseenReceiver;
    }

    public void setSeenReceiver(boolean isseenReceiver) {
        isseenReceiver = isseenReceiver;
    }

    public String toString(){
        return "(" + edtsent + ")";
    }
}
