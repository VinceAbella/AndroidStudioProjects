package com.vins.task2forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        usernameEditText = findViewById(R.id.loginUsernameEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String inputUsername = usernameEditText.getText().toString().trim();
            String inputPassword = passwordEditText.getText().toString().trim();

            if (checkCredentials(inputUsername, inputPassword)) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent displayInfoIntent = new Intent(LoginActivity.this, DisplayInfo.class);
                displayInfoIntent.putExtra("username", inputUsername);
                startActivity(displayInfoIntent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });


        signUpButton.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(signUpIntent);
        });
    }

    private boolean checkCredentials(String username, String password) {
        return dbHelper.checkUserCredentials(username, password);
    }
}
