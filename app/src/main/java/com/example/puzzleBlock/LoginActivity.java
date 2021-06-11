package com.example.puzzleBlock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public static String username = "";
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechUsername();
            }
        });
    }

    public void chechUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = editText.getText().toString();
        String prefUsername = sharedPreferences.getString(username, "");
        //first entry
        if (prefUsername.equals("")) {
            Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
        } else {
            int highScore = sharedPreferences.getInt(username + "score", 0);
            Toast.makeText(this, "Welcome back " + username + "! your highest score: " + highScore, Toast.LENGTH_LONG).show();
        }
        editor.putString(username, username);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}