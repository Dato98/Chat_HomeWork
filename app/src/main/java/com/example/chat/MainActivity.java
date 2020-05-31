package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ChatFragmentA chatFragmentA;
    private ChatFragmentB chatFragmentB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatFragmentA = (ChatFragmentA) getSupportFragmentManager().findFragmentById(R.id.ChatFragmentA);
        chatFragmentB = (ChatFragmentB) getSupportFragmentManager().findFragmentById(R.id.ChatFragmentB);
    }
}
