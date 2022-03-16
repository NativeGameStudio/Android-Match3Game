package com.example.matchgamesample.game.counter;

import android.widget.TextView;

import com.example.matchgamesample.R;
import com.example.matchgamesample.engine.GameEngine;
import com.example.matchgamesample.engine.GameEvent;
import com.example.matchgamesample.engine.GameObject;
import com.example.matchgamesample.level.Level;

public class ScoreCounter extends GameObject {
    private static final int POINTS_GAINED_PER_FRUIT = 10;

    private final TextView mText;
    private final Level mLevel;
    private int mPoints;
    private boolean mPointsHaveChanged;

    public ScoreCounter(GameEngine gameEngine) {
        mText = (TextView) gameEngine.mActivity.findViewById(R.id.score);
        mLevel = gameEngine.mLevel;
        mPointsHaveChanged = false;
    }

    @Override
    public void startGame() {
        mPoints = 0;
        mPointsHaveChanged = true;
    }

    @Override
    public void onUpdate(long elapsedMillis) {

    }

    @Override
    public void onDraw() {
        if (mPointsHaveChanged) {
            mText.setText(String.valueOf(mPoints));
            mPointsHaveChanged = false;
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        if (gameEvents == GameEvent.PLAYER_SCORE) {
            mPoints += POINTS_GAINED_PER_FRUIT;
            mLevel.mScore += POINTS_GAINED_PER_FRUIT;
            mPointsHaveChanged = true;
        }
    }
}