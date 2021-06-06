package com.example.puzzleBlock;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.puzzleBlock.common.NetworkUtil;
import com.example.puzzleBlock.common.ResourceManager;
import com.example.puzzleBlock.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
//                Log.d("aaa", "onReceive: network not connected");
                } else {
                    //loadAdsWhenInternetConnected();
                }
            }
        }
    };
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceManager resourceManager = ResourceManager.getInstance(this);
        resourceManager.loadData();
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        binding.mGameView.continueGameByActivityCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        binding.mGameView.pauseGameByActivityCycle();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        binding.mGameView.destroyThread();
        super.onDestroy();
    }

    public void openLinkRatingApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
