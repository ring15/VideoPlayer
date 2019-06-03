package com.founq.sdk.videoplayer.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.founq.sdk.videoplayer.MainActivity;
import com.founq.sdk.videoplayer.R;
import com.founq.sdk.videoplayer.SurfaceMediaPlayerActivity;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import java.util.Locale;

public class HtmlTextView extends AppCompatTextView {

    private Context mContext;

    public HtmlTextView(Context context) {
        super(context);
        mContext = context;
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setShowText(String text, BufferType type) {
        CharSequence result;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            result = Html.fromHtml(text, 1, new MyImageGetter(mContext), new MyTagHandler());
//        } else {
//            result = Html.fromHtml(text, new MyImageGetter(mContext), new MyTagHandler());
//        }
        result = HtmlTagHandler.fromHtml(text, new MyImageGetter(mContext), new MyTagHandler(), mContext);
        super.setText(result, type);
    }


    private long mLastActionDownTime = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence text = getText();
        if (text != null && text instanceof Spannable) {
            handleLinkMovementMethod(this, (Spannable) text, event);
        }

        return super.onTouchEvent(event);
    }

    private boolean handleLinkMovementMethod(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    long actionUpTime = System.currentTimeMillis();
                    if (actionUpTime - mLastActionDownTime > ViewConfiguration.getLongPressTimeout()) { //长按事件，取消LinkMovementMethod处理，即不处理ClickableSpan点击事件
                        return false;
                    }
                    link[0].onClick(widget);
                    Selection.removeSelection(buffer);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    mLastActionDownTime = System.currentTimeMillis();
                }
            }
        }
        return false;
    }


    public class MyImageGetter implements Html.ImageGetter {

        private Context mContext;
        private myDrawable drawable;

        public MyImageGetter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public Drawable getDrawable(final String source) {
            String s = source.split("@")[0];
            drawable = new myDrawable();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher);
            RequestBuilder load = Glide.with(mContext)
                    .asDrawable()
                    .load(s)
                    .apply(options);
            Target target = new DrawableTaget(drawable);
            load.into(target);
            return drawable;
        }

        class myDrawable extends BitmapDrawable {

            public void setDrawable(Drawable drawable) {
                this.drawable = drawable;
            }

            public Drawable getDrawable() {
                return drawable;
            }

            private Drawable drawable;

            @Override
            public void draw(@NonNull Canvas canvas) {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            }
        }

        class DrawableTaget implements Target<Drawable> {

            private myDrawable mDrawable;

            public DrawableTaget(myDrawable drawable) {
                mDrawable = drawable;
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                if (placeholder == null) {
                    return;
                }
                Rect rect = new Rect(0, 0, placeholder.getIntrinsicWidth(), placeholder.getIntrinsicHeight());
                placeholder.setBounds(rect);
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (errorDrawable == null) {
                    return;
                }
                Rect rect = new Rect(0, 0, errorDrawable.getIntrinsicWidth(), errorDrawable.getIntrinsicHeight());
                errorDrawable.setBounds(rect);
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(errorDrawable);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Rect rect = new Rect(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                resource.setBounds(rect);
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(resource);
                HtmlTextView.this.setText(HtmlTextView.this.getText());
                HtmlTextView.this.invalidate();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb) {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(@Nullable Request request) {
            }

            @Nullable
            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        }

    }

    public class MyTagHandler implements HtmlTagHandler.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, Attributes attrs) {

            //处理超链接标签
            if (tag.toLowerCase(Locale.getDefault()).equals("html")) {
                // 获取长度
                int len = output.length();
                URLSpan[] urls = output.getSpans(0, len, URLSpan.class);
                if (urls.length != 0) {
                    for (URLSpan urlSpan : urls) {
                        int start = output.getSpanStart(urlSpan);
                        int end = output.getSpanEnd(urlSpan);
                        String url = urlSpan.getURL();
                        output.removeSpan(urlSpan);
                        output.setSpan(new ClickableURL(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                if (!opening) {
                    // 获取长度
                    int len = output.length();
                    // 获取图片地址
                    ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                    String imgURL = images[0].getSource();
                    String s = imgURL.split("@")[0];
                    // 使图片可点击并监听点击事件
                    output.setSpan(new ClickableImage(s), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("video")) {
                if (!opening) {
                    // 获取长度
                    int len = output.length();
                    // 获取图片地址
                    ImageSpan[] videoString = output.getSpans(len - 1, len, ImageSpan.class);
                    String videoUrl = videoString[0].getSource();
                    // 使图片可点击并监听点击事件
                    output.setSpan(new ClickVideo(videoUrl), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }


    /**
     * 图片点击事件
     */
    private class ClickableImage extends ClickableSpan {

        private String url;

        public ClickableImage(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 进行图片点击之后的处理
            Toast.makeText(mContext, "点击图片", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(mContext, PhotoPreviewActivity.class);
//            intent.putExtra("imgUri", url);
//            mContext.startActivity(intent);
        }
    }

    /**
     * 链接点击事件
     */
    private class ClickableURL extends ClickableSpan {

        private String url;

        public ClickableURL(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Toast.makeText(mContext, "点击链接", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            intent.setData(Uri.parse(url));
//            mContext.startActivity(intent);
//            Intent intent = new Intent(mContext, WebActivity.class);
//            intent.putExtra("imgUri", url);
//            mContext.startActivity(intent);
        }
    }

    private class ClickVideo extends ClickableSpan{
        private String url;
        public ClickVideo(String videoUrl) {
            url = videoUrl;
        }

        @Override
        public void onClick(@NonNull View widget) {
            Uri uri = Uri.parse(url);
            Intent intent2 = new Intent(mContext, SurfaceMediaPlayerActivity.class);
            intent2.putExtra("uri", uri);
            mContext.startActivity(intent2);
        }
    }
}
