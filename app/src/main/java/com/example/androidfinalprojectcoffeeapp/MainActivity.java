package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
  
  private Button signUpButton, loginButton;
  private EditText usernameInput, passwordInput;
  private TextView forgetPasswordTextView;
  private ImageView googleLogo, facebookLogo, twitterLogo, githubLogo, showPasswordImage;
  
  private boolean toggleEyeVisibility = false;
  
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
    showPasswordImage = findViewById(R.id.showPasswordImage);
    
    // signUpButton onclick handler
    signUpButton.setOnClickListener(v -> openSignUpActivity());
    
    // loginButton onclick handler
    loginButton.setOnClickListener(v -> validateLoginInfo());
    
    // eyeImage onclick handler
    showPasswordImage.setOnClickListener(view -> {
      // toggle password input visibility
      passwordInput.setInputType(!toggleEyeVisibility ?
                                 InputType.TYPE_CLASS_TEXT :
                                 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      
      // toggle eye image
      showPasswordImage.setImageResource(!toggleEyeVisibility ?
                                         R.drawable.ic_eye_pass_invisible :
                                         R.drawable.ic_eye_pass_visible);
      
      // toggle toggleEyeVisibility
      toggleEyeVisibility = !toggleEyeVisibility;
    });
    
    //    showPasswordImage.setOnTouchListener((v, event) -> {
    //      switch (event.getAction()) {
    //        case MotionEvent.ACTION_DOWN:
    //          passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
    //          break;
    //        case MotionEvent.ACTION_UP:
    //          passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    //          break;
    //      }
    //      return true;
    //    });
  }
  
  /**
   * Validate that the user exists in the database.
   */
  public void validateLoginInfo() {
    openMapsActivity(null);
    //    TODO: uncomment after - for now just login (very annoying to have to sign in every time)
    //    String username = usernameInput.getText().toString();
    //    String password = passwordInput.getText().toString();
    //    //check if username and password combination in the database
    //    FirebaseDatabase.getInstance().getReference("users").child(username).addValueEventListener(new ValueEventListener() {
    //      @Override
    //      public void onDataChange(@NonNull DataSnapshot snapshot) {
    //        if (String.valueOf(snapshot.child("password").getValue()).equals(SignUpActivity.SHA1(password))) {
    //          openMapsActivity(username);
    //        } else {
    //          usernameInput.requestFocus();
    //          usernameInput.setError("Invalid Username or Password");
    //          passwordInput.setError("Invalid Username or Password");
    //        }
    //      }
    //
    //      @Override
    //      public void onCancelled(@NonNull DatabaseError error) {
    //        return;
    //      }
    //    });
  }
  
  /**
   * Start {@link SignUpActivity}
   */
  public void openSignUpActivity() {
    startActivity(new Intent(this, SignUpActivity.class));
  }
  
  /**
   * Start {@link MapsActivity}
   *
   * @param username username of the current user.
   */
  private void openMapsActivity(String username) {
    Intent intent = new Intent(this, MapsActivity.class);
    intent.putExtra("username", username);
    startActivity(intent);
  }
}