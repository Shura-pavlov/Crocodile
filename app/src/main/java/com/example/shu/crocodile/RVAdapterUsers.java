package com.example.shu.crocodile;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapterUsers extends RecyclerView.Adapter<RVAdapterUsers.MessageViewHolder>{

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView messageText;
        TextView senderName;
        Boolean markMessage;
        int n;

        MessageViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            messageText = (TextView)itemView.findViewById(R.id.message_text);
            senderName = (TextView)itemView.findViewById(R.id.sender_name);

        }
    }

    List<Message> messages;
    String cookie;


    RVAdapterUsers(List<Message> messages, String cookie){
        this.cookie = cookie;
        this.messages = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RVAdapterUsers.MessageViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        RVAdapterUsers.MessageViewHolder mvh = new RVAdapterUsers.MessageViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final RVAdapterUsers.MessageViewHolder messageViewHolder, int i){
        messageViewHolder.messageText.setText(messages.get(i).text);
        messageViewHolder.senderName.setText(messages.get(i).sender);
        messageViewHolder.markMessage = messages.get(i).mark;
        messageViewHolder.n = messages.get(i).number;
        if (messages.get(i).mark != null){
            if (messages.get(i).mark){
                messageViewHolder.itemView.setBackgroundColor(Color.parseColor("#ff99cc00"));
                messageViewHolder.markMessage = true;

            }else{
                messageViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffff4444"));
                messageViewHolder.markMessage = false;
            }
        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }
}
