package com.yyydjk.miaojiedemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by dongjunkun on 2015/12/1.
 */
public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> {
    private boolean isFirst = true;
    private List<Integer> walls;

    private int item_normal_height;
    private int item_max_height;
    private float item_text_transitionY;

    private LinearLayoutManager linearLayoutManager;

    public DemoAdapter(Context context, List<Integer> walls) {
        this.walls = walls;
        item_max_height = context.getResources().getDimensionPixelOffset(R.dimen.item_max_height);
        item_normal_height = context.getResources().getDimensionPixelOffset(R.dimen.item_normal_height);
        item_text_transitionY = context.getResources().getDimension(R.dimen.item_text_transitionY);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView != null && (recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        linearLayoutManager = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (linearLayoutManager != null) {
            Log.i("DemoAdapter", "bind position = " + position + ", first visible = " + linearLayoutManager.findFirstVisibleItemPosition());
        }
        if (isFirst && position == 0) {
            isFirst = false;
            holder.textLayout.setAlpha(1);
            holder.textLayout.setTranslationY(item_text_transitionY);
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_max_height));
        } else {
            holder.textLayout.setAlpha(1);
            holder.textLayout.setTranslationY(0);
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_normal_height));
        }
        holder.imageView.setImageResource(walls.get(position));
    }

    @Override
    public int getItemCount() {
        return walls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View textLayout;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textLayout = itemView.findViewById(R.id.text_layout);
        }
    }
}
