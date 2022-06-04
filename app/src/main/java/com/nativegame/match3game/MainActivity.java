package com.nativegame.match3game;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.nativegame.match3game.database.DatabaseHelper;
import com.nativegame.match3game.dialog.BaseDialog;
import com.nativegame.match3game.effect.sound.SoundEvent;
import com.nativegame.match3game.effect.sound.SoundManager;
import com.nativegame.match3game.fragment.BaseFragment;
import com.nativegame.match3game.fragment.GameFragment;
import com.nativegame.match3game.fragment.LoadingFragment;
import com.nativegame.match3game.fragment.WinDialogFragment;
import com.nativegame.match3game.level.LevelManager;

/**
 * Created by Oscar Liang on 2022/02/23
 */

/*
 *    Copyright 2022 Oscar Liang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";

    private LevelManager mLevelManager;
    private SoundManager mSoundManager;
    private DatabaseHelper mDatabaseHelper;
    private BaseDialog mCurrentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MatchGameSample);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Add LoadingFragment to screen
            navigateToFragment(new LoadingFragment());
        }

        mLevelManager = new LevelManager(this);
        mSoundManager = new SoundManager(this);
        mDatabaseHelper = new DatabaseHelper(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    public LevelManager getLevelManager() {
        return mLevelManager;
    }

    public SoundManager getSoundManager() {
        return mSoundManager;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public void startGame(int level) {
        // Navigate the game fragment, which makes the start automatically
        navigateToFragment(GameFragment.newInstance(level));
    }

    public void navigateToFragment(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void navigateBack() {
        // Do a push on the navigation history
        getSupportFragmentManager().popBackStack();
    }

    public void showDialog(BaseDialog newDialog, boolean dismissOtherDialog) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (dismissOtherDialog) {
                mCurrentDialog.dismiss();
            } else {
                return;
            }
        }
        mCurrentDialog = newDialog;
        mCurrentDialog.show();
        mSoundManager.playSoundForSoundEvent(SoundEvent.SWEEP2);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            return;
        }
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundManager.pauseBgMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment != null && !(fragment instanceof GameFragment || fragment instanceof WinDialogFragment)) {
            mSoundManager.resumeBgMusic();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoundManager.unloadMusic();
        mSoundManager.unloadSounds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            } else {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

}