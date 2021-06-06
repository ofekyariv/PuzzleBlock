package com.example.puzzleBlock.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageButton extends Button {
    private final Bitmap bitmapIdle;
    private final Bitmap bitmapPressed;

    public ImageButton(Bitmap bitmapIdle, Bitmap bitmapPressed) {
        super();
        this.bitmapIdle = bitmapIdle;
        this.bitmapPressed = bitmapPressed;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isPressed) {
            canvas.drawBitmap(bitmapPressed, null, rect, paint);
        } else {
            canvas.drawBitmap(bitmapIdle, null, rect, paint);
        }
    }
}
