package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolderOfMapsRecyclerView> {
  private Context context;
  private List<ShopJsonObj> shopsList;
  private String currentUser;
  
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
    holder.tvTypeId.setText(shopsList.get(position).getType());
    holder.tvAddressId.setText(shopsList.get(position).getAddress());
    holder.ivFavsId.setImageResource(shopsList.get(position).isHeartChecked() ?
                                     R.drawable.ic_heart_colored :
                                     R.drawable.ic_heart_uncolored);
    
    // set ivFavsId onclick handler
    holder.ivFavsId.setOnClickListener(view -> {
      boolean isHeartChecked = shopsList.get(position).isHeartChecked();
      DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("favorites");
      
      if (!isHeartChecked) {
        holder.ivFavsId.setImageResource(R.drawable.ic_heart_colored);
        shopsList.get(position).setHeartChecked(true);
        mDatabaseRef.push().getKey();
        mDatabaseRef.child(currentUser).child(shopsList.get(position).getPhoneNumber()).setValue(shopsList.get(position));
      } else {
        holder.ivFavsId.setImageResource(R.drawable.ic_heart_uncolored);
        shopsList.get(position).setHeartChecked(false);
        mDatabaseRef.child(currentUser).child(shopsList.get(position).getPhoneNumber()).removeValue();
      }
      
      notifyItemChanged(position);
    });
    
    // set ivInfoBtnId onclick handler
    holder.ivInfoBtnId.setOnClickListener(view -> {
      // open ShopDetails Activity
      openShopDetailsActivity(shopsList.get(position));
    });
    
    // set cvCardViewId onclick handler
    holder.cvCardViewId.setOnClickListener(view -> {
      // open ShopDetails Activity
      openShopDetailsActivity(shopsList.get(position));
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
    private CardView cvCardViewId;
    private ImageView ivFavsId, ivInfoBtnId;
    private TextView tvTypeId, tvAddressId;
    
    
    private ViewHolderOfMapsRecyclerView(@NonNull View itemView) {
      super(itemView);
      
      //link Views
      cvCardViewId = itemView.findViewById(R.id.cvCardViewId);
      ivFavsId = itemView.findViewById(R.id.ivFavsId);
      tvTypeId = itemView.findViewById(R.id.tvTypeId);
      tvAddressId = itemView.findViewById(R.id.tvAddressId);
      ivInfoBtnId = itemView.findViewById(R.id.ivInfoBtnId);
    }
  }
}
