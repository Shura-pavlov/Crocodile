package com.example.shu.crocodile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//адаптер вывода сообщений чата
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MessageViewHolder>{

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView messageText;
        TextView senderName;

        MessageViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            messageText = (TextView)itemView.findViewById(R.id.message_text);
            senderName = (TextView)itemView.findViewById(R.id.sender_name);

        }
    }

    List<Message> messages;

    RVAdapter(List<Message> messages){
        this.messages = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MessageViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        MessageViewHolder mvh = new MessageViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int i){
        messageViewHolder.messageText.setText(messages.get(i).text);
        messageViewHolder.senderName.setText(messages.get(i).sender);
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }
}
