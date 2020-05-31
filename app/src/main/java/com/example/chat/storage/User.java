package com.example.chat.storage;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<Message> History;

    public User(String name){
        this.name = name;
        History = new ArrayList<>();
    }

    public ArrayList<Message> getHistory() {
        return History;
    }

    public String getName() {
        return name;
    }

    public void setHistory(ArrayList<Message> history) {
        History = history;
    }
}
