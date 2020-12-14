package com.example.androidfinalprojectcoffeeapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CircleImageView profile_pic;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private String currentUser;
    private EditText firstName, lastName, username, email;
    private Button saveImage, saveNames;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String fName, lName, Email, user, password, URL;
    private ImageView edit;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View header;
    private TextView headerName, deleteAccount;
    private CircleImageView headerProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //link views
        profile_pic = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.profile_first_name);
        lastName = findViewById(R.id.profile_last_name);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        edit = findViewById(R.id.profile_editnames);
        saveNames = findViewById(R.id.profile_saveNames);
        saveImage = findViewById(R.id.profile_saveImage);
        drawerLayout = findViewById(R.id.profile_drawerlayout);
        navigationView = findViewById(R.id.profile_nav);
        header = navigationView.getHeaderView(0);
        headerName = header.findViewById(R.id.nav_header_name);
        headerProfilePic = header.findViewById(R.id.nav_header_profile_pic);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        deleteAccount = findViewById(R.id.profile_deleteAccount);

        //get current user
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");
        changeHeaderInfo();

        // display current user info
        if (currentUser != null) {
            displayUserInfo();
        }

        //Side bar nav will have the focus when toggled
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        //Select photo from phone gallery
        profile_pic.setOnClickListener(view -> {
            openFileChooser();
            // startActivity(new Intent(ProfileActivity.this, temporary.class));
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("users").child(currentUser).removeValue();
                FirebaseDatabase.getInstance().getReference("favorites").child(currentUser).removeValue();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        //TODO: add comments here
        saveImage.setOnClickListener(view -> {
            saveImage.setVisibility(View.INVISIBLE);
            saveImage.setEnabled(false);
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) {
                }
                Uri url = uri.getResult();

                //update change this one
                User uploadUser = new User(fName, lName, user, Email, password, "" + url);
                String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(currentUser).setValue(uploadUser);
                URL = "" + url;
                //FirebaseDatabase.getInstance().getReference("users").child(currentUser).child("imageUrl");//.child(currentUser).child("imageUrl").setValue(url);
            }).addOnFailureListener(e -> {
            });
        });

        //TODO: add comments here
        saveNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User uploadUser = new User(firstName.getText().toString(), lastName.getText().toString(), user, Email, password, URL);
                mDatabaseRef.child(currentUser).setValue(uploadUser);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                saveNames.setEnabled(false);
                saveNames.setVisibility(View.INVISIBLE);
                edit.setEnabled(true);
                edit.setVisibility(View.VISIBLE);
            }
        });

        //TODO: add comments here
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setEnabled(true);
                lastName.setEnabled(true);
                saveNames.setEnabled(true);
                saveNames.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
                edit.setEnabled(false);
            }
        });
    }

    /**
     * Gets the current user's info from database and display info.
     */
    private void displayUserInfo() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fName = String.valueOf(snapshot.child("fName").getValue());
                lName = String.valueOf(snapshot.child("lName").getValue());
                Email = String.valueOf(snapshot.child("email").getValue());
                user = String.valueOf(snapshot.child("username").getValue());
                password = String.valueOf(snapshot.child("password").getValue());
                URL = String.valueOf(snapshot.child("imageUrl").getValue());

                firstName.setText(fName);
                lastName.setText(lName);
                username.setText(user);
                email.setText(Email);
                Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //TODO: add comments here
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //TODO: add comments here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            //if we actually get something back
            if (data != null && data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(profile_pic);
                saveImage.setVisibility(View.VISIBLE);
                saveImage.setEnabled(true);
                edit.setVisibility(View.INVISIBLE);
                edit.setEnabled(false);
            }
        }
    }

    //TODO: add comments here
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //open activities for the rest of nav items
        switch (item.getItemId()) {
            case R.id.nav_map:
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("username", currentUser);
                startActivity(mapIntent);
                break;

            case R.id.nav_favorites:
                Intent favoritesIntent = new Intent(this, FavoriteListActivity.class);
                favoritesIntent.putExtra("username", currentUser);
                startActivity(favoritesIntent);
                break;

            case R.id.nav_signOut:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Sets profile pic and name in the header of user who logged in.
     */
    public void changeHeaderInfo() {
        if (currentUser == null) {
            return;
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fName = String.valueOf(snapshot.child("fName").getValue());
                lName = String.valueOf(snapshot.child("lName").getValue());
                headerName.setText(fName + " " + lName);
                Picasso.get().load(String.valueOf(snapshot.child("imageUrl").getValue())).into(headerProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Side bar nav will close if swiped
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}