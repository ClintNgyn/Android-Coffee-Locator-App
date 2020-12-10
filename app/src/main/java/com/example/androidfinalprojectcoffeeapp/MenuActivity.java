package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends SideNavigationBar {
  
  private RecyclerView rvMenuListId;
  
  private List<MenuJsonObj> menuJsonObjsList = new ArrayList<>();
  private String mShopType, currUser;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    
    //get intent data from ShopDetails Activity
    mShopType = getIntent().getStringExtra("mShopType");
    currUser = getIntent().getStringExtra("username");
    
    //link rvMenuListId
    rvMenuListId = findViewById(R.id.rvMenuListId);
    
    //from SideNavigationBar abstract class
    drawerLayoutId = findViewById(R.id.drawLayoutId);
    navViewId = findViewById(R.id.navViewId);
    navHeader = navViewId.getHeaderView(0);
    tvNavHeaderName = navHeader.findViewById(R.id.nav_header_name);
    navHeaderPfp = navHeader.findViewById(R.id.nav_header_profile_pic);
  
    //init nav bar
    initNavBar();
    fetchNavHeaderInfo();
    
    //add menu items from json file to menuJsonObjsList
    addMenuItemsToList();
  }
  
  /**
   * Fetch json file with retrofit and add items to menuJsonObjsList.
   */
  private void addMenuItemsToList() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    MenuJsonPlaceHolder menuJsonPlaceHolder = retrofit.create(MenuJsonPlaceHolder.class);
    Call<List<MenuJsonObj>> call = menuJsonPlaceHolder.getMenuJsonPlaceHolder();
    call.enqueue(new Callback<List<MenuJsonObj>>() {
      @Override
      public void onResponse(Call<List<MenuJsonObj>> call, Response<List<MenuJsonObj>> response) {
        if (!response.isSuccessful()) {
          return;
        }
        
        //storing menu items into menuItemClassList
        menuJsonObjsList = response.body();
        
        //display menu items in recycler view
        displayMenu();
        
      }
      
      @Override
      public void onFailure(Call<List<MenuJsonObj>> call, Throwable t) {
      
      }
    });
  }
  
  private void displayMenu() {
    MenuAdapter menuAdapter = new MenuAdapter(this, menuJsonObjsList);
    rvMenuListId.setLayoutManager(new GridLayoutManager(this, 3));
    rvMenuListId.setAdapter(menuAdapter);
  }
  
  @Override
  public void initNavBar() {
    navViewId.bringToFront();
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutId, R.string.open, R.string.close);
    drawerLayoutId.addDrawerListener(toggle);
    toggle.syncState();
    navViewId.setNavigationItemSelectedListener(this);
  }
  
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
  
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav_map: {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("username", currUser);
        startActivity(intent);
        break;
      }

      case R.id.nav_favorites:
        startActivity(new Intent(this, FavoriteListActivity.class));
        break;
      
      case R.id.nav_profile: {
        Intent intent = new Intent(this, ProfileActivity.class);
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
  
  @Override
  public void onBackPressed() {
    if (drawerLayoutId.isDrawerOpen(GravityCompat.START)) {
      drawerLayoutId.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
}