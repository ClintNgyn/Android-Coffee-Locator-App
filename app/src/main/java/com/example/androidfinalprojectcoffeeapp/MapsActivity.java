package com.example.androidfinalprojectcoffeeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends SideNavigationBar
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String currentUser;

    private List<ShopJsonObj> shopJsonObjsList = new ArrayList<>();
    private RecyclerView rvShopsListId;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        //link views
        rvShopsListId = findViewById(R.id.rvShopsListId);

        //from SideNavigationBar abstract class
        drawerLayoutId = findViewById(R.id.maps_drawerlayout);
        navViewId = findViewById(R.id.maps_nav);
        navHeader = navViewId.getHeaderView(0);
        tvNavHeaderName = navHeader.findViewById(R.id.nav_header_name);
        navHeaderPfp = navHeader.findViewById(R.id.nav_header_profile_pic);

        //init nav bar
        initNavBar();
        fetchNavHeaderInfo();

        //Get Google Account Information
        getGoogleAccountInfo();
    }

    public void getGoogleAccountInfo() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            tvNavHeaderName.setText(personGivenName + " " + personFamilyName);
            Picasso.get().load(String.valueOf(personPhoto)).into(navHeaderPfp);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> Toast.makeText(MapsActivity.this, "Sign Out Successful", Toast.LENGTH_SHORT));
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
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("username", currentUser);
                startActivity(intent);
                break;

            case R.id.nav_favorites:
                startActivity(new Intent(this, FavoriteListActivity.class));
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
        mMap = googleMap;

        //TODO: get device's location
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

                //display shops in rvShopsListId
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
        int id = 0;

        switch (type.toUpperCase()) {
            case "STARBUCKS":
                id = R.drawable.ic_starbucks_marker;
                break;
            case "TIM HORTONS":
                id = R.drawable.ic_tim_hortons_marker;
                break;
            case "DUNKIN DONUTS":
                id = R.drawable.ic_dunkin_donuts_marker;
                break;
            case "SECOND CUP":
                // TODO: find second cup svg marker
                id = R.drawable.ic_dunkin_donuts_marker;
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