package com.example.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chat.storage.IStorage;
import com.example.chat.storage.Message;
import com.example.chat.storage.MessageRepository;
import com.example.chat.storage.StorageSharedPreference;
import com.example.chat.storage.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ChatFragmentB extends Fragment {
    private User userB;
    private EditText txtMessage;
    private Button btnSend;
    private ListView listView;
    private  MessageArrayAdapter messageArrayAdapter;
    public static String MESSAGE = "MESSAGEB";
    public static String MESSAGE_DATA = "data";
    private BroadcastReceiver messageBroadcastListener;
    private MessageRepository messageRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(userB == null)
            userB = new User("UserB");
        View view = inflater.inflate(R.layout.fragment_chat_b, container, false);
        InitBroadcast();
        txtMessage = view.findViewById(R.id.txtMessageB);
        btnSend = view.findViewById(R.id.btnSendB);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send();
            }
        });
        messageArrayAdapter = new MessageArrayAdapter(getActivity().getApplicationContext(),0,new ArrayList<Message>());
        listView = view.findViewById(R.id.MessageListB);
        listView.setAdapter(messageArrayAdapter);
        messageRepository = new MessageRepository();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetMessageHistory();
    }

    void InitBroadcast(){
        messageBroadcastListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.hasExtra(ChatFragmentA.MESSAGE_DATA)){
                    String message = intent.getStringExtra(ChatFragmentA.MESSAGE_DATA);
                    Receive(new Message(message,new User("userA")));
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatFragmentA.MESSAGE);
        getActivity().registerReceiver(messageBroadcastListener,filter);
    }

    private void GetMessageHistory(){
        if(messageArrayAdapter.getCount() == 0) {
            ArrayList<Message> messages = messageRepository.GetMessageList(getActivity().getApplicationContext(), userB);
            if(messages != null)
                messageArrayAdapter.addAll(messages);
        }
    }

    private void Receive(Message message){
        messageArrayAdapter.add(message);
        messageRepository.SaveMessage(getActivity().getApplicationContext(),message,userB);
    }


    private void Send(){
        if(txtMessage.getText().length() != 0) {
            Message message = new Message(txtMessage.getText().toString(), userB);
            Intent intent = new Intent();
            intent.setAction(MESSAGE);
            intent.putExtra(MESSAGE_DATA, message.getText());
            getActivity().sendBroadcast(intent);
            messageArrayAdapter.add(message);
            messageRepository.SaveMessage(getActivity().getApplicationContext(),message,userB);
            txtMessage.setText("");
        }
    }




    class MessageArrayAdapter extends ArrayAdapter<Message>{
        Context mContext;
        public MessageArrayAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
            super(context, resource, objects);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Message message = getItem(position);
            View view;
            TextView txtMessage;
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(userB.getName().equals(message.getSender().getName())){
                view = inflater.inflate(R.layout.message_item_send,parent,false);
                txtMessage = view.findViewById(R.id.txtSend);
            }else {
                view = inflater.inflate(R.layout.message_item_receive,parent,false);
                txtMessage = view.findViewById(R.id.txtReceive);
            }
            txtMessage.setText(message.getText());
            return view;
        }
    }
}
