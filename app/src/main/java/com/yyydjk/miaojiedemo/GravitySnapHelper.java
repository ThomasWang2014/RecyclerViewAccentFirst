package com.yyydjk.miaojiedemo;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;


public class GravitySnapHelper extends LinearSnapHelper {

    private OrientationHelper mVerticalHelper;
    private OrientationHelper mHorizontalHelper;
    private int mGravity;
    private boolean mIsRtl;
    private RecyclerView mRecyclerView;

    @SuppressLint("RtlHardcoded")
    public GravitySnapHelper(int gravity) {
        mGravity = gravity;
        if (mGravity == Gravity.LEFT) {
            mGravity = Gravity.START;
        } else if (mGravity == Gravity.RIGHT) {
            mGravity = Gravity.END;
        }
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        if (recyclerView != null) {
            mIsRtl = recyclerView.getContext().getResources().getBoolean(R.bool.is_rtl);
        }
        super.attachToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];

        if (layoutManager.canScrollHorizontally()) {
            if (mGravity == Gravity.START) {
                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
            } else { // END
                out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager));
            }
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            if (mGravity == Gravity.TOP) {
                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
            } else { // BOTTOM
                out[1] = distanceToEnd(targetView, getVerticalHelper(layoutManager));
            }
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            int lastCompletelyVisibleItemPosition =
                    ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                return layoutManager.findViewByPosition(layoutManager.getItemCount() - 1);
            }
            switch (mGravity) {
                case Gravity.START:
                    return findStartView(layoutManager, getHorizontalHelper(layoutManager));
                case Gravity.TOP:
                    return findStartView(layoutManager, getVerticalHelper(layoutManager));
                case Gravity.END:
                    return findEndView(layoutManager, getHorizontalHelper(layoutManager));
                case Gravity.BOTTOM:
                    return findEndView(layoutManager, getVerticalHelper(layoutManager));
            }
        }

        return super.findSnapView(layoutManager);
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        if (mIsRtl) {
            return distanceToEnd(targetView, helper);
        }
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private int distanceToEnd(View targetView, OrientationHelper helper) {
        if (mIsRtl) {
            return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
        }
        return helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding();
    }

    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

            if (firstChild == RecyclerView.NO_POSITION) {
                return null;
            }

            View child = layoutManager.findViewByPosition(firstChild);

            if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                    && helper.getDecoratedEnd(child) > 0) {
                return child;
            } else {
                if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
                        == layoutManager.getItemCount() - 1) {
                    return null;
                } else {
                    return layoutManager.findViewByPosition(firstChild + 1);
                }
            }
        }

        return super.findSnapView(layoutManager);
    }

    private View findEndView(RecyclerView.LayoutManager layoutManager,
                             OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            if (lastChild == RecyclerView.NO_POSITION) {
                return null;
            }

            View child = layoutManager.findViewByPosition(lastChild);

            if (helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2
                    <= helper.getTotalSpace()) {
                return child;
            } else {
                if (((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition()
                        == 0) {
                    return null;
                } else {
                    return layoutManager.findViewByPosition(lastChild - 1);
                }
            }
        }

        return super.findSnapView(layoutManager);
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    private static final float MILLISECONDS_PER_INCH = 150.0f;

    @Nullable
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(mRecyclerView.getLayoutManager(),
                        targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
    }

}
