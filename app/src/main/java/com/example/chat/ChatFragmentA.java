package com.example.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ChatFragmentA extends Fragment {
    private User userA;
    private EditText txtMessage;
    private Button btnSend;
    private MessageArrayAdapter messageArrayAdapter;
    private ListView listView;
    private BroadcastReceiver messageBroadcastListener;
    public static String MESSAGE = "MESSAGE";
    public static String MESSAGE_DATA = "data";
    private MessageRepository messageRepository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(userA == null)
            userA = new User("UserA");

        View view = inflater.inflate(R.layout.fragment_chat_a, container, false);
        InitBroadcast();
        txtMessage = view.findViewById(R.id.txtMessageA);
        btnSend = view.findViewById(R.id.btnSendA);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send();
            }
        });
        listView = view.findViewById(R.id.MessageListA);
        messageArrayAdapter = new MessageArrayAdapter(getActivity().getApplicationContext(),0,new ArrayList<Message>());
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
                if(intent.hasExtra(ChatFragmentB.MESSAGE_DATA)){
                    String message = intent.getStringExtra(ChatFragmentB.MESSAGE_DATA);
                    Receive(new Message(message,new User("userB")));
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatFragmentB.MESSAGE);
        getActivity().registerReceiver(messageBroadcastListener,filter);
    }

    private void GetMessageHistory(){
        if(messageArrayAdapter.getCount() == 0) {
            ArrayList<Message> messages = messageRepository.GetMessageList(getActivity().getApplicationContext(), userA);
            if(messages != null)
                messageArrayAdapter.addAll(messages);
        }
    }

    private void Send(){
        if(txtMessage.getText().length() != 0) {
            Message message = new Message(txtMessage.getText().toString(), userA);
            Intent intent = new Intent();
            intent.setAction(MESSAGE);
            intent.putExtra(MESSAGE_DATA,message.getText());
            getActivity().sendBroadcast(intent);
            messageArrayAdapter.add(message);
            messageRepository.SaveMessage(getActivity().getApplicationContext(),message,userA);
            txtMessage.setText("");
        }
    }

    private void Receive(Message message){
        messageArrayAdapter.add(message);
        messageRepository.SaveMessage(getActivity().getApplicationContext(),message,userA);
    }


    class MessageArrayAdapter extends ArrayAdapter<Message> {
        Context mContext;
        public MessageArrayAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
            super(context, resource, objects);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Message message = getItem(position);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            TextView txtMessage;
            if(message.getSender().getName().equals(userA.getName())){
                view = inflater.inflate(R.layout.message_item_send,parent,false);
                txtMessage = view.findViewById(R.id.txtSend);
            }
            else {
                view = inflater.inflate(R.layout.message_item_receive, parent, false);
                txtMessage = view.findViewById(R.id.txtReceive);
            }

            txtMessage.setText(message.getText());

            return view;
        }
    }
}
