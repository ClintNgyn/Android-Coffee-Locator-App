package com.example.androidfinalprojectcoffeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {

    private EditText fName, lName, email, user, pass;
    private DatabaseReference mDatabaseRef;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fName = findViewById(R.id.signUp_first_name);
        lName = findViewById(R.id.signUp_last_name);
        email = findViewById(R.id.signUp_email);
        user = findViewById(R.id.signUp_username);
        pass = findViewById(R.id.signUp_password);
        register = findViewById(R.id.signUp_register_btn);

    }
}