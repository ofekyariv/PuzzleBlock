package com.example.puzzleBlock;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread mGameThread;
    private GameWorld mGameWorld;

    public GameSurfaceView(Context context) {
        super(context);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
        mGameWorld = new GameWorld(getContext());
        setOnTouchListener(mGameWorld);
        mGameThread = new GameThread(this);
        mGameThread.setRunning(true);
        mGameThread.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mGameWorld != null) {
            mGameWorld.draw(canvas, getWidth(), getHeight());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mGameWorld.setResolution(getWidth(), getHeight());
        mGameThread.setSurfaceHolder(surfaceHolder);
        mGameThread.setPauseGameBySurfaceViewDestroy(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mGameThread.setPauseGameBySurfaceViewDestroy(true);
    }

    public void update(long deltaTime) {
        if (mGameWorld != null) {
            mGameWorld.update(deltaTime);
        }
    }

    public void destroyThread() {
        mGameThread.destroyThread();
    }
}
