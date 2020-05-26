package com.example.weatherproject.model;

public class Chat {
    private String sender;
    private String reciver;
    private String edtsent;

    public Chat(){

    }

    public Chat(String sender, String reciver, String edtsent) {
        this.sender = sender;
        this.reciver = reciver;
        this.edtsent = edtsent;
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

    public String toString(){
        return "(" + edtsent + ")";
    }
}
