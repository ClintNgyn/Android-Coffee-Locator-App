package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

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

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
  
  private GoogleMap mMap;
  private TextView temp;
  private List<JSONobjects> jsonObjects;
  
  private Button menuListBtn;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps2);
    
    //link views
    temp = findViewById(R.id.map2_temp);
    
    menuListBtn = findViewById(R.id.menuListBtn);
    menuListBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
      }
    });
    
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map2);
    mapFragment.getMapAsync(this);
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    JsonPlaceHolder jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
    Call<List<JSONobjects>> call = jsonPlaceHolder.getJSONobjects();
    call.enqueue(new Callback<List<JSONobjects>>() {
      @Override
      public void onResponse(Call<List<JSONobjects>> call, Response<List<JSONobjects>> response) {
        if (!response.isSuccessful()) {
          
          return;
        }
        jsonObjects = response.body();
        LatLng montreal = new LatLng(45.4774675, -73.6080016);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(montreal));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(montreal, 13));
        for (int c = 0; c < jsonObjects.size(); c++) {
          mMap.addMarker(new MarkerOptions().position(
              new LatLng(Double.parseDouble(jsonObjects.get(c).getLat()), Double.parseDouble(jsonObjects.get(c).getLongitude())))
                                            .title(jsonObjects.get(c).getType()));
        }
      }
      
      @Override
      public void onFailure(Call<List<JSONobjects>> call, Throwable t) {
      
      }
    });
  }
  
  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    
  }
}