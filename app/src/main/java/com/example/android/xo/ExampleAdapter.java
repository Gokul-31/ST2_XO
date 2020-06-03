package com.example.android.xo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<Rank> mRanks;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView t1Name;
        public TextView t2Num;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            t1Name=itemView.findViewById(R.id.listName);
            t2Num=itemView.findViewById(R.id.listWins);
        }
    }

    public ExampleAdapter(ArrayList<Rank> ranks){
        mRanks=ranks;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
        //viewholder created
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Rank currRankItem=mRanks.get(position);

        holder.t1Name.setText(currRankItem.getName());
        holder.t2Num.setText(String.valueOf(currRankItem.getR()));
    }

    @Override
    public int getItemCount() {
        return mRanks.size();
    }
}
