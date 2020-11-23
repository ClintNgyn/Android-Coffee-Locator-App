package com.example.androidfinalprojectcoffeeapp;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
  
  private GoogleMap mMap;
  
  private List<ShopJsonObj> shopJsonObjsList;
  
  private RecyclerView rvShopsListId;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    
    //link views
    rvShopsListId = findViewById(R.id.rvShopsListId);
    
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    
    //get shops from json
    getShops();
    
  }
  
  private void getShops() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    ShopJsonPlaceHolder shopJsonPlaceHolder = retrofit.create(ShopJsonPlaceHolder.class);
    Call<List<ShopJsonObj>> call = shopJsonPlaceHolder.locationJsonPlaceHolder();
    call.enqueue(new Callback<List<ShopJsonObj>>() {
      @Override
      public void onResponse(Call<List<ShopJsonObj>> call, Response<List<ShopJsonObj>> response) {
        if (!response.isSuccessful()) {
          return;
        }
        
        //storing locations in jsonObjectsList
        shopJsonObjsList = response.body();
        
        //adding markers of shops
        for (ShopJsonObj currShop : shopJsonObjsList) {
          mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(currShop.getLatitude()),
                                                                 Double.parseDouble(currShop.getLongitude())))
                                            .title(currShop.getType()));
        }
        
        //display shops in rvNearYouListId
        displayShops();
      }
      
      @Override
      public void onFailure(Call<List<ShopJsonObj>> call, Throwable t) {
      
      }
    });
  }
  
  private void displayShops() {
    ShopAdapter shopAdapter = new ShopAdapter(this, shopJsonObjsList);
    rvShopsListId.setLayoutManager(new LinearLayoutManager(this));
    rvShopsListId.setAdapter(shopAdapter);
  }
  
  /**
   * Manipulates the map once available.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    
    //moving camera to montreal
    LatLng montreal = new LatLng(45.4774675, -73.6080016);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(montreal, 13f));
  }
}