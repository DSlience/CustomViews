package com.adealink.baselib.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Grid-RecyclerView设置间隔条
 * Created by Xuefu_Du on 2018/6/19.
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;//Grid RecyclerView的列数
    private int gapWidth;//间隔条宽（px）
    private boolean includeEdge;

    public GridSpaceItemDecoration(int spanCount, int gapWidth) {
        this(spanCount, gapWidth, false);
    }

    public GridSpaceItemDecoration(int spanCount, int gapWidth, boolean includeEdge) {
        this.spanCount = spanCount;
        this.gapWidth = gapWidth;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position < 0) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
            return;
        }

        int column = position % spanCount; // item column
        if (includeEdge) {
            outRect.left = gapWidth - column * gapWidth / spanCount; // gapWidth - column * ((1f / spanCount) * gapWidth)
            outRect.right = (column + 1) * gapWidth / spanCount; // (column + 1) * ((1f / spanCount) * gapWidth)

            if (position < spanCount) { // top edge
                outRect.top = gapWidth;
            }
            outRect.bottom = gapWidth; // item bottom
        } else {
            outRect.left = column * gapWidth / spanCount; // column * ((1f / spanCount) * gapWidth)
            outRect.right = gapWidth - (column + 1) * gapWidth / spanCount; // gapWidth - (column + 1) * ((1f /    spanCount) * gapWidth)
            if (position >= spanCount) {
                outRect.top = gapWidth; // item top
            }
        }
    }
}
