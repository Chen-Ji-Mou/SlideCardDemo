package com.chenjimou.slidecarddemo;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SlideCallback extends ItemTouchHelper.SimpleCallback {

    private final MainActivity.MyAdapter adapter;
    private final List<String> data;

    public SlideCallback(List<String> data, MainActivity.MyAdapter adapter) {
        // @param dragDirs 二进制或方向标志，在其中可以拖动视图。
        // 必须由{@link#LEFT}、{@link#RIGHT}、{@link#START}、{@link#END}、{@link#UP}和{@link#DOWN}组成。
        // @param swipeDirs 二进制或方向标志，在其中可以滑动视图。
        // 必须由{@link#LEFT}、{@link#RIGHT}、{@link#START}、{@link#END}、{@link#UP}和{@link#DOWN}组成。
        // 这里是不使用拖动所以 dragDirs 设为 0，swipeDirs 设为 15 表示可以360°滑动
        super(0, 15);
        this.data = data;
        this.adapter = adapter;
    }

    // item 被拖动的回调
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    // item 被滑动的回调
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 删除数据中的最后一个
        String removeUrl = data.remove(viewHolder.getLayoutPosition());
        // 将数据中的最后一个再添加到数据中第一个
        data.add(0, removeUrl);
        adapter.notifyDataSetChanged();
    }

    // 绘制滑动的动画效果
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        double maxDistance = recyclerView.getWidth() * 0.5f;
        double distance = Math.sqrt(dX * dX + dY * dY);
        double fraction = distance / maxDistance;

        if (fraction > 1) {
            fraction = 1;
        }

        // 显示的个数  4个
        int itemCount = recyclerView.getChildCount();

        for (int i = 0; i < itemCount; i++) {
            View view = recyclerView.getChildAt(i);

            int level = itemCount - i - 1;

            if (level > 0) {
                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP));
                    view.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                    view.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                }
            }
        }
    }
}
