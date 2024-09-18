package com.vins.task2forms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayInfo extends AppCompatActivity {

    private TextView userInfoTextView;
    private DatabaseHelper dbHelper;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_info);
        setTitle("It's You");

        userInfoTextView = findViewById(R.id.userInfoTextView);
        dbHelper = new DatabaseHelper(this);
        backButton = findViewById(R.id.backButton);

        String username = getIntent().getStringExtra("username");

        displayUserInfo(username);

        backButton.setOnClickListener(v ->{
            Intent loginIntent = new Intent(DisplayInfo.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });


    }

    private void displayUserInfo(String username) {
        Cursor cursor = dbHelper.getUserInfo(username);
        if (cursor != null && cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex("email");
            int ageIndex = cursor.getColumnIndex("age");
            int birthdateIndex = cursor.getColumnIndex("birthDate");

            if (emailIndex == -1 || ageIndex == -1 || birthdateIndex == -1) {
                Log.e("DisplayInfo", "Column index not found");
                return;
            }

            String email = cursor.getString(emailIndex);
            String age = cursor.getString(ageIndex);
            String birthdate = cursor.getString(birthdateIndex);

            String userInfo = "Username: " + username + "\n" +
                    "Email: " + email + "\n" +
                    "Age: " + age + "\n" +
                    "Birthdate: " + birthdate;

            userInfoTextView.setText(userInfo);

            cursor.close();
        } else {
            Log.e("DisplayInfo", "No user found with username: " + username);
        }
    }

}