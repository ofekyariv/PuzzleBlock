package com.example.puzzleBlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.example.puzzleBlock.common.GameConfig;
import com.example.puzzleBlock.common.ResourceManager;
import com.example.puzzleBlock.gameobject.Board;
import com.example.puzzleBlock.gameobject.GameOverMenu;
import com.example.puzzleBlock.gameobject.GamePlayHeader;
import com.example.puzzleBlock.gameobject.MovedGroupBlock;
import com.example.puzzleBlock.gameobject.MovedGroupBlockManager;


public class GameWorld implements View.OnTouchListener {
    public static final int STATE_GOTO_PLAYING = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_GAME_OVER_GO_IN = 3;
    public static final int STATE_GAME_OVER = 4;
    public static final int STATE_GAME_OVER_GO_OUT = 5;
    private static final float BOARD_X = 0.05f;
    private static final float BOARD_Y = 0.15f;
    private static final float BOARD_WIDTH = 0.9f;

    private static final long GAME_OVER_GO_IN_TIME = 700;
    private static final long GAME_PLAY_GO_IN_TIME = 400;
    private final Paint mPaint;
    private final Context context;
    private final GameConfig gameConfig;
    private int gameState = STATE_GOTO_PLAYING;
    private GamePlayHeader gamePlayHeader;
    private float gamePlayHeaderFixedPosY;
    private float gamePlayHeaderSpeedGoIn;
    private GameOverMenu gameOverMenu;
    private float gameOverMenuFixedPosY;
    private float gameOverMenuSpeedGoIn;

    // Game objects
    private MovedGroupBlockManager movedGroupBlockPooler;
    private MovedGroupBlock movedGroupBlockFirst;
    private MovedGroupBlock movedGroupBlockSecond;
    private MovedGroupBlock movedGroupBlockThird;
    private Board board;
    private int backgroundGreen;
    private Bitmap bitmapFooter;
    // Fixed positions
    private float blocksHookedPositionY;
    private float blocksFirstHookedPosX;
    private float blocksSecondHookedPosX;
    private float blocksThirdHookedPosX;
    private Rect footerRect;
    private boolean isFirstSetResolution = true;

    public GameWorld(Context context) {
        gameConfig = GameConfig.getInstance(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.context = context;
        initObjectGame();
    }

    private void initObjectGame() {
        gameOverMenu = new GameOverMenu(this);
        gamePlayHeader = new GamePlayHeader();

        movedGroupBlockPooler = new MovedGroupBlockManager();
        generateGroupBlocks();
        board = new Board(8, ResourceManager.getInstance().getBitmap("tiled_stone_ground"),
                ResourceManager.getInstance().getBitmap("stone"),
                ResourceManager.getInstance().getBitmap("stone"));
        board.setBoardCallback(new Board.BoardCallback() {
            @Override
            public void onDeletedBoardAnimFinish() {
                changeState(STATE_GAME_OVER_GO_IN);
            }
        });

        backgroundGreen = context.getResources().getColor(R.color.background_green);
        bitmapFooter = ResourceManager.getInstance().getBitmap("footer_background");
        footerRect = new Rect();
    }

    private void generateGroupBlocks() {
        movedGroupBlockFirst = movedGroupBlockPooler.getBlockRandom();
        movedGroupBlockSecond = movedGroupBlockPooler.getBlockRandom();
        movedGroupBlockThird = movedGroupBlockPooler.getBlockRandom();
    }

    public void setResolution(float deviceWidth, float deviceHeight) {
        if (!isFirstSetResolution) {
            return;
        }
        isFirstSetResolution = false;

        gameOverMenuFixedPosY = deviceHeight * 0.1f;
        gameOverMenu.setX(deviceWidth * 0.1f);
        gameOverMenu.setWidth(deviceWidth * 0.8f);
        gameOverMenu.setHeight(deviceHeight * 0.8f);
        gameOverMenu.setY(-gameOverMenu.getHeight());
        gameOverMenuSpeedGoIn = (gameOverMenuFixedPosY + gameOverMenu.getHeight()) / GAME_OVER_GO_IN_TIME;

        gamePlayHeaderFixedPosY = deviceHeight * (BOARD_Y / 6);
        gamePlayHeader.setH(deviceHeight * (BOARD_Y / 6 * 4));
        gamePlayHeader.setY(-gamePlayHeader.getH());
        gamePlayHeaderSpeedGoIn = (gamePlayHeaderFixedPosY + gamePlayHeader.getH()) / GAME_PLAY_GO_IN_TIME;
        gamePlayHeader.setX(BOARD_X * deviceWidth);
        gamePlayHeader.setW(BOARD_WIDTH * deviceWidth);

        float boardY = deviceHeight * BOARD_Y;
        float boardX = deviceWidth * BOARD_X;
        float boardWidth = deviceWidth * BOARD_WIDTH;
        board.setBound(boardX, boardY, boardWidth);
        movedGroupBlockPooler.setBlockMaxWidth(boardWidth / 8);

        // init fixed positions
        blocksHookedPositionY = boardY + boardWidth + deviceWidth * 0.25f;
        float distanceX = boardWidth / 6;
        blocksFirstHookedPosX = boardX + distanceX;
        blocksSecondHookedPosX = boardX + distanceX * 3;
        blocksThirdHookedPosX = boardX + distanceX * 5;

        footerRect.left = (int) (boardX);
        footerRect.right = (int) (deviceWidth - boardX);
        footerRect.top = (int) (blocksHookedPositionY - boardWidth / 8 * 0.4f * 5);
        footerRect.bottom = (int) (blocksHookedPositionY + boardWidth / 8 * 0.4f * 3);

        setGroupBlocksPosition();

        // First init group blocks
        movedGroupBlockFirst.setSizeMaximum(boardWidth / 8);
        movedGroupBlockFirst.setChildSizeNormal(boardWidth / 8 * 0.4f);
        movedGroupBlockSecond.setSizeMaximum(boardWidth / 8);
        movedGroupBlockSecond.setChildSizeNormal(boardWidth / 8 * 0.4f);
        movedGroupBlockThird.setSizeMaximum(boardWidth / 8);
        movedGroupBlockThird.setChildSizeNormal(boardWidth / 8 * 0.4f);
    }

    public void draw(Canvas canvas, int width, int height) {
        mPaint.setColor(backgroundGreen);
        canvas.drawRect(0, 0, width, height, mPaint);
        board.draw(canvas, mPaint);
        gamePlayHeader.draw(canvas, mPaint);
        canvas.drawBitmap(bitmapFooter, null, footerRect, mPaint);

        // draw block group
        movedGroupBlockFirst.draw(canvas, mPaint);
        movedGroupBlockSecond.draw(canvas, mPaint);
        movedGroupBlockThird.draw(canvas, mPaint);

        switch (gameState) {
            case STATE_GOTO_PLAYING:
            case STATE_PLAYING:

                break;
            case STATE_GAME_OVER:
            case STATE_GAME_OVER_GO_IN:
            case STATE_GAME_OVER_GO_OUT:
                gameOverMenu.draw(canvas, mPaint);
                break;
        }
    }

    public void update(long deltaTime) {
        if (!movedGroupBlockFirst.isHidden()) {
            movedGroupBlockFirst.update(deltaTime);
        }
        if (!movedGroupBlockSecond.isHidden()) {
            movedGroupBlockSecond.update(deltaTime);
        }
        if (!movedGroupBlockThird.isHidden()) {
            movedGroupBlockThird.update(deltaTime);
        }
        board.update(deltaTime);
        switch (gameState) {
            case STATE_GOTO_PLAYING:
                gamePlayHeader.setY(gamePlayHeader.getY() + gamePlayHeaderSpeedGoIn * deltaTime);
                if (gamePlayHeader.getY() > gamePlayHeaderFixedPosY) {
                    gamePlayHeader.setY(gamePlayHeaderFixedPosY);
                    changeState(STATE_PLAYING);
                }
                break;
            case STATE_PLAYING:
                gamePlayHeader.update(deltaTime);
                break;
            case STATE_GAME_OVER:
                break;
            case STATE_GAME_OVER_GO_IN:
                gameOverMenu.setY(gameOverMenu.getY() + gameOverMenuSpeedGoIn * deltaTime);
                if (gameOverMenu.getY() > gameOverMenuFixedPosY) {
                    gameOverMenu.setY(gameOverMenuFixedPosY);
                    changeState(STATE_GAME_OVER);
                }
                break;
            case STATE_GAME_OVER_GO_OUT:
                gameOverMenu.setY(gameOverMenu.getY() - gameOverMenuSpeedGoIn * deltaTime);
                if (gameOverMenu.getY() < -gameOverMenu.getHeight()) {
                    gameOverMenu.setY(-gameOverMenu.getHeight());
                    changeState(STATE_PLAYING);
                    resetGame();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (gameState) {
            case STATE_PLAYING:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    processPressingOnMovedBlock(motionEvent);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    processTouchUpMovedBlock(motionEvent);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    processMovingMovedBlock(motionEvent);
                }
                break;
            case STATE_GAME_OVER:
                gameOverMenu.processTouch(motionEvent);
                break;
        }
        return true;
    }

    private void processPressingOnMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden() && movedGroupBlockFirst.isInArea(motionEvent.getX(), motionEvent.getY())) {
            movedGroupBlockFirst.setIsPressed(true);
        }
        if (!movedGroupBlockSecond.isHidden() && movedGroupBlockSecond.isInArea(motionEvent.getX(), motionEvent.getY())) {
            movedGroupBlockSecond.setIsPressed(true);
        }
        if (!movedGroupBlockThird.isHidden() && movedGroupBlockThird.isInArea(motionEvent.getX(), motionEvent.getY())) {
            movedGroupBlockThird.setIsPressed(true);
        }
    }

    private void processMovingMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden() && movedGroupBlockFirst.isPressed()) {
            movedGroupBlockFirst.setMiddleX(motionEvent.getX());
            movedGroupBlockFirst.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockFirst.getFirstBlockMiddleX(), movedGroupBlockFirst.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockFirst.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockFirst.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
        if (!movedGroupBlockSecond.isHidden() && movedGroupBlockSecond.isPressed()) {
            movedGroupBlockSecond.setMiddleX(motionEvent.getX());
            movedGroupBlockSecond.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockSecond.getFirstBlockMiddleX(), movedGroupBlockSecond.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockSecond.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockSecond.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
        if (!movedGroupBlockThird.isHidden() && movedGroupBlockThird.isPressed()) {
            movedGroupBlockThird.setMiddleX(motionEvent.getX());
            movedGroupBlockThird.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockThird.getFirstBlockMiddleX(), movedGroupBlockThird.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockThird.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockThird.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
    }

    private void processTouchUpMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden()) {
            if (movedGroupBlockFirst.isPressed()) {
                movedGroupBlockFirst.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockFirst.getFirstBlockMiddleX(), movedGroupBlockFirst.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockFirst.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockFirst.getBlockMatrix());
                    movedGroupBlockFirst.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                        gameConfig.saveHighScore(gamePlayHeader.getPoint(), context);
                    }
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockFirst.startMoveHome(blocksFirstHookedPosX, blocksHookedPositionY);
                }
            }
        }
        if (!movedGroupBlockSecond.isHidden()) {
            if (movedGroupBlockSecond.isPressed()) {
                movedGroupBlockSecond.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockSecond.getFirstBlockMiddleX(), movedGroupBlockSecond.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockSecond.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockSecond.getBlockMatrix());
                    movedGroupBlockSecond.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                        gameConfig.saveHighScore(gamePlayHeader.getPoint(), context);
                    }
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockSecond.startMoveHome(blocksSecondHookedPosX, blocksHookedPositionY);
                }
            }
        }
        if (!movedGroupBlockThird.isHidden()) {
            if (movedGroupBlockThird.isPressed()) {
                movedGroupBlockThird.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockThird.getFirstBlockMiddleX(), movedGroupBlockThird.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockThird.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockThird.getBlockMatrix());
                    movedGroupBlockThird.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                        gameConfig.saveHighScore(gamePlayHeader.getPoint(), context);
                    }
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockThird.startMoveHome(blocksThirdHookedPosX, blocksHookedPositionY);
                }
            }
        }

        board.resetShadowBoard();

        if (movedGroupBlockFirst.isHidden() && movedGroupBlockSecond.isHidden() && movedGroupBlockThird.isHidden()) {
            generateGroupBlocks();
            if (checkGameOver()) {
                movedGroupBlockFirst = movedGroupBlockPooler.getAFixedGroupBlock(board);
            }
            setGroupBlocksPosition();
            checkPlaceAbleForThreeGroupBlock();
        } else {
            if (checkGameOver()) {
                board.startResetBoard();
            }
        }
    }

    private boolean checkGameOver() {
        boolean gameOver = true;
        if (!movedGroupBlockFirst.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockFirst.getBlockMatrix())) {
            gameOver = false;
        }
        if (!movedGroupBlockSecond.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockSecond.getBlockMatrix())) {
            gameOver = false;
        }
        if (!movedGroupBlockThird.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockThird.getBlockMatrix())) {
            gameOver = false;
        }
        if (gameOver) {
            gameConfig.saveHighScore(gamePlayHeader.getPoint(), context);
        }
        return gameOver;
    }

    public void changeState(int state) {
        this.gameState = state;
    }

    private void checkPlaceAbleForThreeGroupBlock() {
        if (!movedGroupBlockFirst.isHidden()) {
            movedGroupBlockFirst.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockFirst.getBlockMatrix()));
        }
        if (!movedGroupBlockSecond.isHidden()) {
            movedGroupBlockSecond.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockSecond.getBlockMatrix()));
        }
        if (!movedGroupBlockThird.isHidden()) {
            movedGroupBlockThird.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockThird.getBlockMatrix()));
        }
    }

    private void setGroupBlocksPosition() {
        movedGroupBlockFirst.setMiddleX(blocksFirstHookedPosX);
        movedGroupBlockFirst.setMiddleY(blocksHookedPositionY);
        movedGroupBlockFirst.resetStatus();
        movedGroupBlockSecond.setMiddleX(blocksSecondHookedPosX);
        movedGroupBlockSecond.setMiddleY(blocksHookedPositionY);
        movedGroupBlockSecond.resetStatus();
        movedGroupBlockThird.setMiddleX(blocksThirdHookedPosX);
        movedGroupBlockThird.setMiddleY(blocksHookedPositionY);
        movedGroupBlockThird.resetStatus();
    }

    public void resetGame() {
        board.resetBoard();
        generateGroupBlocks();
        setGroupBlocksPosition();
        gamePlayHeader.setCurrentPointLabel(0);
    }
}
