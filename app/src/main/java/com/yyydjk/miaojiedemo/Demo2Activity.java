package com.yyydjk.miaojiedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class Demo2Activity extends AppCompatActivity {
    private int item_normal_height;
    private int item_max_height;
    private float item_normal_alpha;
    private float item_text_transitionY;

    private List<Integer> walls = Arrays.asList(R.mipmap.image_01,
            R.mipmap.image_02, R.mipmap.image_03, R.mipmap.image_04, R.mipmap.image_05,
            R.mipmap.image_06, R.mipmap.image_07, R.mipmap.image_08, R.mipmap.image_09,
            R.mipmap.image_10, R.mipmap.image_01,
            R.mipmap.image_02, R.mipmap.image_03, R.mipmap.image_04, R.mipmap.image_05,
            R.mipmap.image_06, R.mipmap.image_07, R.mipmap.image_08, R.mipmap.image_09,
            R.mipmap.image_10);
    private Animator bottomAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item_max_height = getResources().getDimensionPixelOffset(R.dimen.item_max_height);
        item_normal_height = getResources().getDimensionPixelOffset(R.dimen.item_normal_height);
        item_normal_alpha = 1;
        item_text_transitionY = getResources().getDimension(R.dimen.item_text_transitionY);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        DemoAdapter demoAdapter = new DemoAdapter(this, walls);
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(demoAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (bottomAnimator != null && bottomAnimator.isRunning()) {
                    return;
                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                RecyclerView.ViewHolder firstViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstVisibleItemPosition());
                RecyclerView.ViewHolder secondViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                RecyclerView.ViewHolder threeViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition() + 1);
                RecyclerView.ViewHolder lastViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findLastVisibleItemPosition());
                if (firstViewHolder != null && firstViewHolder instanceof DemoAdapter.ViewHolder) {
                    DemoAdapter.ViewHolder viewHolder = (DemoAdapter.ViewHolder) firstViewHolder;
                    if (viewHolder.itemView.getLayoutParams().height - dy <= item_max_height
                            && viewHolder.itemView.getLayoutParams().height - dy >= item_normal_height) {
                        viewHolder.itemView.getLayoutParams().height = viewHolder.itemView.getLayoutParams().height - dy;
                        viewHolder.textLayout.setAlpha(viewHolder.textLayout.getAlpha() - dy * 1.0f / item_normal_height);
                        viewHolder.textLayout.setTranslationY(viewHolder.textLayout.getTranslationY() - dy * item_text_transitionY / item_normal_height);
                    }
                }
                if (secondViewHolder != null && secondViewHolder instanceof DemoAdapter.ViewHolder) {
                    DemoAdapter.ViewHolder viewHolder = (DemoAdapter.ViewHolder) secondViewHolder;
                    if (viewHolder.itemView.getLayoutParams().height + dy <= item_max_height
                            && viewHolder.itemView.getLayoutParams().height + dy >= item_normal_height) {
                        viewHolder.itemView.getLayoutParams().height = viewHolder.itemView.getLayoutParams().height + dy;
                        viewHolder.textLayout.setAlpha(item_normal_alpha);
                        viewHolder.textLayout.setTranslationY(viewHolder.textLayout.getTranslationY() + dy * item_text_transitionY / item_normal_height);
                    }
                }

                if (threeViewHolder != null && threeViewHolder instanceof DemoAdapter.ViewHolder) {
                    DemoAdapter.ViewHolder viewHolder = (DemoAdapter.ViewHolder) threeViewHolder;
                    viewHolder.textLayout.setAlpha(item_normal_alpha);
                    viewHolder.textLayout.setTranslationY(0);
                    viewHolder.itemView.getLayoutParams().height = item_normal_height;
                }
                if (lastViewHolder != null && lastViewHolder instanceof DemoAdapter.ViewHolder) {
                    DemoAdapter.ViewHolder viewHolder = (DemoAdapter.ViewHolder) lastViewHolder;
                    viewHolder.textLayout.setAlpha(item_normal_alpha);
                    viewHolder.textLayout.setTranslationY(0);
                    viewHolder.itemView.getLayoutParams().height = item_normal_height;
                }
                recyclerView.requestLayout();
            }

//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && !recyclerView.canScrollVertically(1)) {
//                    if (bottomAnimator != null && bottomAnimator.isRunning()) {
//                        bottomAnimator.cancel();
//                    }
//                    bottomAnimator = createBottomAnimator(recyclerView);
//                    bottomAnimator.start();
//                }
//            }
        });
    }

    public Animator createBottomAnimator(final RecyclerView recyclerView) {
        final View lastOneItemView =
                recyclerView.findViewHolderForAdapterPosition(recyclerView.getAdapter().getItemCount() - 1).itemView;
        final View lastTwoItemView =
                recyclerView.findViewHolderForAdapterPosition(recyclerView.getAdapter().getItemCount() - 2).itemView;
        final View lastThreeItemView =
                recyclerView.findViewHolderForAdapterPosition(recyclerView.getAdapter().getItemCount() - 3).itemView;
        final View lastFourItemView =
                recyclerView.findViewHolderForAdapterPosition(recyclerView.getAdapter().getItemCount() - 4).itemView;

        final int lastItemHeight = (int) ((recyclerView.getHeight() - item_max_height) / 3.0f);
        final int lastItemMaxHeight = recyclerView.getHeight() - lastItemHeight * 3;
        final int lastItemNormalHeight = lastOneItemView.getLayoutParams().height;
        final int lastFourItemHeight = lastFourItemView.getLayoutParams().height;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int lastFourItemCurrentHeight = (int)
                        (lastFourItemHeight + (lastItemMaxHeight - lastFourItemHeight) * value);
                int lastNormalItemCurrentHeight = (int)
                        (lastItemNormalHeight + (lastItemHeight - lastItemNormalHeight) * value);
                lastOneItemView.getLayoutParams().height = lastNormalItemCurrentHeight;
                lastTwoItemView.getLayoutParams().height = lastNormalItemCurrentHeight;
                lastThreeItemView.getLayoutParams().height = lastNormalItemCurrentHeight;
                lastFourItemView.getLayoutParams().height = lastFourItemCurrentHeight;
                recyclerView.scrollBy(0, lastOneItemView.getBottom() - recyclerView.getBottom());
                recyclerView.requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lastOneItemView.getLayoutParams().height = lastItemHeight;
                lastTwoItemView.getLayoutParams().height = lastItemHeight;
                lastThreeItemView.getLayoutParams().height = lastItemHeight;
                lastFourItemView.getLayoutParams().height = lastItemMaxHeight;
                recyclerView.scrollBy(0, lastOneItemView.getBottom() - recyclerView.getBottom());
                recyclerView.requestLayout();
            }
        });
        animator.setDuration(240);
        return animator;
    }
}
