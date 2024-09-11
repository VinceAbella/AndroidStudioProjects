package com.vins.helloworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(view -> showEditDialog());
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit TextView");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputText = new EditText(this);
        inputText.setHint("Enter new text");
        layout.addView(inputText);

        final EditText inputColor = new EditText(this);
        inputColor.setHint("Enter text color (e.g., #FF0000)");
        layout.addView(inputColor);

        final EditText inputSize = new EditText(this);
        inputSize.setHint("Enter text size (e.g., 18)");
        inputSize.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputSize);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = inputText.getText().toString();
                String newColor = inputColor.getText().toString();
                String newSizeStr = inputSize.getText().toString();

                if (!newText.isEmpty()) {
                    textView.setText(newText);
                }

                if (!newColor.isEmpty()) {
                    try {
                        textView.setTextColor(Color.parseColor(newColor));
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(MainActivity.this, "Invalid color format", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!newSizeStr.isEmpty()) {
                    try {
                        float newSize = Float.parseFloat(newSizeStr);
                        if (newSize <= 100) {
                            textView.setTextSize(newSize);
                        } else {
                            Toast.makeText(MainActivity.this, "Text size too large", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid text size", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
