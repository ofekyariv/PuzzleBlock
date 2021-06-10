package com.example.puzzleBlock.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {

    private static GameConfig sInstance;
    private SharedPreferences sharedPreferences;
    private int highScore;

    private final Gson gson;

    private GameConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static GameConfig getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GameConfig(context);
        }
        return sInstance;
    }

    public static GameConfig getInstance() {
        return sInstance;
    }

    public void loadConfig() {
        highScore = sharedPreferences.getInt("score", 0);
    }

    public void saveHighScore(int highScore) {
        this.highScore = highScore;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", highScore);
        editor.apply();
    }

    public int getHighScore() {
        return highScore;
    }


}
