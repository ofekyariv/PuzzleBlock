package com.example.puzzleBlock.common;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager sResourceManager;
    private final int MAX_STREAMS = 5;
    private final Context mContext;
    private final Map<String, Bitmap> bitmapMap;
    private boolean loaded;

    private ResourceManager(Context context) {
        mContext = context;
        bitmapMap = new HashMap<>();
    }

    public static ResourceManager getInstance(Context context) {
        if (sResourceManager == null) {
            sResourceManager = new ResourceManager(context);
        }
        return sResourceManager;
    }

    public static ResourceManager getInstance() {
        return sResourceManager;
    }

    public void loadData() {
        try {
            Bitmap bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/stone.png");
            bitmapMap.put("stone", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/tiled_stone_ground.png");
            bitmapMap.put("tiled_stone_ground", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/header_background.png");
            bitmapMap.put("header_background", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/footer_background.png");
            bitmapMap.put("footer_background", bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String name) {
        return bitmapMap.get(name);
    }

}
