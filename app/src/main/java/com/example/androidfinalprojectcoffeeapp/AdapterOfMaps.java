package com.example.androidfinalprojectcoffeeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOfMaps extends RecyclerView.Adapter<AdapterOfMaps.ViewHolderOfMapsRecyclerView> {
    ArrayList<ExampleItemMaps> list;

    public AdapterOfMaps(ArrayList<ExampleItemMaps> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderOfMapsRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exampleitem_maps, parent, false);
        ViewHolderOfMapsRecyclerView viewHolder = new ViewHolderOfMapsRecyclerView(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOfMapsRecyclerView holder, int position) {
        ExampleItemMaps item = list.get(position);
        holder.address.setText(item.getAddress());
        holder.type.setText(item.getType());
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolderOfMapsRecyclerView extends RecyclerView.ViewHolder{
        public TextView address;
        public TextView type;
        public ImageView heart;
        public ImageView info;
        public ViewHolderOfMapsRecyclerView(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.exampleitem_address);
            type = itemView.findViewById(R.id.exampleitem_type);
            heart = itemView.findViewById(R.id.exampleitem_favorite);
            info = itemView.findViewById(R.id.exampleitem_info_btn);
        }
    }
}
