package com.example.androidfinalprojectcoffeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  public void openMapAct(View view) {
    startActivity(new Intent(this, MapsActivity.class));
  }

    public void openSignUpActivity(View view) {
    startActivity(new Intent(this, SignUpActivity.class));
    }

    public void openProfileActivity(View view) {
      startActivity(new Intent(this, ProfileActivity.class));
    }
}