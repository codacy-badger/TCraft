package com.dkaishu.tcraft.feature.view_custom;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by dks on 2019/1/23.
 */
public class DragViewGroup extends LinearLayout {
    private ViewDragHelper mHelper;

    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    private int mOriginX, mOriginY;

    public DragViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //第二个参数为灵敏度，默认为1.0f,越大灵敏度越高
        //mTouchSlop，被判定为滑动事件的移动门限，默认为8dp，但注意此构造函数可以传入一个sensitivity参数，
        // 此时实际的mTouchSlop值会除以这个sensitivity参数，因此通过sensitivity参数间接控制对滑动手势的敏感性
        mHelper = ViewDragHelper.create(this, 8f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child != mEdgeTrackerView;
            }


            //可以在以下方法中对child移动的边界进行控制，left , top 分别为即将移动到的位置，return值为边界值
            //要理解View的移动是由你手指的移动驱动的，每划过一个最短识别距离，View再随之移动，每一次的滑动都会回调以下方法，
            // 在触动View下一个移动时，需要确定下一次的终点的位置，即 left 和 top
            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.e("drag","clampViewPositionHorizontal");
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }

            //手指释放时
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild == mAutoBackView) {
                    //使CapturedView移动到指定点，速度由CapturedView的前一个动作计算得出，之前移动越快，当前动作也越快
                    //因为其内部使用的是mScroller.startScroll，所以需要invalidate()以及结合computeScroll方法一起
                    mHelper.settleCapturedViewAt(mOriginX, mOriginY);
                    invalidate();
                }
            }

            //在父View边界拖动时回调,根据setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)..确定哪一侧边缘
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {

                //直接捕获captureChildView，该方法可以绕过tryCaptureView
                //需要调用mDragger.setEdgeTrackingEnabled()才能生效
                mHelper.captureChildView(mEdgeTrackerView, pointerId);
            }

            //如果子View是可点击的（会消耗点击事件），则需要Override以下俩个方法
            //方法的返回值应当是该childView横向或者纵向的移动的范围,只有当以下两个方法返回值大于0时，才能捕获
            //横向，纵向是分离的，可只Override其中一个
            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }


            //当ViewDragHelper状态发生变化时回调（IDLE,DRAGGING,SETTING[自动滚动时]）
            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }

            //当captureView的位置发生改变时回调
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            //当captureView被捕获时回调
            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            //当触摸到边界时回调
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            //返回true锁定edgeFlags对应的边缘. 沿着边缘滑动时会回调，具体机制不详
            @Override
            public boolean onEdgeLock(int edgeFlags) {
                return false;
            }

            //如果需要改变子View的倒序遍历查询顺序则可改写此方法，譬如让重叠的下层View先于上层View被捕获
            @Override
            public int getOrderedChildIndex(int index) {
                return super.getOrderedChildIndex(index);
            }
        });

        //通过或操作多重组合
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT | ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mOriginX = mAutoBackView.getLeft();
        mOriginY = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
