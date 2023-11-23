package com.example.experiment2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.Calendar;


public class ClockView extends View {
    private Paint circlePaint;
    private Paint hourHandPaint;
    private Paint minuteHandPaint;
    private Paint secondHandPaint;
    private int viewWidth;
    private int viewHeight;
    private int centerX;
    private int centerY;
    private int radius;
    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化画笔
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);

        hourHandPaint = new Paint();
        hourHandPaint.setColor(Color.BLACK);
        hourHandPaint.setStyle(Paint.Style.FILL);
        hourHandPaint.setStrokeWidth(10);

        minuteHandPaint = new Paint();
        minuteHandPaint.setColor(Color.BLACK);
        minuteHandPaint.setStyle(Paint.Style.FILL);
        minuteHandPaint.setStrokeWidth(5);

        secondHandPaint = new Paint();
        secondHandPaint.setColor(Color.RED);
        secondHandPaint.setStyle(Paint.Style.FILL);
        secondHandPaint.setStrokeWidth(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        centerX = viewWidth / 2;
        centerY = viewHeight / 2;
        radius = Math.min(viewWidth, viewHeight) / 2 - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制外圆
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // 绘制刻度线
        for (int i = 0; i < 12; i++) {
            float angle = (float) (i * Math.PI / 6);
            float startX = (float) (centerX + (radius - 40) * Math.sin(angle));
            float startY = (float) (centerY - (radius - 40) * Math.cos(angle));
            float endX = (float) (centerX + radius * Math.sin(angle));
            float endY = (float) (centerY - radius * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, circlePaint);
        }

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 绘制时针
        float hourAngle = (float) ((hour + minute / 60.0) * Math.PI / 6);
        float hourHandLength = radius * 0.5f;
        float hourX = (float) (centerX + hourHandLength * Math.sin(hourAngle));
        float hourY = (float) (centerY - hourHandLength * Math.cos(hourAngle));
        canvas.drawLine(centerX, centerY, hourX, hourY, hourHandPaint);

        // 绘制分针
        float minuteAngle = (float) ((minute + second / 60.0) * Math.PI / 30);
        float minuteHandLength = radius * 0.7f;
        float minuteX = (float) (centerX + minuteHandLength * Math.sin(minuteAngle));
        float minuteY = (float) (centerY - minuteHandLength * Math.cos(minuteAngle));
        canvas.drawLine(centerX, centerY, minuteX, minuteY, minuteHandPaint);

        // 绘制秒针
        float secondAngle = (float) (second * Math.PI / 30);
        float secondHandLength = radius * 0.9f;
        float secondX = (float) (centerX + secondHandLength * Math.sin(secondAngle));
        float secondY = (float) (centerY - secondHandLength * Math.cos(secondAngle));
        canvas.drawLine(centerX, centerY, secondX, secondY, secondHandPaint);
    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate(); // 触发重绘
            handler.postDelayed(this, 1000); // 每秒更新一次
        }
    };

    public void start() {
        handler.post(runnable); // 启动定时器
    }

    public void stop() {
        handler.removeCallbacks(runnable); // 停止定时器
    }


}
