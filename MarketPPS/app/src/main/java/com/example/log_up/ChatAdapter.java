package com.example.log_up;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder>{
    private Context context;
    private ArrayList<Chat> chatList;
    private DatabaseReference reference;
    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Chat> list) {
        this.chatList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_sender, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ItemViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.nameSender.setText(chat.getSender());
        holder.textMsg.setText(chat.getMessage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChat(chat);
            }
        });
    }
    private void gotoChat(Chat chat)
    {
        Intent intent = new Intent(context, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_item", chat);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUsr;
        private TextView nameSender, textMsg;
        private MaterialCardView cardView;
        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            this.setIsRecyclable(false);
            cardView = itemView.findViewById(R.id.layout_item_chat);
            imgUsr = itemView.findViewById(R.id.imgSender);
            nameSender = itemView.findViewById(R.id.tvNameSender);
            textMsg = itemView.findViewById(R.id.tvMsg);
        }
    }
}
