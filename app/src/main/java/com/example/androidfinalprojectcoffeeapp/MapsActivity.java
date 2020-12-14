package com.example.androidfinalprojectcoffeeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends SideNavigationBar
    implements OnMapReadyCallback {
  
  private final double EARTH_RADIUS_KM = 6372.8;
  private final LatLng CURR_LOCATION = new LatLng(45.5056156, -73.6159479);
  private GoogleSignInClient mGoogleSignInClient;
  private GoogleMap mMap;
  
  private String currentUser;
  private boolean isGoogleSignIn;
  
  private List<ShopJsonObj> shopJsonObjsList = new ArrayList<>();
  
  private RecyclerView rvShopsListId;
  private RelativeLayout mapRL;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    
    //link views
    rvShopsListId = findViewById(R.id.rvShopsListId);
    mapRL = findViewById(R.id.mapRL);
    
    //from SideNavigationBar abstract class
    drawerLayoutId = findViewById(R.id.maps_drawerlayout);
    navViewId = findViewById(R.id.maps_nav);
    navHeader = navViewId.getHeaderView(0);
    tvNavHeaderName = navHeader.findViewById(R.id.nav_header_name);
    navHeaderPfp = navHeader.findViewById(R.id.nav_header_profile_pic);
    
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    
    // Build a GoogleSignInClient with the options specified by gso.
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    
    //get intent data from MainActivity
    currentUser = getIntent().getStringExtra("username");
    isGoogleSignIn = getIntent().getBooleanExtra("isGoogleSignIn", false);
    
    if (isGoogleSignIn) {
      getGoogleAccountInfo();
    } else {
      //init nav bar
      initNavBar();
      fetchNavHeaderInfo();
    }
    
  }
  
  private void zoomToCurrLocation() {
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CURR_LOCATION, 13.4f));
  }
  
  public void getGoogleAccountInfo() {
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        boolean exists = false;
        
        for (DataSnapshot d : snapshot.getChildren()) {
          if (d.getKey().equals(account.getId())) {
            currentUser = account.getId();
            exists = true;
          }
          //Toast.makeText(MainActivity.this, "" + d.getKey(), Toast.LENGTH_SHORT).show();
        }
        if (!exists) {
          User user = new User(account.getGivenName(),
                               account.getFamilyName(),
                               account.getGivenName() + account.getFamilyName() + "123",
                               account.getEmail(), "",
                               account.getPhotoUrl().toString());
          FirebaseDatabase.getInstance().getReference("users").child(account.getId()).setValue(user);
          currentUser = account.getId();
        }
        
        initNavBar();
        fetchNavHeaderInfo();
      }
      
      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      
      }
    });
  }
  
  
  private void signOut() {
    mGoogleSignInClient.signOut()
                       .addOnCompleteListener(this, task -> Toast.makeText(MapsActivity.this, "Sign Out Successful",
                                                                           Toast.LENGTH_SHORT));
  }
  
  @Override
  public void initNavBar() {
    navViewId.bringToFront();
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutId, R.string.open, R.string.close);
    drawerLayoutId.addDrawerListener(toggle);
    toggle.syncState();
    navViewId.setNavigationItemSelectedListener(this);
    navViewId.setCheckedItem(R.id.nav_map);
  }
  
  /**
   * Fetches user's data and display in nav's header.
   */
  @Override
  public void fetchNavHeaderInfo() {
    if (currentUser == null) {
      return;
    }
    
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
    mDatabaseRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
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
      case R.id.nav_profile:
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("username", currentUser);
        startActivity(profileIntent);
        break;
      
      case R.id.nav_favorites:
        Intent favoriteIntent = new Intent(this, FavoriteListActivity.class);
        favoriteIntent.putExtra("username", currentUser);
        startActivity(favoriteIntent);
        break;
      
      case R.id.nav_signOut:
        startActivity(new Intent(this, MainActivity.class));
        signOut();
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
   * Manipulates the map once available.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    //set location btn
    //TODO: fix this - button behind map fragment
    CircleImageView civ = new CircleImageView(this);
    civ.setOnClickListener(view -> zoomToCurrLocation());
  
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    params.setMargins(20, 20, 20, 20);
  
    civ.setLayoutParams(params);
    mapRL.addView(civ);
  
    mMap = googleMap;
  
    //set and get location
    setUpLocation();
  
    //moving camera to current location (cote vertu)
    zoomToCurrLocation();
  
    //pin location
    mMap.addMarker(new MarkerOptions().position(CURR_LOCATION)
                                      .icon(getBitmapDescriptorFromVector(this, R.drawable.ic_my_location_marker_icon)));
  
    //get shops from json
    getShops();
  }
  
  private void setUpLocation() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
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
  
        //adding markers of shops to map
        //calculating distance from current location
        for (ShopJsonObj currShop : shopJsonObjsList) {
          String type = currShop.getType();
          double currLat = Double.parseDouble(currShop.getLatitude());
          double currLng = Double.parseDouble(currShop.getLongitude());
    
          //marker
          mMap.addMarker(new MarkerOptions().position(new LatLng(currLat, currLng))
                                            .title(type)
                                            .icon(getBitmapDescriptorFromVector(getApplicationContext(), getVectorId(type))));
    
          //find distance from current location
          currShop.setDistance(Math.ceil(
              haverineFormula(CURR_LOCATION.latitude, CURR_LOCATION.longitude, currLat, currLng))
                               * 100 / 100.0);
        }
  
        //sort shopJsonObjsList by closest to farthest
        Collections.sort(shopJsonObjsList, (a, b) -> {
          if (a.getDistance() < b.getDistance()) {
            return -1;
          } else if (a.getDistance() > b.getDistance()) {
            return 1;
          } else {
            return 0;
          }
        });
  
        //display shops in rvShopsListId
        displayShops();
      }
  
      @Override
      public void onFailure(Call<List<ShopJsonObj>> call, Throwable t) {
    
      }
    });
  }
  
  /**
   * Haversine formula.
   * Giving great-circle distances between two points on a sphere from their longitudes and latitudes.
   * It is a special case of a more general formula in spherical trigonometry, the law of haversines, relating the
   * sides and angles of spherical "triangles".
   *
   * @param sLat start latitude
   * @param sLng start longitude
   * @param eLat end latitude
   * @param eLng end longitude
   * @return distance in kilometers
   */
  private double haverineFormula(double sLat, double sLng, double eLat, double eLng) {
    //convert to radians
    double diffLat = Math.toRadians(eLat - sLat);
    double diffLng = Math.toRadians(eLng - sLng);
    double originLat = Math.toRadians(sLat);
    double destinationLat = Math.toRadians(eLat);
    
    return EARTH_RADIUS_KM * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(diffLat / 2), 2) +
                                                     Math.pow(Math.sin(diffLng / 2), 2) * Math.cos(originLat) *
                                                     Math.cos(destinationLat)));
  }
  
  /**
   * Display shops in recycler view
   */
  private void displayShops() {
    ShopAdapter shopAdapter = new ShopAdapter(this, shopJsonObjsList, currentUser);
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
    int id = R.drawable.ic_star_icon;
    
    switch (type.toUpperCase()) {
      case "STARBUCKS":
        id = R.drawable.ic_starbucks_marker;
        break;
      case "TIM HORTONS":
        id = R.drawable.ic_tim_hortons_marker;
        break;
      case "DUNKINDONUTS":
        id = R.drawable.ic_dunkin_donuts_marker;
        break;
      case "SECONDCUP":
        id = R.drawable.ic_second_cup_marker;
        break;
    }
  
    return id;
  }
  
  /**
   * Converts a vector into a BitmapDescriptor for google maps to use.
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