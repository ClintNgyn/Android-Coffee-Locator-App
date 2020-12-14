package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends SideNavigationBar {
  
  private final List<ShopJsonObj> favShopList = new ArrayList<>();
  private GoogleSignInClient mGoogleSignInClient;
  private String currentUser;
  private String fName, lName;
  private RecyclerView recyclerView;
  private ImageView ivNavToolbar;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite_list);
  
    //connect recycler view
    recyclerView = findViewById(R.id.favoriteRecyclerView);
    ivNavToolbar = findViewById(R.id.ivNavToolbar);
    ivNavToolbar.setOnClickListener(view -> drawerLayoutId.openDrawer(GravityCompat.START));
  
    //get current user
    Intent intent = getIntent();
    currentUser = intent.getStringExtra("username");
    changeHeaderInfo();
  
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    
    // Build a GoogleSignInClient with the options specified by gso.
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    
    //from SideNavigationBar abstract class
    drawerLayoutId = findViewById(R.id.favorite_drawerlayout);
    navViewId = findViewById(R.id.favorite_list_nav);
    navHeader = navViewId.getHeaderView(0);
    tvNavHeaderName = navHeader.findViewById(R.id.nav_header_name);
    navHeaderPfp = navHeader.findViewById(R.id.nav_header_profile_pic);
    
    //init nav bar
    initNavBar();
    fetchNavHeaderInfo();
    
    //Get Google Account Information
    getGoogleAccountInfo();
    
    //fill up the list
    FirebaseDatabase.getInstance().getReference("favorites").child(currentUser).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String temp = "";
        for (DataSnapshot d : snapshot.getChildren()) {
          //temp += d.child("type").getValue() +  ":";
          favShopList.add(new ShopJsonObj(
              d.child("img").getValue().toString(),
              d.child("address").getValue().toString(),
              d.child("phoneNumber").getValue().toString(),
              d.child("type").getValue().toString(),
              d.child("latitude").getValue().toString(),
              d.child("longitude").getValue().toString(),
              true,
              Double.parseDouble(d.child("distance").getValue().toString())
          ));
        }
        //set RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteListActivity.this));
        recyclerView.setAdapter(new FavoriteListAdapter(FavoriteListActivity.this, favShopList, currentUser));
      }
      
      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      
      }
    });
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
        tvNavHeaderName.setText(fName + " " + lName);
        Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(navHeaderPfp);
      }
      
      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      
      }
    });
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
    mGoogleSignInClient.signOut()
                       .addOnCompleteListener(this, task -> Toast.makeText(FavoriteListActivity.this, "Sign Out Successful",
                                                                           Toast.LENGTH_SHORT));
  }
  
  @Override
  public void initNavBar() {
    navViewId.bringToFront();
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutId, R.string.open, R.string.close);
    drawerLayoutId.addDrawerListener(toggle);
    toggle.syncState();
    navViewId.setNavigationItemSelectedListener(this);
    navViewId.setCheckedItem(R.id.nav_favorites);
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
        finish();
        break;
      
      case R.id.nav_map:
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("username", currentUser);
        startActivity(mapIntent);
        finish();
        break;
  
      case R.id.nav_signOut:
        startActivity(new Intent(this, MainActivity.class));
        signOut();
        finish();
        break;
  
      case R.id.nav_about:
        String m = "This app allows different users to view coffee stores near by, users can add their favorite coffee store so" +
                   " that he\\she can get their favorite cup of coffee as soon as possible";
    
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
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
}