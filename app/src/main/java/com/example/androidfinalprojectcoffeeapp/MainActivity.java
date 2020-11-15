package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    private ArrayList<String> users;
    private boolean isValid;
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

        signUpButton.setOnClickListener(v -> openSignUpActivity());

        loginButton.setOnClickListener(v -> validateLoginInfo());
        users = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    users.add(String.valueOf(d.child("username").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void validateLoginInfo() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        isValid = false;
        for(int c = 0; c < users.size(); c++){
            if(username.equals(users.get(c))){
                FirebaseDatabase.getInstance().getReference("users").child(users.get(c)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(String.valueOf(snapshot.child("password").getValue()).equals(SignUpActivity.SHA1(password))){
                            isValid = true;
                            openMapAct();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        if(!isValid){
            usernameInput.setError("Invalid Username or Password");
            usernameInput.requestFocus();
            passwordInput.setError("Invalid Username or Password");
        }
    }

    public void openMapAct() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void openSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}