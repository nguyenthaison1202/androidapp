package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private TextView phoneUser;
    private FirebaseUser user;
    DatabaseReference reference;
    private ImageButton send;
    private EditText text_send;
    private Intent intent;
    private String phoneUserID;
    private String getMessphoneUser;
    private String getMessphoneCurrent;
    private int idProduct;
    List<Chat> mChat;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    private Chat chat;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        text_send = findViewById(R.id.text_send);
        send = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recycleViewMessage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        phoneUserID = intent.getStringExtra("idPhoneUser");
        idProduct = intent.getIntExtra("idProduct",0);

        String phoneCurrentUser = MainActivity.getPhone;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(idProduct,phoneCurrentUser,phoneUserID, msg);
                }
                else
                {
                    Toast.makeText(MessageActivity.this, "Bạn chưa nhập gì cả", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = getIntent();
                if(intent.hasExtra("object_item"))
                {
                    bundle = intent.getExtras();
                    chat = (Chat) bundle.get("object_item");
                    phoneUserID =  chat.getSender();
                    idProduct = chat.getIdProduct();
                    readMessages(phoneCurrentUser, phoneUserID, idProduct);
                }
                else
                {
                    readMessages(phoneCurrentUser, phoneUserID, idProduct);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void sendMessage(int id, String sender, String receiver, String message)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idProduct", id);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }
    private void readMessages(String myids, String userid, int id)
    {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat =  dataSnapshot.getValue(Chat.class);
                    if(chat.getIdProduct() == id)
                    {
                        if(chat.getReceiver().equals(myids) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myids))
                        {
                            mChat.add(chat);
                        }
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}