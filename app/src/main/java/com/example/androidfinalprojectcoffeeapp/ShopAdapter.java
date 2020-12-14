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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolderOfMapsRecyclerView> {
  private final Context context;
  private final List<ShopJsonObj> shopsList;
  private final String currentUser;
  
  public ShopAdapter(Context context, List<ShopJsonObj> shopsList, String currentUser) {
    this.context = context;
    this.shopsList = shopsList;
    this.currentUser = currentUser;
  }
  
  @NonNull
  @Override
  public ViewHolderOfMapsRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.recycler_shops_item, parent, false);
    return new ViewHolderOfMapsRecyclerView(view);
  }
  
  @Override
  public void onBindViewHolder(@NonNull ViewHolderOfMapsRecyclerView holder, int position) {
    ShopJsonObj currShop = shopsList.get(position);
  
    //check isHeartChecked
    FirebaseDatabase.getInstance().getReference("favorites/" + currentUser).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot d : snapshot.getChildren()) {
          if (d.getKey().equals(currShop.getPhoneNumber())) {
            currShop.setHeartChecked(true);
          }
        }
  
        holder.tvTypeId.setText(currShop.getType());
        holder.tvAddressId.setText(currShop.getAddress());
        holder.ivFavsId.setImageResource(currShop.isHeartChecked() ? R.drawable.ic_heart_colored : R.drawable.ic_heart_uncolored);
        holder.tvDistanceId.setText(currShop.getDistance() + " km away");
      }
  
      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      
      }
    });
  
    // set ivFavsId onclick handler
    holder.ivFavsId.setOnClickListener(view -> {
      boolean isHeartChecked = currShop.isHeartChecked();
      DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("favorites");
  
      if (!isHeartChecked) {
        holder.ivFavsId.setImageResource(R.drawable.ic_heart_colored);
        currShop.setHeartChecked(true);
        mDatabaseRef.push().getKey();
        mDatabaseRef.child(currentUser).child(currShop.getPhoneNumber()).setValue(currShop);
      } else {
        holder.ivFavsId.setImageResource(R.drawable.ic_heart_uncolored);
        currShop.setHeartChecked(false);
        mDatabaseRef.child(currentUser).child(currShop.getPhoneNumber()).removeValue();
      }
      
      notifyItemChanged(position);
    });
    
    // set ivInfoBtnId onclick handler
    holder.ivInfoBtnId.setOnClickListener(view -> {
      // open ShopDetails Activity
      openShopDetailsActivity(currShop);
    });
    
    // set cvCardViewId onclick handler
    holder.cvCardViewId.setOnClickListener(view -> {
      // open ShopDetails Activity
      openShopDetailsActivity(currShop);
    });
  }
  
  private void openShopDetailsActivity(ShopJsonObj currShop) {
    Intent i = new Intent(context, ShopDetailsActivity.class);
    i.putExtra("currShop", currShop);
    context.startActivity(i);
  }
  
  @Override
  public int getItemCount() {
    return shopsList.size();
  }
  
  
  public static class ViewHolderOfMapsRecyclerView extends RecyclerView.ViewHolder {
    private final CardView cvCardViewId;
    private final ImageView ivFavsId;
    private final ImageView ivInfoBtnId;
    private final TextView tvTypeId;
    private final TextView tvAddressId;
    private final TextView tvDistanceId;
    
    
    private ViewHolderOfMapsRecyclerView(@NonNull View itemView) {
      super(itemView);
      
      //link Views
      cvCardViewId = itemView.findViewById(R.id.cvCardViewId);
      ivFavsId = itemView.findViewById(R.id.ivFavsId);
      tvTypeId = itemView.findViewById(R.id.tvTypeId);
      tvAddressId = itemView.findViewById(R.id.tvAddressId);
      ivInfoBtnId = itemView.findViewById(R.id.ivInfoBtnId);
      tvDistanceId = itemView.findViewById(R.id.tvDistanceId);
    }
  }
}
