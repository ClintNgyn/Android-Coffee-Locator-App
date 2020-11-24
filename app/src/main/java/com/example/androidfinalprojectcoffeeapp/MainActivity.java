package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
  
  private Button signUpButton, loginButton;
  private EditText usernameInput, passwordInput;
  private TextView forgetPasswordTextView;
  private ImageView googleLogo, facebookLogo, twitterLogo, githubLogo, showPasswordImage;
  private BlurImageView bivBackgroundId;
  
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
    
    showPasswordImage = findViewById(R.id.showPasswordImage);
    
    bivBackgroundId = findViewById(R.id.bivBackgroundId);
    
    
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
    
    //TODO: move to profile act
    //get and set background image
//    getBackgroundImage();
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
  
  /**
   * Gets background image from api.
   */
  private void getBackgroundImage() {
    
    //get image from api
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://coffee.alexflipnote.dev/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    
    BackgroundImageJsonPlaceHolder backgroundImageJsonPlaceHolder = retrofit.create(BackgroundImageJsonPlaceHolder.class);
    
    Call<BackgroundImageJsonObj> call = backgroundImageJsonPlaceHolder.getBackgroundImageJsonPlaceHolder();
    call.enqueue(new Callback<BackgroundImageJsonObj>() {
      @Override
      public void onResponse(Call<BackgroundImageJsonObj> call, Response<BackgroundImageJsonObj> response) {
        setBackgroundImage(response.body().getFileImageUrl());
        Log.d("retrofit", "" + response.body().getFileImageUrl());
      }
      
      @Override
      public void onFailure(Call<BackgroundImageJsonObj> call, Throwable t) {
      
      }
    });
  }
  
  /**
   * Sets background image from api.
   */
  private void setBackgroundImage(String url) {
    //set background image
//    Picasso.get().load(url).into(new Target() {
    Picasso.get().load("https://coffee.alexflipnote.dev/27OJ6laasxs_coffee.jpg").into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        bivBackgroundId.setImageBitmap(bitmap);
        bivBackgroundId.setBlur(2);
      }

      @Override
      public void onBitmapFailed(Exception e, Drawable errorDrawable) {

      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    });
  }
}