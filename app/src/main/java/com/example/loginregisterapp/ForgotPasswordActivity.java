package com.example.loginregisterapp;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUsername = findViewById(R.id.etUsername);
        btnFind = findViewById(R.id.btnFind);

        btnFind.setOnClickListener(v -> {
            String enteredUser = etUsername.getText().toString().trim();

            if (enteredUser.isEmpty()) {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String savedUser = prefs.getString("username", "");
            String savedPass = prefs.getString("password", "");

            // No registered user at all
            if (savedUser.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("No account found")
                        .setMessage("No registered users found. Please register first.")
                        .setPositiveButton("Register", (dialog, which) -> {
                            startActivity(new Intent(this, RegisterActivity.class));
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return;
            }

            // If entered username matches saved username (case-sensitive)
            if (enteredUser.equals(savedUser)) {
                // If password is empty for some reason
                if (savedPass.isEmpty()) {
                    Toast.makeText(this, "Password not set for this user.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show password in an AlertDialog with option to copy or go to login
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Password Found");
                builder.setMessage("Username: " + savedUser + "\nPassword: " + savedPass);
                builder.setPositiveButton("Copy Password", (dialog, which) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("password", savedPass);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to access clipboard", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("Go to Login", (dialog, which) -> {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
                builder.setNegativeButton("Close", null);
                builder.show();
            } else {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
