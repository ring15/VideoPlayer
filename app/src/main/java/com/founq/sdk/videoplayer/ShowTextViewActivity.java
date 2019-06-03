package com.founq.sdk.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;

import com.founq.sdk.videoplayer.widget.HtmlTextView;

public class ShowTextViewActivity extends AppCompatActivity {

    private HtmlTextView mHtmlShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text_view);
        mHtmlShow = findViewById(R.id.html_show);
        String text = getIntent().getStringExtra("contentValue");
        String s = "<p>&nbsp; &nbsp; &nbsp;&nbsp; 《蜀门手游》&middot;《镇魂街》热血联动正式上线！ 身为镇魂将一员的&ldquo;墨韵&rdquo;化身为蜀门大陆全新 第六大职业，拜入蜀仙盟下尽全力拯救苍生。同时由许辰老师执笔《镇魂街》墨韵新篇问世。在《蜀门手游》2018&ldquo;玩乐3次方&rdquo;联合品牌发布会上，第一热血国漫《镇魂街》作者许辰先生分享新职业&ldquo;墨韵&rdquo;创作思路，深化联动的灵魂。</p><p>&nbsp; &nbsp; &nbsp; &nbsp; 联动不并不止此！除了线下的见面会，线上活动也是不停！在10月25日墨韵全平台上线后，登录游戏即可领取墨韵专属仙灵以及丰厚大礼！更可以在有妖气平台，为墨韵绘制墨韵的守护灵，赢取千元京东卡！有让我们一起期待墨韵的表现吧~！</p><br/><p>&nbsp;&nbsp;&nbsp; &nbsp;<a href=\"http://h5.shumensy.com/rakshasa-street\">点击查看详细联动内容&gt;&gt;&gt;&gt;&gt;</a></p><br/>"+text+"<div class=\"img\"><img src=\"http://img.shumen.oss.founq.com/37155/20181022/1540188208.jpg@!img\"></div>";

        mHtmlShow.setShowText(s, null);
        mHtmlShow.setMovementMethod(ArrowKeyMovementMethod.getInstance());
    }
}
