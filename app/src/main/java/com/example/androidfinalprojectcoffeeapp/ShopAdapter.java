package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolderOfMapsRecyclerView> {
  private Context context;
  private List<ShopJsonObj> shopsList;
  
  public ShopAdapter(Context context, List<ShopJsonObj> shopsList) {
    this.context = context;
    this.shopsList = shopsList;
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
    
    // set tvTypeId
    holder.tvTypeId.setText(currShop.getType());
    
    // set tvAddressId
    holder.tvAddressId.setText(currShop.getAddress());
    
    //set init heart image
    holder.ivFavsId.setImageResource(currShop.isHeartChecked() ? R.drawable.ic_heart_colored : R.drawable.ic_heart_uncolored);
    
    // set ivFavsId onclick handler
    holder.ivFavsId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean isHeartChecked = currShop.isHeartChecked();
        
        // toggle heart
        holder.ivFavsId.setImageResource(isHeartChecked ? R.drawable.ic_heart_uncolored : R.drawable.ic_heart_colored);
        
        // TODO: add or remove from favorite places
        
        //set isHeartChecked to !isHeartChecked
        currShop.setHeartChecked(!isHeartChecked);
      }
    });
    
    // set ivInfoBtnId onclick handler
    holder.ivInfoBtnId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // open ShopDetails Activity
        openShopDetailsActivity(currShop);
      }
    });
    
    // set cvCardViewId onclick handler
    holder.cvCardViewId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // open ShopDetails Activity
        openShopDetailsActivity(currShop);
      }
    });
  }
  
  private void openShopDetailsActivity(ShopJsonObj currShop) {
    Intent i = new Intent(context, ShopDetailsActivity.class);
    i.putExtra("currShop", (Serializable) currShop);
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
