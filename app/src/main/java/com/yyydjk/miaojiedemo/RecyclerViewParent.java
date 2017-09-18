package com.yyydjk.miaojiedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author ThomasWang
 * @since 2017/9/19 0:30
 */

public class RecyclerViewParent extends FrameLayout implements NestedScrollingParent {
    private final NestedScrollingParentHelper mParentHelper;

    public RecyclerViewParent(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i("demoscroll", "onNestedScrollAccepted target = " + target);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.i("demoscroll", "onNestedScrollAccepted target = " + target);
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.i("demoscroll", "onStopNestedScroll");
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        Log.i("demoscroll", "onNestedScroll target = " + target);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.i("demoscroll", "onNestedPreScroll target = " + target);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i("demoscroll", "onNestedFling target = " + target);
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i("demoscroll", "onNestedPreFling target = " + target);
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.i("demoscroll", "getNestedScrollAxes");
        return mParentHelper.getNestedScrollAxes();
    }
}
