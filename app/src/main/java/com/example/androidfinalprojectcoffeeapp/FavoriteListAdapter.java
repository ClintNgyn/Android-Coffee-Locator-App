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

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private Context context;
    private List<ShopJsonObj> favoriteList;
    private String currentUser;

    public FavoriteListAdapter(Context context, List<ShopJsonObj> favoriteList, String currentUser) {
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
        ShopJsonObj currShop = favoriteList.get(position);
        holder.favoriteType.setText(currShop.getType());
        holder.favoriteAddressId.setText(currShop.getAddress());
        
        //shop detail activity onclick handler
        holder.favoriteListCardView.setOnClickListener(view -> openShopDetailsActivity(currShop));
        holder.favoriteListInfoButton.setOnClickListener(view -> openShopDetailsActivity(currShop));
        
        //trash icon onclick handler
        holder.ivTrashId.setOnClickListener(view -> {
            //TODO: @Dennis
            currShop.setHeartChecked(false);
            FirebaseDatabase.getInstance().getReference("favorites").child(currentUser).child(currShop.getPhoneNumber()).removeValue();
            //multiple toogles still happen this is a quick fix
            Intent favoriteIntent = new Intent(context, FavoriteListActivity.class);
            favoriteIntent.putExtra("username", currentUser);
            context.startActivity(favoriteIntent);
        });

    }
    private void openShopDetailsActivity(ShopJsonObj currShop) {
        Intent i = new Intent(context, ShopDetailsActivity.class);
        i.putExtra("currShop", (Serializable) currShop);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView favoriteListCardView;
        private TextView favoriteType, favoriteAddressId;
        private ImageView favoriteListInfoButton ,ivTrashId;
        
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
    
            favoriteListCardView = itemView.findViewById(R.id.favoriteListCardView);
            favoriteType = itemView.findViewById(R.id.favoriteTypeId);
            favoriteAddressId = itemView.findViewById(R.id.favoriteAddressId);
            favoriteListInfoButton = itemView.findViewById(R.id.favoriteListInfoButton);
            ivTrashId = itemView.findViewById(R.id.ivTrashId);
        }
    }
}
