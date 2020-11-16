package com.example.androidfinalprojectcoffeeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profile_pic;
    private static final int PICK_IMAGE_REQUEST= 1;
    private Uri mImageUri;
    private String currentUser;
    private EditText firstName, lastName, username, email;
    private Button saveImage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String fName, lName, Email, user, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_pic = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.profile_first_name);
        lastName = findViewById(R.id.profile_last_name);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
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
                user =  String.valueOf(snapshot.child("username").getValue());
                password = String.valueOf(snapshot.child("password").getValue());

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

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
               // startActivity(new Intent(ProfileActivity.this, temporary.class));
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage.setVisibility(View.INVISIBLE);
                saveImage.setEnabled(false);
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        //update change this one
                        User uploadUser = new User(fName, lName, user, Email, password, ""+url);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(currentUser).setValue(uploadUser);
                        //FirebaseDatabase.getInstance().getReference("users").child(currentUser).child("imageUrl");//.child(currentUser).child("imageUrl").setValue(url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });
    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST){
            //if we actually get something back
            if(data !=null && data.getData() != null){
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(profile_pic);
                saveImage.setVisibility(View.VISIBLE);
                saveImage.setEnabled(true);
            }
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}