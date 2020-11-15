package com.example.androidfinalprojectcoffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    }

    public void validateLoginInfo() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        boolean isValid = true;
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT);
            isValid = false;
        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT);
            isValid = false;
        }
        if (isValid) {
            openMapAct();
        }
    }

    public void openMapAct() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void openSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}