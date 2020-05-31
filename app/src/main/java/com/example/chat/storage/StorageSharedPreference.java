package com.example.chat.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageSharedPreference implements IStorage {
    @Override
    public void save(Context context, String key, Message value) {
        ArrayList<Message> list = get(context,key);
        list.add(value);
        SharedPreferences sharedPreferences = getInstance(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,new Gson().toJson(list));
        editor.commit();
    }

    @Override
    public ArrayList<Message> get(Context context, String key) {
        SharedPreferences sharedPreferences = getInstance(context);
        String data = sharedPreferences.getString(key,null);
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        ArrayList<Message> list = new Gson().fromJson(data,listType);
        if(list == null)
            return new ArrayList<>();
        return list;
    }

    private SharedPreferences getInstance(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        return sharedPref;
    }
}
