package com.example.pinkusermanager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class LoadedUserDataActivity extends AppCompatActivity {
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_user_data);
        Init();
    }

    private void Init() {

        try {
            mPlayer = MediaPlayer.create(this, R.raw.noise);
            if (mPlayer != null) {
                mPlayer.setVolume(0.2f, 0.2f);
                mPlayer.setLooping(true);
                mPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoadedUserDataActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayer != null) mPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null && mPlayer.isPlaying()) mPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer != null && mPlayer.isPlaying()) mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}