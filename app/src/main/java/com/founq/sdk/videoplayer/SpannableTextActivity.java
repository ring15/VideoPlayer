package com.founq.sdk.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.founq.sdk.videoplayer.util.RealPathFromUriUtils;
import com.founq.sdk.videoplayer.util.Util;

import java.io.File;
import java.util.ArrayList;

public class SpannableTextActivity extends AppCompatActivity {

    public static int REQUEST_CODE_CHOOSER = 0x01;

    private EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_text);
        mText = findViewById(R.id.et_text);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video_post:
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_CHOOSER);
                break;
            case R.id.btn_show:
                String contentValue = mText.getText().toString().trim();
                Log.i("contentValue",contentValue);
                Intent intent1 = new Intent(SpannableTextActivity.this, ShowTextViewActivity.class);
                intent1.putExtra("contentValue", contentValue);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 图文混排 插入视频
     *
     * @param paths
     */
    private void insertPic(ArrayList<String> paths) {
        //开始插入图片
        for (String path : paths) {
            Bitmap thumb = null;
            String videoUrl = "";
            thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
            if (thumb == null) {
                // thumb = null; // TODO should load first frame bitmap
                thumb = Bitmap.createBitmap(400, 300, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(thumb);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.BLACK);
                canvas.drawRect(0, 0, 400, 300, paint);
            }

            Bitmap play = BitmapFactory.decodeResource(getResources(), R.drawable.play);
            Bitmap video = Util.mergeBitmaps(thumb, play);
            // 根据Bitmap对象创建ImageSpan对象
            ImageSpan imageSpan = new ImageSpan(this, video);
            // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像，先使用编号占位
            String tempUrl;
            StringBuilder htmlBuilder = new StringBuilder("<video src=\"");
            String path2 = TextUtils.isEmpty(videoUrl) ? path : videoUrl;
            htmlBuilder.append(path2);
            htmlBuilder.append("\" uri=\"");
            htmlBuilder.append(path);
            htmlBuilder.append("\" controls=\"controls\">");
//		htmlBuilder.append("您的浏览器不支持 video 标签。");
            htmlBuilder.append("</video>");
            tempUrl = htmlBuilder.toString();
            SpannableString spannableString = new SpannableString(tempUrl);
            // 用ImageSpan对象替换你指定的字符串
            spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            // 将选择的图片追加到EditText中光标所在位置
            int index = mText.getSelectionStart(); // 获取光标所在位置
            if (index < 0 || index >= mText.length()) {
                mText.append("\n");
                mText.append(spannableString);
                //mText.append("\n");
            } else {
                mText.getText().insert(index, "\n");
                mText.getText().insert(index + 1, spannableString);
                //mContentText.getText().insert(index + spannableString.length() + 1, "\n");
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (REQUEST_CODE_CHOOSER == requestCode) {
                String realPath = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                ArrayList<String> mPaths = new ArrayList<>();
                mPaths.add(realPath);
                insertPic(mPaths);
            }
        }
    }
}
