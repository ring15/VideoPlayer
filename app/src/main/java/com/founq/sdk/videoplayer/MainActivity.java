package com.founq.sdk.videoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x01);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video:
                //直接调用系统中的视频播放器
                Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
                break;
            case R.id.btn_video_view:
                Intent intent1 = new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_surface_view:
                Intent intent2 = new Intent(MainActivity.this, SurfaceMediaPlayerActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_spannable:
                Intent intent3 = new Intent(MainActivity.this, SpannableTextActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_exoplayer:
                startActivity(new Intent(MainActivity.this, ExoPlayerActivity.class));
                break;
            case R.id.btn_jiaozi:
                startActivity(new Intent(MainActivity.this, JiaoZiVideoPlayerActivity.class));
        }
    }
}
