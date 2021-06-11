package com.example.puzzleBlock.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;

import static com.example.puzzleBlock.LoginActivity.username;

public class GameConfig {

    private static GameConfig sInstance;
    private SharedPreferences sharedPreferences;

    private GameConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
    }

    public static GameConfig getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GameConfig(context);
        }
        return sInstance;
    }

    public void saveHighScore(int highScore, Context context) {
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        int currentScore = sharedPreferences.getInt(username + "score", 0);
        if (highScore > currentScore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(username + "score", highScore);
            editor.apply();
        }
    }
}
