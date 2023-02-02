package com.r3.findmestuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyViewHolder> {
    Context context;
    ArrayList<ItemHelperClass> list;

    public AdapterItem(Context context, ArrayList<ItemHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.card_view_items,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.MyViewHolder holder, int position) {
ItemHelperClass itemHelperClass = list.get(position);
        holder.itemName.setText(itemHelperClass.getIname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        ImageView itemPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_Name);
            itemPic=itemView.findViewById(R.id.item_pic);

        }
    }
}
