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

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolderOfMapsRecyclerView> {
  private Context context;
  private List<ShopJsonObj> list;
  
  public ShopAdapter(Context context, List<ShopJsonObj> list) {
    this.context = context;
    this.list = list;
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
    ShopJsonObj currShop = list.get(position);
  
    // set tvTypeId
    holder.tvTypeId.setText(currShop.getType());
  
    // set tvAddressId
    holder.tvAddressId.setText(currShop.getAddress());
  
    // set ivFavsId onclick handler
    holder.ivFavsId.setOnClickListener((View view) -> {
      boolean isHeartChecked = currShop.isHeartChecked();
    
      // toggle heart
      holder.ivFavsId.setImageResource(isHeartChecked ? R.drawable.ic_heart_uncolored : R.drawable.ic_heart_colored);
    
      // TODO: add or remove from favorite places
    
      //set isHeartChecked to !isHeartChecked
      currShop.setHeartChecked(!isHeartChecked);
    });
  
    // TODO: set ivInfoBtnId onclick handler
    holder.ivInfoBtnId.setOnClickListener((View view) -> {
      // see detailed view of shop
    });
  }
  
  @Override
  public int getItemCount() {
    return list.size();
  }
  
  
  public static class ViewHolderOfMapsRecyclerView extends RecyclerView.ViewHolder {
    private ImageView ivFavsId, ivInfoBtnId;
    private TextView tvTypeId, tvAddressId;
    
    private ViewHolderOfMapsRecyclerView(@NonNull View itemView) {
      super(itemView);
      
      //link Views
      ivFavsId = itemView.findViewById(R.id.ivFavsId);
      tvTypeId = itemView.findViewById(R.id.tvTypeId);
      tvAddressId = itemView.findViewById(R.id.tvAddressId);
      ivInfoBtnId = itemView.findViewById(R.id.ivInfoBtnId);
    }
  }
}
