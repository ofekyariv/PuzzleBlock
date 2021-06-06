package com.example.puzzleBlock.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.puzzleBlock.GameWorld;
import com.example.puzzleBlock.common.Constant;
import com.example.puzzleBlock.common.ResourceManager;


public class GameOverMenu {
    private static final float BT_PLAY_WIDTH = 0.5f;
    private float x, y;
    private float w, h;
    private final ImageButton btPlay;
    private final GameWorld gameWorld;

    public GameOverMenu(GameWorld gameWorld) {
        btPlay = new ImageButton(ResourceManager.getInstance().getBitmap("stone"), ResourceManager.getInstance().getBitmap("tiled_stone_ground"));
        this.gameWorld = gameWorld;
    }

    public void setX(float x) {
        this.x = x;
        updateButtonPosition();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateButtonPosition();
    }

    public void setWidth(float w) {
        this.w = w;
        updateButtonPosition();
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(float h) {
        this.h = h;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Constant.WOOD_COLOR);
        canvas.drawRect(x, y, x + w, y + h, paint);

        btPlay.draw(canvas, paint);
    }

    public void processTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (btPlay.isInArea(motionEvent.getX(), motionEvent.getY())) {
                btPlay.setIspressed(true);
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (btPlay.isInArea(motionEvent.getX(), motionEvent.getY())) {
                gameWorld.changeState(GameWorld.STATE_GAME_OVER_GO_OUT);
            }
            btPlay.setIspressed(false);
        }
    }

    private void updateButtonPosition() {
        btPlay.setW(w * BT_PLAY_WIDTH);
        btPlay.setH(btPlay.getW() * 0.3f);
        btPlay.setX(this.x + w / 2 - btPlay.getW() / 2);
        btPlay.setY(this.y + h / 2);
    }
}
