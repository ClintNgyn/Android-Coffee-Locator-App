package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context;
    private List<MenuJsonObj> menuJsonObjsList;

    public MenuAdapter(Context context, List<MenuJsonObj> menuJsonObjsList) {
        this.context = context;
        this.menuJsonObjsList = menuJsonObjsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuJsonObj currMenu = menuJsonObjsList.get(position);
        
        //set ivFoodImageId
        Picasso.get().load(currMenu.getImageUrl()).into(holder.ivFoodImageId);
        
        //set tvFoodNameId
        holder.tvFoodNameId.setText(currMenu.getName());
        
        //set card onclick handler
        holder.cardViewId.setOnClickListener(view -> {
            //open menu's detail
            Intent i = new Intent(context, MenuDetailsActivity.class);
            i.putExtra("currMenu", currMenu);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return menuJsonObjsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivFoodImageId;
        private TextView tvFoodNameId;
        private CardView cardViewId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //link Views
            ivFoodImageId = itemView.findViewById(R.id.ivFoodImageId);
            tvFoodNameId = itemView.findViewById(R.id.tvFoodNameId);
            cardViewId = itemView.findViewById(R.id.cardViewId);
        }
    }
}
