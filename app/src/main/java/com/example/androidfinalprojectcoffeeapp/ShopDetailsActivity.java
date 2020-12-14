package com.example.androidfinalprojectcoffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopDetailsActivity extends SideNavigationBar {
  
  private String currUser;
  private ShopJsonObj mShop;
  
  private ImageView ivShopImageId;
  private TextView tvTypeId, tvAddressId, tvPhoneId, tvIsHeartCheckedId;
  Button btnSeeMenuId;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shop_details);
    
    //get intent data from ShopAdapter Activity
    mShop = (ShopJsonObj) getIntent().getSerializableExtra("currShop");
    currUser = getIntent().getStringExtra("username");
    
    //link views
    ivShopImageId = findViewById(R.id.ivShopImageId);
    tvTypeId = findViewById(R.id.tvTypeId);
    tvAddressId = findViewById(R.id.tvAddressId);
    tvPhoneId = findViewById(R.id.tvPhoneId);
    tvPhoneId = findViewById(R.id.tvPhoneId);
    tvIsHeartCheckedId = findViewById(R.id.tvIsHeartCheckedId);
    btnSeeMenuId = findViewById(R.id.btnSeeMenuId);
    
    //from SideNavigationBar abstract class
    drawerLayoutId = findViewById(R.id.drawLayoutId);
    navViewId = findViewById(R.id.navViewId);
    navHeader = navViewId.getHeaderView(0);
    tvNavHeaderName = navHeader.findViewById(R.id.nav_header_name);
    navHeaderPfp = navHeader.findViewById(R.id.nav_header_profile_pic);
    
    //init nav bar
    initNavBar();
    fetchNavHeaderInfo();
    
    //display shop's detail
    displayShopsData();
  }
  
  /**
   * Setting up navigation bar so that it can be interacted with.
   */
  @Override
  public void initNavBar() {
    navViewId.bringToFront();
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutId, R.string.open, R.string.close);
    drawerLayoutId.addDrawerListener(toggle);
    toggle.syncState();
    navViewId.setNavigationItemSelectedListener(this);
  }
  
  /**
   * Fetches user's data and display in nav's header.
   */
  @Override
  public void fetchNavHeaderInfo() {
    if (currUser == null) {
      return;
    }
    
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
    mDatabaseRef.child(currUser).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String fName = String.valueOf(snapshot.child("fName").getValue());
        String lName = String.valueOf(snapshot.child("lName").getValue());
        tvNavHeaderName.setText(fName + " " + lName);
        Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(navHeaderPfp);
      }
      
      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      
      }
    });
  }
  
  /**
   * Navigation bar switch activity.
   *
   * @param item menu item being selected.
   * @return true when navigation bar is in focus.
   */
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //open activities for the rest of nav items
    switch (item.getItemId()) {
      case R.id.nav_map: {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("username", currUser);
        startActivity(intent);
        break;
      }
      
      case R.id.nav_profile: {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", currUser);
        startActivity(intent);
        break;
      }
      
      case R.id.favorite_list_nav: {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        intent.putExtra("username", currUser);
        startActivity(intent);
        break;
      }
      
      case R.id.nav_signOut:
        startActivity(new Intent(this, MainActivity.class));
        finish();
        break;
    }
    drawerLayoutId.closeDrawer(GravityCompat.START);
    return true;
  }
  
  /**
   * App will not close when back button is press when nav is in focus.
   */
  @Override
  public void onBackPressed() {
    if (drawerLayoutId.isDrawerOpen(GravityCompat.START)) {
      drawerLayoutId.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
  
  /**
   * Display shop's details such as address, phone number etc.
   */
  private void displayShopsData() {
    //set image
    Picasso.get().load(mShop.getImg()).into(ivShopImageId);
    
    //set type
    tvTypeId.setText(mShop.getType());
    
    //set address
    tvAddressId.setText(mShop.getAddress());
    
    //set phone number
    tvPhoneId.setText(mShop.getPhoneNumber());
    
    //set tvIsHeartCheckedId
    tvIsHeartCheckedId.setText(mShop.isHeartChecked() + "");
    
    //set see menu onclick handler
    btnSeeMenuId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: change menu items base on shop's type i.e. Starbucks, Second Cup etc.
        
        // open menu activity
        Intent i = new Intent(ShopDetailsActivity.this, MenuActivity.class);
        i.putExtra("mShopType", mShop.getType());
        ShopDetailsActivity.this.startActivity(i);
      }
    });
  }
  
  
}