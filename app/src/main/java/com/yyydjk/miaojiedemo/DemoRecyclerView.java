package com.yyydjk.miaojiedemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author ThomasWang
 * @since 2017/9/18 21:21
 */

public class DemoRecyclerView extends RecyclerView {
    private GestureDetectorCompat gestureDetectorCompat;

    public DemoRecyclerView(Context context) {
        this(context, null);
    }

    public DemoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        gestureDetectorCompat = new GestureDetectorCompat(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        boolean canScrollDown = canScrollVertically(1);
                        if (!canScrollDown) {
                            Log.i("DemoRecyclerView", "dy = " + distanceY);
                        }
                        return super.onScroll(e1, e2, distanceX, distanceY);
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return super.onTouchEvent(e);
    }
}
