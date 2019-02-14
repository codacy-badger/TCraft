package com.tincher.tcraft.feature.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by dks on 2019/1/25.
 */
public class ScrollerViewGroup extends ViewGroup {

    private Scroller mScroller;

    private int mTouchSlop;

    private float mXDown;
    private float mXMove;

    private float mXLastMove;

    private int leftBorder, rightBorder;

    public ScrollerViewGroup(Context context) {
        this(context, null);
    }

    public ScrollerViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int paddingLeft, paddingTop, paddingRight, paddingButtom;
//        l+= getPaddingLeft();
        if (changed) {
            for (int i = 0; i < getChildCount(); i++) {
                View child      = getChildAt(i);
                int  childWidth = getMeasuredWidth();
                //水平布局
                child.layout(l, t, l + childWidth, child.getMeasuredHeight());
                l += childWidth;
            }
            // 初始化左右边界值
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(getChildCount() - 1).getRight();
        }

//        if (changed) {
//            int childCount = getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View childView = getChildAt(i);
//                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
//                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
//            }
//            // 初始化左右边界值
//            leftBorder = getChildAt(0).getLeft();
//            rightBorder = getChildAt(getChildCount() - 1).getRight();
//        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //RawX,RawY 相对于屏幕位置坐标
                //X,Y 相对于容器的位置坐标
                mXDown = ev.getRawX();
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_MOVE:

                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) return true;
                break;
//            case MotionEvent.ACTION_UP:
//                break;
        }

        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int scrolledX = 0;
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE://处理滑动过程中，子View的表现
                mXMove = event.getRawX();
                scrolledX = (int) (mXLastMove - mXMove);
                if (getScrollX() + scrolledX < leftBorder) {

                    //TODO ..
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                break;
            case MotionEvent.ACTION_UP:

                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(getScrollX(), 0, dx, 0);


                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


}
