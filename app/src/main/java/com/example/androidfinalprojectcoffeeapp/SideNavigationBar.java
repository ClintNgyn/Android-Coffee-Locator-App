package com.example.androidfinalprojectcoffeeapp;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class SideNavigationBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  
  public DatabaseReference mDatabaseRef;
  public DrawerLayout drawerLayoutId;
  public NavigationView navViewId;
  public View navHeader;
  public TextView tvNavHeaderName;
  public CircleImageView navHeaderPfp;
  
  public abstract void initNavBar();
  
  public abstract void fetchNavHeaderInfo();
  
  @Override
  public abstract boolean onNavigationItemSelected(@NonNull MenuItem item);
  
}
