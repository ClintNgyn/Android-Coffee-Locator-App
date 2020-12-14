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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private Button signUpButton, loginButton;
    private SignInButton googleSignInButton;
    private EditText usernameInput, passwordInput;
    private TextView forgetPasswordTextView;
    private ImageView googleLogo, facebookLogo, twitterLogo, githubLogo, showPasswordImage;

    private boolean toggleEyeVisibility = false;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link views
        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.google_sign_in_button);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);

        showPasswordImage = findViewById(R.id.showPasswordImage);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


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
        //getBackgroundImage();
    }

    //get a GoogleSignInAccount object for the user.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //Verifies that the sign was a success or failure
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean exists = false;
                    for(DataSnapshot d: snapshot.getChildren()){
                        if(d.getKey().equals(account.getEmail())){
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("username", account.getEmail());
                            startActivity(intent);
                            exists = true;
                        }
                        //Toast.makeText(MainActivity.this, "" + d.getKey(), Toast.LENGTH_SHORT).show();
                    }
                    if(exists){
                        User user = new User(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getEmail(), "", account.getPhotoUrl().toString());
                        FirebaseDatabase.getInstance().getReference("users").child(account.getEmail()).setValue(user);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            System.out.println(account.getEmail());

//            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Validate that the user exists in the database.
     */
    public void validateLoginInfo() {
        //openMapsActivity(null);
        //    TODO: uncomment after - for now just login (very annoying to have to sign in every time)
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            //check if username and password combination in the database
            FirebaseDatabase.getInstance().getReference("users").child(username).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (String.valueOf(snapshot.child("password").getValue()).equals(SignUpActivity.SHA1(password))) {
                  openMapsActivity(username);
                } else {
                  usernameInput.requestFocus();
                  usernameInput.setError("Invalid Username or Password");
                  passwordInput.setError("Invalid Username or Password");
                }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                return;
              }
            });
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
        finish();
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