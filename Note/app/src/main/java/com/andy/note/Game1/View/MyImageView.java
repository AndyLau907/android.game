package com.andy.note.Game1.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.util.Log;
import android.widget.ImageView;


/**
 * Created by andy on 2018/8/21.
 */

@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView {
    private static String LOG_TAG = "Game1View";
    //上下文
    private Context mContext;
    //画笔
    private Paint paint;
    //是否画边框
    private boolean isDrawBorder = false;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        paint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawBorder) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            Rect rect = new Rect(1, 1, getWidth()-1, getHeight()-1);
            canvas.drawRect(rect, paint);
        }
    }

    public void setIsDrawBorder(boolean is) {
        isDrawBorder = is;
        //Log.e(LOG_TAG,"isdrawBorder = "+isDrawBorder);
    }
}

