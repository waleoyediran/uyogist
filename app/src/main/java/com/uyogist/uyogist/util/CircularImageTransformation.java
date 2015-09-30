package com.uyogist.uyogist.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.squareup.picasso.Transformation;

/**
 * Transform Image into circular avatar
 * Created by oyewale on 4/6/15.
 */
public class CircularImageTransformation implements Transformation {
    int radius = 10;

    public CircularImageTransformation(final int radius)
    {
        this.radius = radius;
    }

    public CircularImageTransformation()
    {
    }

    @Override
    public Bitmap transform(final Bitmap source)
    {
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawCircle(source.getWidth() / 2 + 0.7f, source.getHeight() / 2 + 0.7f, source.getWidth() / 2 - 1.1f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, rect, rect, paint);

        if(source != output) {
            source.recycle();
        }
        return output;
    }

    @Override
    public String key()
    {
        return "circular" + String.valueOf(radius);
    }
}
