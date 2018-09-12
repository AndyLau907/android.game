package com.andy.note.Game1.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by andy on 2018/9/3.
 */

public class TimeView extends View {

    private static String LOG_TAG = "TimeView";
    private Context mContext;
    //游戏时间
    private int time = 100;
    //view宽度
    private float width;
    //游戏难度
    private int range = 1;
    //画笔
    private Paint paint;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.e(LOG_TAG, "121212");
        width = getWidth();
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, paint);
        if (time != 0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#15cef9"));
            //Log.e(LOG_TAG, (((float) time) / 100) * width + "+++" + time + "+++" + width);
            canvas.drawRect(2, 2, (((float) time) / 100) * width - 2, getHeight() - 2, paint);
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void addTime() {
        if (time + 2 > 100) {
            time = 100;
        } else {
            time += 4;
        }
    }

    public void subTime() {
        time--;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
