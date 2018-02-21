package dev.paj.towatchtonight.ui.common;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDivider extends RecyclerView.ItemDecoration{
    private Drawable decorator;

    public RecyclerViewItemDivider(Drawable decorator) {
        this.decorator = decorator;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + decorator.getIntrinsicHeight();

            decorator.setBounds(left, top, right, bottom);
            decorator.draw(c);
        }
    }
}
