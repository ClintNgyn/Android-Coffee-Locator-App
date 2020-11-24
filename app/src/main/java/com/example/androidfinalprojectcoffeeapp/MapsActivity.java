package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;

    private List<ShopJsonObj> shopJsonObjsList;
    private RecyclerView rvShopsListId;

    private String currentUser;
    private String fName, lName;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View header;
    private TextView headerName;
    private CircleImageView headerProfilePic;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //link views
        rvShopsListId = findViewById(R.id.rvShopsListId);
        drawerLayout = findViewById(R.id.maps_drawerlayout);
        navigationView = findViewById(R.id.maps_nav);
        header = navigationView.getHeaderView(0);
        headerName = header.findViewById(R.id.nav_header_name);
        headerProfilePic = header.findViewById(R.id.nav_header_profile_pic);

        //get intent data from MainActivity
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");
        changeHeaderInfo();

        //Side bar nav will have the focus when toggled
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Allows user to switch activities from the side bar nav
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //open activities for the rest of nav items
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                intent.putExtra("username", currentUser);
                startActivity(intent);
                break;
        }
        return true;
    }

    /**
     * Sets profile pic and name in the header of user who logged in.
     */
    public void changeHeaderInfo() {
        if (currentUser == null) {
            return;
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fName = String.valueOf(snapshot.child("fName").getValue());
                lName = String.valueOf(snapshot.child("lName").getValue());
                headerName.setText(fName + " " + lName);
                Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(headerProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Side bar nav will close if swiped
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //moving camera to montreal
        LatLng montreal = new LatLng(45.4774675, -73.6080016);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(montreal, 13.4f));

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