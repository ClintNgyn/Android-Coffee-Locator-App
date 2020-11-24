package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity implements communicator {

    private Button aboutAppButton;
    private String currentUser;
    private DatabaseReference mDatabaseRef;
    private TextView welcomeTextView;
    private String fName;
    private CircleImageView homepage_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        aboutAppButton = findViewById(R.id.aboutAppButton);
        homepage_image = findViewById(R.id.homepage_image);
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fName = String.valueOf(snapshot.child("fName").getValue());
                Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(homepage_image);
                welcomeTextView.setText("Welcome " + fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        aboutAppButton.setOnClickListener(v -> goToAboutAppActivity());
    }

    public void goToAboutAppActivity() {
        startActivity(new Intent(this, AboutAppActivity.class));
    }

    @Override
    public void goToMaps() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", currentUser);
        startActivity(intent);
    }
}