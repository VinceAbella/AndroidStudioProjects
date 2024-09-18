package com.vins.task2forms;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, ageEditText, bdayEditText;
    private Button sellButton, backButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        Spinner gender_Spinner = findViewById(R.id.gender_spinner);
        Spinner country_Spinner = findViewById(R.id.country_spinner);
        SeekBar eraSeekBar = findViewById(R.id.eraSeekbar);
        TextView eraTextView = findViewById(R.id.eraTextView);
        ageEditText = findViewById(R.id.ageEditText);
        sellButton = findViewById(R.id.sellButton);
        backButton = findViewById(R.id.backButton);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        bdayEditText = findViewById(R.id.bdayEditText);

        sellButton.setEnabled(false);

        String[] historyEras = getResources().getStringArray(R.array.history_eras);
        eraTextView.setText(historyEras[0]);
        ageEditText.setHint("How was the meteor??My Fellow Dinosaur");
        ageEditText.setEnabled(false);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gender_Spinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, R.layout.spinner_dropdown_item);
        countryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        country_Spinner.setAdapter(countryAdapter);

        gender_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    Toast.makeText(MainActivity.this, "Please select a valid gender", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        country_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    Toast.makeText(MainActivity.this, "Please select a valid country", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        eraSeekBar.setMax(historyEras.length - 1);
        eraSeekBar.setProgress(0);

        eraSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                eraTextView.setText(historyEras[progress]);
                switch (progress) {
                    case 0:
                        ageEditText.setHint("How was the meteor??My Fellow Dinosaur");
                        ageEditText.setEnabled(false);
                        break;
                    case 1:
                        ageEditText.setHint("Did you Join The Battle of Thermopylae");
                        ageEditText.setEnabled(false);
                        break;
                    case 2:
                        ageEditText.setHint("Salutations to you \"Survivor of the Black Plague\"");
                        ageEditText.setEnabled(false);
                        break;
                    case 3:
                        ageEditText.setHint("Have you met Isaac Newton");
                        ageEditText.setEnabled(false);
                        break;
                    case 4:
                        ageEditText.setHint("You either a Vampire or Super Old");
                        ageEditText.setEnabled(true);
                        break;
                    case 5:
                        ageEditText.setHint("Hello There, My Fellow Homo Sapiens in Christ");
                        ageEditText.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        ageEditText.addTextChangedListener(textWatcher);
        bdayEditText.addTextChangedListener(textWatcher);

        sellButton.setOnClickListener(v -> {
            if (validateForm()) {
                saveFormData();
                Toast.makeText(MainActivity.this, "Sold off Successfully \"GRATIAS AGO TIBI\"",
                        Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }

    private void checkFieldsForEmptyValues() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String birthDate = bdayEditText.getText().toString().trim();

        sellButton.setEnabled(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !age.isEmpty() && !birthDate.isEmpty());
    }

    private boolean validateForm() {
        String ageText = ageEditText.getText().toString().trim();
        String emailText = emailEditText.getText().toString().trim();

        if (!isValidEmail(emailText)) {
            Toast.makeText(MainActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidAge(ageText)) {
            Toast.makeText(MainActivity.this, "Age must be between 0 and 500", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidAge(String age) {
        try {
            int ageValue = Integer.parseInt(age);
            return ageValue >= 0 && ageValue <= 500;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void saveFormData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", usernameEditText.getText().toString());
        values.put("email", emailEditText.getText().toString());
        values.put("password", passwordEditText.getText().toString());
        values.put("age", ageEditText.getText().toString());
        values.put("birthDate", bdayEditText.getText().toString());

        db.insert("users", null, values);
        db.close();
    }
}
