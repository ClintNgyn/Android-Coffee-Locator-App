package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }
  
  /**
   * Manipulates the map once available.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
  
    //moving camera to montreal
    LatLng montreal = new LatLng(45.4774675, -73.6080016);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(montreal, 13.5f));
  
    //get shops from json
    getShops();
  }
  
  /**
   * Fetch shops from json file
   */
  private void getShops() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    ShopJsonPlaceHolder shopJsonPlaceHolder = retrofit.create(ShopJsonPlaceHolder.class);
    Call<List<ShopJsonObj>> call = shopJsonPlaceHolder.getShopJsonPlaceHolder();
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
          mMap.addMarker(new MarkerOptions()
                             .position(new LatLng(Double.parseDouble(currShop.getLatitude()),
                                                  Double.parseDouble(currShop.getLongitude())))
                             .title(currShop.getType())
                             .icon(getBitmapDescriptorFromVector(getApplicationContext(), getVectorId(currShop.getType()))));
        }
        
        //display shops in rvNearYouListId
        displayShops();
      }
      
      @Override
      public void onFailure(Call<List<ShopJsonObj>> call, Throwable t) {
      
      }
    });
  }
  
  /**
   * Display shops in recycler view
   */
  private void displayShops() {
    ShopAdapter shopAdapter = new ShopAdapter(this, shopJsonObjsList);
    rvShopsListId.setLayoutManager(new LinearLayoutManager(this));
    rvShopsListId.setAdapter(shopAdapter);
  }
  
  /**
   * Returns a different vector based on what shop it is.
   *
   * @param type type of shop as string.
   * @return a different vector based on what shop it is.
   */
  private int getVectorId(String type) {
    return type.toUpperCase().equals("STARBUCKS") ?
           R.drawable.ic_starbucks_marker :
           type.toUpperCase().equals("TIM HORTONS") ? R.drawable.ic_tim_hortons_marker : R.drawable.ic_dunkin_donuts_marker;
  }
  
  /**
   * Convert vector to bitmap.
   *
   * @param context     application context
   * @param vectorResId vector resource id
   * @return A bitmapDescriptor of the vector.
   */
  private BitmapDescriptor getBitmapDescriptorFromVector(Context context, int vectorResId) {
    Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
    vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
    
    Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                                        vectorDrawable.getIntrinsicHeight(),
                                        Bitmap.Config.ARGB_8888);
    
    Canvas canvas = new Canvas(bitmap);
    vectorDrawable.draw(canvas);
    
    return BitmapDescriptorFactory.fromBitmap(bitmap);
  }
}