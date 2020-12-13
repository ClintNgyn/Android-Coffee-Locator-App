package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private Context context;
    private List<FavoriteListObj> favoriteList;
    private String currentUser;

    public FavoriteListAdapter(Context context, List<FavoriteListObj> favoriteList, String currentUser) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public FavoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_favorite_item, parent, false);
        return new FavoriteListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView favoriteTypeId;
        TextView favoriteAddressId;
        ImageView favoriteListInfoButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteTypeId = itemView.findViewById(R.id.favoriteTypeId);
            favoriteAddressId = itemView.findViewById(R.id.favoriteAddressId);
            favoriteListInfoButton = itemView.findViewById(R.id.favoriteListInfoButton);
        }
    }
}
