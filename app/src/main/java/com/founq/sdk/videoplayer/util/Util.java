package com.founq.sdk.videoplayer.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 */
public class Util {

    public static Bitmap mergeBitmaps(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();

        //create the new blank bitmap
        Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);

        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        int fgLeft = (bgWidth - fgWidth) / 2;
        int fgTop = (bgHeight - fgHeight) / 2;

        //draw fg into
        cv.drawBitmap(foreground, fgLeft, fgTop, null);
        //save all clip
        cv.save();
        //store
        cv.restore();
        return newBitmap;
    }

}
