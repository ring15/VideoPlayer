package com.founq.sdk.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoViewActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private VideoView mVideoView;

    private int intPositionWhenPause = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mVideoView = findViewById(R.id.video_view);
        initVideoView();
    }

    private void initVideoView() {
        //初始化videoview控制条
        MediaController mediaController = new MediaController(this);
        //设置videoview控制条
        mVideoView.setMediaController(mediaController);
        //显示控制条
        mediaController.show();
        //监听播放完成后事件
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        //监听错误事件
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.e("text", "发生未知错误");

                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.e("text", "媒体服务器死机");
                        break;
                    default:
                        Log.e("text", "onError+" + what);
                        break;
                }
                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        //io读写错误
                        Log.e("text", "文件或网络相关的IO操作错误");
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        //文件格式不支持
                        Log.e("text", "比特流编码标准或文件不符合相关规范");
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        //一些操作需要太长时间来完成,通常超过3 - 5秒。
                        Log.e("text", "操作超时");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                        Log.e("text", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                        break;
                    default:
                        Log.e("text", "onError+" + extra);
                        break;
                }
                return false;
            }
        });
        //设置在视频文件在加载完毕以后的回调函数
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        //视频路径
        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        mVideoView.setVideoURI(uri);
        //设置全屏播放与否：1全屏，其余非全屏
        setVideoViewLayoutParams(0);
    }


    public void setVideoViewLayoutParams(int paramsType) {
        //全屏模式
        if (1 == paramsType) {
            //设置充满整个父布局
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //设置相对于父布局四边对齐
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //为VideoView添加属性
            mVideoView.setLayoutParams(LayoutParams);
        } else {
            //窗口模式
            //获取整个屏幕的宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            //设置窗口模式距离边框50
            int videoHeight = DisplayMetrics.heightPixels - 50;
            int videoWidth = DisplayMetrics.widthPixels - 50;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            //设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //为VideoView添加属性
            mVideoView.setLayoutParams(LayoutParams);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 视频播放与activity生命周期相结合
        // 启动视频播放
        mVideoView.start();
        // 设置获取焦点
        mVideoView.setFocusable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果当前页面暂停则保存当前播放位置
        intPositionWhenPause = mVideoView.getCurrentPosition();
        // 停止播放视频
        mVideoView.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 跳转到暂停播放位置
        if (intPositionWhenPause >= 0) {
            mVideoView.seekTo(intPositionWhenPause);
            // 初始播放位置
            intPositionWhenPause = -1;
        }
    }
}
