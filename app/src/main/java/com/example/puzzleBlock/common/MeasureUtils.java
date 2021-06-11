package com.example.puzzleBlock.common;

import android.graphics.Paint;
import android.graphics.Rect;

public class MeasureUtils {

    public static void setTextSizeForHeight(Paint paint, float desiredHeight, String text) {

        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredHeight / bounds.height();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

}
