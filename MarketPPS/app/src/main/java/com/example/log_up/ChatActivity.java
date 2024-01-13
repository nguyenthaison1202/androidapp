package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rcvChat;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> listChat;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rcvChat = findViewById(R.id.rcvChat);
        chatAdapter = new ChatAdapter(this);
        listChat = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvChat.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvChat.addItemDecoration(itemDecoration);
        chatAdapter.setData(listChat);
        rcvChat.setAdapter(chatAdapter);
        getItems();
    }

    private void getItems() {
        Intent getPhone = getIntent();
        String getPhoneUser = getPhone.getStringExtra("phoneUser");
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String getReceiver = dataSnapshot.child("receiver").getValue(String.class);
                    if(getReceiver != null && getReceiver.equals(getPhoneUser))
                    {
                        Chat chatItem = dataSnapshot.getValue(Chat.class);
                        if(listChat.size()>0)
                        {
                            boolean check = false;
                            for(int i = 0; i< listChat.size(); i++)
                            {
                                if(listChat.get(i).getIdProduct() != chatItem.getIdProduct() || !listChat.get(i).getSender().equals(chatItem.getSender()))
                                {
                                        check = true;
                                }
                                else
                                {
                                    listChat.set(i, chatItem);
                                    check = false;
                                    break;
                                }
                            }
                            if(check == true)
                            {
                                listChat.add(chatItem);
                            }
                        }
                        else
                        {
                            listChat.add(chatItem);
                        }
                    }

                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}