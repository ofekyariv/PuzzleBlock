package com.example.puzzleBlock.common;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final List<Bitmap> bitmaps;
    private final List<Long> bitmapsDelay;
    private final boolean isRepeated;
    private long lastNextFrame;
    private int index;

    public Animation(boolean isRepeated) {
        bitmaps = new ArrayList<>();
        bitmapsDelay = new ArrayList<>();
        this.isRepeated = isRepeated;
    }

    public void addFrame(Bitmap bitmap, long delay) {
        bitmaps.add(bitmap);
        bitmapsDelay.add(delay);
    }

    public void reset() {
        index = 0;
    }

    public void update() {
        if (index >= bitmaps.size()) {
            index = 0;
            return;
        }
        if (System.currentTimeMillis() - lastNextFrame >= bitmapsDelay.get(index)) {
            index++;
            if (index >= bitmapsDelay.size()) {
                if (isRepeated) {
                    reset();
                } else {
                    index = bitmapsDelay.size() - 1;
                }
            }
            lastNextFrame = System.currentTimeMillis();
        }
    }

    public Bitmap getCurrentBitmap() {
        return bitmaps.get(index);
    }
}
