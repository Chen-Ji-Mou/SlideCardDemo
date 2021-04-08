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

    // item 被滑动后的回调
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 删除 adapter 数据集中的最后一个
        String removeUrl = data.remove(viewHolder.getLayoutPosition());
        // 将删除的数据再添加到 adapter 数据集中的第一个
        data.add(0, removeUrl);
        adapter.notifyDataSetChanged();
    }

    // 绘制滑动的动画效果
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        // 判断 item 是否属于滑出屏幕的最大距离
        double maxDistance = recyclerView.getWidth() * 0.5f;
        // item 移动的距离
        double distance = Math.sqrt(dX * dX + dY * dY);
        // item 移动距离与临界值的比率。如果比率大于等于1，则表示 item 滑出屏幕
        double fraction = distance / maxDistance;
        if (fraction > 1) {
            fraction = 1;
        }
        // 屏幕中 item 的个数
        int itemCount = recyclerView.getChildCount();
        // 遍历，从被压在最下面 item 开始设置
        for (int i = 0; i < itemCount; i++) {
            View view = recyclerView.getChildAt(i);
            int zoomDegree = itemCount - i - 1;
            // 如果 zoomDegree 等于0就代表不进行缩放
            if (zoomDegree != 0) {
                // 如果遍历到的不是被压在最下面的 View
                if (zoomDegree != CardConfig.MAX_SHOW_COUNT - 1) {
                    view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * zoomDegree - fraction * CardConfig.TRANS_Y_GAP));
                    view.setScaleX((float) (1 - CardConfig.SCALE_GAP * zoomDegree + fraction * CardConfig.SCALE_GAP));
                    view.setScaleY((float) (1 - CardConfig.SCALE_GAP * zoomDegree + fraction * CardConfig.SCALE_GAP));
                }
            }
        }
    }
}
