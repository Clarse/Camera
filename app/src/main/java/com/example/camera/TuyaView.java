package com.example.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TuyaView extends View {

    Paint paint = null;             //定义画笔
    Bitmap originalBitmap = null;   //存放原始图像
    Bitmap new1_Bitmap = null;      //存放从原始图像复制
    Bitmap new2_Bitmap = null;      //存放处理后的图像
    float startX = 0, startY = 0;    //画线的起点坐标
    float clickX = 0, clickY = 0;    //画线的终点坐标
    boolean isMove = true;         //设置是否画线的标记
    boolean isClear = false;        //设置是否清除涂鸦的
    int color = Color.BLACK;        //设置画笔的颜色（绿）
    float strokeWidth = 8.0f;       //设置画笔的宽度

    public TuyaView(Context context) {
        super(context);
    }

    public TuyaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TuyaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     * 设置画布bitmap
     * */
    public void setImage(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        new1_Bitmap = Bitmap.createBitmap(originalBitmap);
        new1_Bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
        invalidate();
    }

    /*
     * 清除所画的笔记
     * */
    public void clear() {
        isClear = true;
        new2_Bitmap = Bitmap.createBitmap(originalBitmap);
        invalidate();
    }

    /*
     * 设置线条粗细
     * */
    public void setStyle(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(HandWriting(new1_Bitmap), 0, 0, null);
    }

    public Bitmap HandWriting(Bitmap o_Bitmap) {
        Canvas canvas = null;
        if (isClear) {
            canvas = new Canvas(new2_Bitmap);
        } else {
            canvas = new Canvas(o_Bitmap);
        }
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        if (isMove) {
            canvas.drawLine(startX, startY, clickX, clickY, paint);
        }
        startX = clickX;
        startY = clickY;
        if (isClear) {
            return new2_Bitmap;
        }
        return o_Bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isMove = false;
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            isMove = true;
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

}
