package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button signUpButton;
    Button loginButton;

    EditText usernameInput;
    EditText passwordInput;

    TextView forgetPasswordTextView;

    ImageView googleLogo;
    ImageView facebookLogo;
    ImageView twitterLogo;
    ImageView githubLogo;

    ImageView showPasswordImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        signUpButton.setOnClickListener(v -> openSignUpActivity());

        loginButton.setOnClickListener(v -> validateLoginInfo());

        showPasswordImage.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });
    }

    public void validateLoginInfo() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        //check if username and password combination in the database
        FirebaseDatabase.getInstance().getReference("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (String.valueOf(snapshot.child("password").getValue()).equals(SignUpActivity.SHA1(password))) {

                    Intent intent = new Intent(MainActivity.this, MapsActivity2.class);

                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    usernameInput.setError("Invalid Username or Password");
                    usernameInput.requestFocus();
                    passwordInput.setError("Invalid Username or Password");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }

    public void openSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}