package com.example.androidfinalprojectcoffeeapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_pic = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.profile_first_name);
        lastName = findViewById(R.id.profile_last_name);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        edit = findViewById(R.id.profile_editnames);
        saveNames = findViewById(R.id.profile_saveNames);
        saveImage = findViewById(R.id.profile_saveImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        //get current user
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");
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

        profile_pic.setOnClickListener(view -> {
            openFileChooser();
            // startActivity(new Intent(ProfileActivity.this, temporary.class));
        });

        saveImage.setOnClickListener(view -> {
            saveImage.setVisibility(View.INVISIBLE);
            saveImage.setEnabled(false);
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

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

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}