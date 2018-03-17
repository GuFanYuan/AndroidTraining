package com.example.gufan.myscrollview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by gufan on 2018/3/17.
 */

public class MyScrollView extends ViewGroup {

    private static final String TAG = "MyScrollView";


    private int mChildWidth;
    private int mChilder;
    private int mChildIndex;

    private int mLastXIntercept;//上次滑动到的横坐标
    private int mLastYIntercept;//上次滑动到的纵坐标

    private int mLastX;
    private int mLastY;

    private VelocityTracker mVelocityTracker;//速度计算
    private Scroller mScroller;//弹性滑动

    public MyScrollView(Context context) {
        super(context);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if (mScroller == null){
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        final int ChildCount = getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpaceMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpaceMode = MeasureSpec.getMode(heightMeasureSpec);

        if (ChildCount == 0){
            setMeasuredDimension(0,0);
        }else if (widthSpaceMode == MeasureSpec.AT_MOST && heightMeasureSpec==MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * ChildCount;
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measureWidth,measureHeight);
        }else if (heightSpaceMode == MeasureSpec.AT_MOST ){
            final View childView = getChildAt(0);
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize,measureHeight);
        }else if (widthSpaceMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth();
            setMeasuredDimension(measureWidth,heightSpaceSize);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int ChildCount = getChildCount();
        mChilder = ChildCount;

        for (int i = 0; i < mChilder; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)){
                    intercepted = true;
                }else{
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        Log.e(TAG,"intercepted: "+intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = x - mLastY;
                scrollBy(-deltaX,0);//外部拦截法，如果外部拦截就由Parent View滑动，parent view是左右滑动的
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity)>50){
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1: mChildIndex + 1;
                }else{
                    mChildIndex = (scrollX + mChildWidth / 2)/mChildWidth;
                }
                mChildIndex = Math.max(0,Math.min(mChildIndex,mChilder-1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx,int dy){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
