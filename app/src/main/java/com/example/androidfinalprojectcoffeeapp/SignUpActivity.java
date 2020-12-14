package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private EditText fName, lName, email, user, pass;
    private Button register;

    private DatabaseReference mDatabaseRef;

    private InputChecker checker;

    private HashSet<String> emails, users;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //link views
        fName = findViewById(R.id.signUp_first_name);
        lName = findViewById(R.id.signUp_last_name);
        email = findViewById(R.id.signUp_email);
        user = findViewById(R.id.signUp_username);
        pass = findViewById(R.id.signUp_password);
        register = findViewById(R.id.signUp_register_btn);

        //init global vars
        checker = new InputChecker();
        emails = new HashSet<>();
        users = new HashSet<>();

        //get list of users and emails from firebase
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    emails.add(String.valueOf(d.child("email").getValue()));
                    users.add(String.valueOf(d.child("username").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //register onclick handler
        register.setOnClickListener(view -> {
            //for now toast message,****************
            //to do putlabel if invalid inputs****************
            //red border color****************
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

            if (!checker.checkLettersOnly(fName.getText().toString()) || !checker.checkLettersOnly(lName.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Please Enter valid Names, only letters", Toast.LENGTH_SHORT).show();
                fName.setError("Invalid First Name");
                fName.requestFocus();
                lName.setError("Invalid Last Name");
                return;
            }
            //TODO: NOT FINISHED have to check if email is really legit

            //check valid username
            if (!checker.checkLettersNumbersOnly(user.getText().toString())) {
                user.setError("Invalid Username");
                user.requestFocus();
                return;
            }

            //check valid email
            if (!checker.checkEmail(email.getText().toString())) {
                email.setError("Invalid Email");
                email.requestFocus();
                return;
            }

            //check valid password
            if (!checker.checkPassword(pass.getText().toString())) {
                pass.setError("Password must have\n-One capital\n-One number\n-No special characters such as !,%,?");
                pass.requestFocus();
                return;
            }

            //check unique username
            if (users.contains(user.getText().toString())) {
                user.setError("This username is already taken by a user");
                user.requestFocus();
                return;
            }

            //check unique email
            if (emails.contains(email.getText().toString())) {
                email.setError("This email is already taken by a user");
                email.requestFocus();
                return;
            }

            //new user success
            //I made a class just so the codes look neat. and to make sure that no meta data that will be forgotten
            //temporary profile pic url https://vigyanprasar.gov.in/wp-content/uploads/2016/02/dummy-avatar.png

            User uploadUser = new User(fName.getText().toString(), lName.getText().toString(), user.getText().toString(),
                    email.getText().toString(), SHA1(pass.getText().toString()),
                    "https://vistapointe.net/images/stick-man-1.jpg");
            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(user.getText().toString()).setValue(uploadUser);
            fName.setText("");
            lName.setText("");
            user.setText("");
            email.setText("");
            pass.setText("");
            Toast.makeText(SignUpActivity.this, "Success in Registering", Toast.LENGTH_SHORT).show();

            //redirect to login page but for now in the main activity TO PREVENT FROM USING THE SAME USERNAME AND PASSWORD****
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));

            //redirect to login page*********
        });
    }

    //TODO: add comments here
    private static String byteArrayToString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte aByte : bytes) {
            buffer.append(String.format(Locale.getDefault(), "%02x", aByte));
        }
        return buffer.toString();
    }

    //TODO: add comments here
    public static String SHA1(String temp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                md.update(temp.getBytes(StandardCharsets.UTF_8));
            }
            return byteArrayToString(md.digest());
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}