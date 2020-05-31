package com.example.chat.storage;

import android.content.Context;

import java.util.ArrayList;

public class MessageRepository {


    public void SaveMessage(Context context,Message message,User user){
        IStorage storage = new StorageSharedPreference();
        storage.save(context,user.getName(),message);
    }

    public ArrayList<Message> GetMessageList(Context context,User user){
        IStorage storage = new StorageSharedPreference();
        return storage.get(context,user.getName());
    }
}
