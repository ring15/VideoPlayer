package com.founq.sdk.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class SurfaceMediaPlayerActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private MediaPlayer mPlayer;
    private int intPositionWhenPause = -1;

    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_media_player);
        mSurfaceView = findViewById(R.id.surface_view);
        path = getIntent().getStringExtra("uri");
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mSurfaceView.getHolder().setKeepScreenOn(true);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (intPositionWhenPause >= 0){
                    try {
                        play();
                        mPlayer.seekTo(intPositionWhenPause);
                        intPositionWhenPause = -1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                try {
                    play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_pause:
                if (mPlayer.isPlaying()){
                    mPlayer.pause();
                } else {
                    mPlayer.start();
                }
                break;
            case R.id.btn_stop:
                if (mPlayer.isPlaying()){
                    mPlayer.stop();
                }
                break;
        }
    }

    private void play() throws IOException {
        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        File file = new File(path == null?"/storage/emulated/0/DCIM/Camera/VID_20190603_035936.mp4":path);
        mPlayer.reset();
//        mPlayer.setDataSource(this, uri);
        mPlayer.setDataSource(file.getPath());
        mPlayer.setDisplay(mSurfaceView.getHolder());
        mPlayer.prepare();
        mPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying()){
            intPositionWhenPause = mPlayer.getCurrentPosition();
            mPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.release();
        }
    }
}
