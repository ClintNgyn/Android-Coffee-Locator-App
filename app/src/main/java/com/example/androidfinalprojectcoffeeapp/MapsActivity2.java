package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private TextView temp;
    private List<JSONobjects> jsonObjects;
    private RecyclerView recyclerView;
    private String currentUser;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");

        drawerLayout = findViewById(R.id.map2_drawerlayout);
        navigationView = findViewById(R.id.map2_nav);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/").addConverterFactory(GsonConverterFactory.create()).build();
        recyclerView = findViewById(R.id.maps2_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                ArrayList<ExampleItemMaps> list = new ArrayList<>();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(montreal, 13));
                for (int c = 0; c < jsonObjects.size(); c++) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(jsonObjects.get(c).getLat()), Double.parseDouble(jsonObjects.get(c).getLongitude()))).title(jsonObjects.get(c).getType()));
                    list.add(new ExampleItemMaps(jsonObjects.get(c).getImg(), jsonObjects.get(c).getAddress(), jsonObjects.get(c).getPhonenumber(), jsonObjects.get(c).getType(), jsonObjects.get(c).getLat(), jsonObjects.get(c).getLongitude()));
                }
                recyclerView.setAdapter(new AdapterOfMaps(list));
            }

            @Override
            public void onFailure(Call<List<JSONobjects>> call, Throwable t) {

            }
        });
    }

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(MapsActivity2.this, ProfileActivity.class);
                intent.putExtra("username", currentUser);
                startActivity(intent);
                return true;
        }
        return true;
    }
}