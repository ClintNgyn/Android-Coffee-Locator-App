package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  Button signInButton;
  Button loginButton;

  EditText usernameInput;
  EditText passwordInput;

  TextView forgetPasswordTextView;

  ImageView googleLogo;
  ImageView facebookLogo;
  ImageView twitterLogo;
  ImageView githubLogo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    signInButton = findViewById(R.id.signInButton);
    loginButton = findViewById(R.id.loginButton);

    usernameInput = findViewById(R.id.usernameInput);
    passwordInput = findViewById(R.id.passwordInput);

    forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);

    googleLogo = findViewById(R.id.googleLogo);
    facebookLogo = findViewById(R.id.facebookLogo);
    twitterLogo = findViewById(R.id.twitterLogo);
    githubLogo = findViewById(R.id.githubLogo);
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