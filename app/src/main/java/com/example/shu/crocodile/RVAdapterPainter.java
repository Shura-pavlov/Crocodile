package com.example.shu.crocodile;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//адаптер вывода сообщений чата
public class RVAdapterPainter extends RecyclerView.Adapter<RVAdapterPainter.MessageViewHolder>{

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


    RVAdapterPainter(List<Message> messages, String cookie){
        this.cookie = cookie;
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
    public void onBindViewHolder(final MessageViewHolder messageViewHolder, int i){
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

        messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageViewHolder.markMessage == null){

                    messageViewHolder.itemView.setBackgroundColor(Color.parseColor("#ff99cc00"));

                    Connection_Post_Mark mark_message = new Connection_Post_Mark();
                    mark_message.cookie = cookie;
                    mark_message.urll = "http://croco.us-west-2.elasticbeanstalk.com/api/lobby/mark/" + Integer.toString(messageViewHolder.n);
                    mark_message.execute("marked=1");
                    messageViewHolder.markMessage = true;
                }
                else {
                    if (messageViewHolder.markMessage) {

                        messageViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffff4444"));

                        Connection_Post_Mark mark_message = new Connection_Post_Mark();
                        mark_message.cookie = cookie;
                        mark_message.urll = "http://croco.us-west-2.elasticbeanstalk.com/api/lobby/mark/" + Integer.toString(messageViewHolder.n);
                        mark_message.execute("marked=0");
                        messageViewHolder.markMessage = false;
                    }
                    else {
                            messageViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffffff"));

                            Connection_Post_Mark mark_message = new Connection_Post_Mark();
                            mark_message.cookie = cookie;
                            mark_message.urll = "http://croco.us-west-2.elasticbeanstalk.com/api/lobby/mark/" + Integer.toString(messageViewHolder.n);
                            mark_message.execute("");
                            messageViewHolder.markMessage = null;
                        }
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }
}
