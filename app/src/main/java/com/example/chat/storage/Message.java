package com.example.chat.storage;

public class Message {
    private String text;
    private User Sender;

    public Message(String txt,User sender){
        text = txt;
        Sender = sender;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return Sender;
    }

}
