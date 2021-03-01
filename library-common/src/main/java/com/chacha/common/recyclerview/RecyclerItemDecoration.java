package com.chacha.common.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.recyclerview
 * @date 2/18/21
 * @time 5:48 PM
 *
 * <p>
 *      RecyclerView item间距设置，分割线绘制实现类。RecyclerView使用指南(https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=zh-cn)
 * </p>
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private final int left;

    private final int top;

    private final int right;

    private final int bottom;

    private int dividerColor;

    private int dividerHeight;

    private int dividerMarginHeight;

    private Paint paint;

    public RecyclerItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public RecyclerItemDecoration(int left, int top, int right, int bottom, int dividerColor, int dividerHeight, int dividerMarginHeight) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.dividerColor = dividerColor;
        this.dividerHeight = dividerHeight;
        this.dividerMarginHeight = dividerMarginHeight;

        paint = new Paint();
        paint.setColor(dividerColor);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (paint == null) {
            return;
        }

        // 获取RecyclerView的child view的个数
        int childCount = parent.getChildCount();

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {
            // 获取每个Item
            View child = parent.getChildAt(i);
            final int left = child.getLeft();
            // 需要加上 margin 的高度
            final int top = child.getBottom() + dividerMarginHeight;
            final int right = child.getRight();
            final int bottom = top + dividerHeight;
            // 绘制分割线
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return;
        }

        outRect.set(left, top, right, bottom);
    }
}
