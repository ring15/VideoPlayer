package com.founq.sdk.videoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class JiaoZiVideoPlayerActivity extends AppCompatActivity {

    private JzvdStd mJzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_zi_video_player);
        mJzvdStd = findViewById(R.id.jazd_view);
        mJzvdStd.setUp("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "title");
        mJzvdStd.thumbImageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round));
        mJzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
