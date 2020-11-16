package com.example.androidfinalprojectcoffeeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends FragmentActivity {
  
  private static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
  private static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
  private static final int LOCATION_PERMISSION_REQ_CODE = 1234;
  private String currentUser;
  private boolean isLocationPermissionsGranted = false;
  
  private GoogleMap mGoogleMap;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    Intent intent = getIntent();
    currentUser = intent.getStringExtra("username");
    getLocationPermission();
  }
  
  /**
   *
   */
  private void getLocationPermission() {
    String[] mPermissions = new String[]{FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION};
    
    //get location permission
    if ((ContextCompat.checkSelfPermission(this, FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED)
        && (ContextCompat.checkSelfPermission(this, COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED)) {
      isLocationPermissionsGranted = true;
    } else {
      ActivityCompat.requestPermissions(this, mPermissions, LOCATION_PERMISSION_REQ_CODE);
    }
  }
  
  /**
   * @param requestCode
   * @param permissions
   * @param grantResults
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
    isLocationPermissionsGranted = false;
    
    switch (requestCode) {
      case LOCATION_PERMISSION_REQ_CODE: {
        if (grantResults.length > 0) {
          for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
              isLocationPermissionsGranted = false;
              break;
            }
          }
          isLocationPermissionsGranted = true;
          initMap();
        }
      }
      
      
    }
  }
  
  private void initMap() {
    SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    
    mapFrag.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
      }
    });
  }

    public void goToProfile(View view) {
      Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
      intent.putExtra("username", currentUser);
      startActivity(intent);
    }
}