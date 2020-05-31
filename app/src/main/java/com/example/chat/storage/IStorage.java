package com.example.chat.storage;

import android.content.Context;

import java.util.ArrayList;

public interface IStorage {
    void save(Context context, String key, Message value);
    ArrayList<Message> get(Context context,String key);
}
