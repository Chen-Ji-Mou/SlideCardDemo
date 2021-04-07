package com.chenjimou.slidecarddemo;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class SlideCardLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // ViewHolder 回收
        detachAndScrapAttachedViews(recycler);
        // 被压在最下面的 View 的 position
        int bottomPosition;
        int itemCount = getItemCount();
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = 0;
        } else {
            // 屏幕上永远只显示四个 item
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }
        for (int i = bottomPosition; i < itemCount; i++) {
            // ViewHolder 复用
            View view = recycler.getViewForPosition(i);
            // 添加 View 到布局中
            addView(view);
            // 测量 View 的大小
            measureChildWithMargins(view, 0, 0);
            // 水平方向上剩余的大小
            int surplusHorizontal = getWidth() - getDecoratedMeasuredWidth(view);
            // 垂直方向上剩余的大小
            int surplusVertical = getHeight() - getDecoratedMeasuredHeight(view);
            // 进行布局
            layoutDecoratedWithMargins(view, surplusHorizontal / 2, surplusVertical / 2,
                    surplusHorizontal / 2 + getDecoratedMeasuredWidth(view),
                    surplusVertical / 2 + getDecoratedMeasuredHeight(view));
            // 设置 View 大小的缩放，使 View 叠加得有层次感
            int zoomDegree = itemCount - i - 1;
            if (zoomDegree > 0){
                if (zoomDegree < CardConfig.MAX_SHOW_COUNT - 1) {
                    // 设置 View 在水平方向的偏移量，以像素为单位。会引发 View 重绘
                    // 偏移量为正数时，表示 View 向下平移；反之表示向上平移
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * zoomDegree);
                    // 设置 View 在水平方向的缩放比例。会引发 View 重绘
                    view.setScaleX(1 - CardConfig.SCALE_GAP * zoomDegree);
                    // 设置 View 在垂直方向的缩放比例。会引发 View 重绘
                    view.setScaleY(1 - CardConfig.SCALE_GAP * zoomDegree);
                } else {
                    // 被压在最下面的 View 与它的前一个 View 的比例相同
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * (zoomDegree - 1));
                    view.setScaleX(1 - CardConfig.SCALE_GAP * (zoomDegree - 1));
                    view.setScaleY(1 - CardConfig.SCALE_GAP * (zoomDegree - 1));
                }
            }
        }
    }
}
