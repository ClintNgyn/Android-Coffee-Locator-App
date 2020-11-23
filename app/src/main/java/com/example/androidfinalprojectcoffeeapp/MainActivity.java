package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
  
  private Button signUpButton;
  private Button loginButton;
  
  private EditText usernameInput;
  private EditText passwordInput;
  
  private TextView forgetPasswordTextView;
  
  private ImageView googleLogo;
  private ImageView facebookLogo;
  private ImageView twitterLogo;
  private ImageView githubLogo;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // link views
    signUpButton = findViewById(R.id.signUpButton);
    loginButton = findViewById(R.id.loginButton);
    usernameInput = findViewById(R.id.usernameInput);
    passwordInput = findViewById(R.id.passwordInput);
    forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);
    googleLogo = findViewById(R.id.googleLogo);
    facebookLogo = findViewById(R.id.facebookLogo);
    twitterLogo = findViewById(R.id.twitterLogo);
    githubLogo = findViewById(R.id.githubLogo);
    
    // signUpButton onclick handler
    signUpButton.setOnClickListener(v -> openSignUpActivity());
    
    // loginButton onclick handler
    loginButton.setOnClickListener(v -> validateLoginInfo());
    
  }
  
  /**
   * Validate that the user exists in the database.
   */
  public void validateLoginInfo() {
    String username = usernameInput.getText().toString();
    String password = passwordInput.getText().toString();
    boolean isValid = true;
    if (username.isEmpty()) {
      Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
      isValid = false;
    } else if (password.isEmpty()) {
      Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
      isValid = false;
    }
    if (isValid) {
      openMapAct();
    }
  }
  
  /**
   * Start {@link MapsActivity}
   */
  public void openMapAct() {
    startActivity(new Intent(this, MapsActivity.class));
  }
  
  /**
   * Start {@link SignUpActivity}
   */
  public void openSignUpActivity() {
    startActivity(new Intent(this, SignUpActivity.class));
  }
}