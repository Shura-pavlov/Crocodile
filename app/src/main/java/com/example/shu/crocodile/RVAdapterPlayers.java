package com.example.shu.crocodile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//адаптер вывода имен игроков
public class RVAdapterPlayers extends RecyclerView.Adapter<RVAdapterPlayers.PlayerViewHolder>{

    public static class PlayerViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView name;

        PlayerViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_player);
            name = (TextView)itemView.findViewById(R.id.name_player);

        }
    }

    List<String> names;

    RVAdapterPlayers(List<String> names){
        this.names = names;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player, viewGroup, false);
        PlayerViewHolder pvh = new PlayerViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder playerViewHolder, int i){
        playerViewHolder.name.setText(names.get(i));
    }

    @Override
    public int getItemCount(){
        return names.size();
    }
}

