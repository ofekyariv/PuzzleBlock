package com.example.puzzleBlock;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.puzzleBlock.common.ResourceManager;
import com.example.puzzleBlock.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    BroadcastReceiver receiver;
    Intent svc;
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceManager resourceManager = ResourceManager.getInstance(this);
        resourceManager.loadData();
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        svc = new Intent(this, SoundService.class);
        startService(svc);


    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastCharging();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, ifilter);
//        binding.mGameView.continueGameByActivityCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        stopService(svc);
    }

    @Override
    protected void onDestroy() {
        binding.mGameView.destroyThread();
        super.onDestroy();
    }
}
