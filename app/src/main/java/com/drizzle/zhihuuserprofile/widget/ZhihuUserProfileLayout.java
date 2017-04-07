package com.drizzle.zhihuuserprofile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.drizzle.zhihuuserprofile.BuildConfig;
import com.drizzle.zhihuuserprofile.R;

/**
 * Created by drizzle on 2017/4/2.
 */
public class ZhihuUserProfileLayout extends RelativeLayout {

    private static final String TAG = "ZhihuUserProfileLayout";

    private static final int COLLAPSING_VIEW_POSITION = 0, NESTED_SCROLLVIEW_POSITION = 1;

    private static final int FLING_UPWARD = 1, FLING_DOWNWARD = 2;
    private int mFlingDirection;

    private ViewGroup mCollapsingViewGroup, mNestedScrollViewGroup;

    private int mMinSrollY, mMaxScrollY, mCurrentScrollY, mLastScrollY;
    private int mCollapsingOffset;
    private float mTouchDownX, mTouchDownY, mLastTouchY;

    private ViewConfiguration mViewConfiguration;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;

    private OnCollapsingListener mOnCollapsingListener;
    private NestedScrollViewProvider mNestedScrollViewProvider;

    public ZhihuUserProfileLayout(Context context) {
        this(context, null);
    }

    public ZhihuUserProfileLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhihuUserProfileLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZhihuUserProfileLayout, defStyleAttr, 0);
        mCollapsingOffset = array.getDimensionPixelOffset(R.styleable.ZhihuUserProfileLayout_collapsing_offset,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
        array.recycle();
        mMinSrollY = 0;
        mScroller = new Scroller(context);
        mViewConfiguration = ViewConfiguration.get(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            throw new IllegalArgumentException("child count can not be less than 2");
        }
        for (int i = 0; i < getChildCount(); i++) {
            switch (i) {
                case COLLAPSING_VIEW_POSITION:
                    mCollapsingViewGroup = (ViewGroup) getChildAt(COLLAPSING_VIEW_POSITION);
                    measureChildWithMargins(mCollapsingViewGroup, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
                    break;
                case NESTED_SCROLLVIEW_POSITION:
                    mNestedScrollViewGroup = (ViewGroup) getChildAt(NESTED_SCROLLVIEW_POSITION);
                    break;
            }
        }
        mMaxScrollY = mCollapsingViewGroup.getMeasuredHeight() - mCollapsingOffset;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + mMaxScrollY, MeasureSpec.EXACTLY));
    }

    public void setNestedScrollViewProvider(NestedScrollViewProvider nestedScrollViewProvider) {
        mNestedScrollViewProvider = nestedScrollViewProvider;
    }

    public void setOnCollapsingListener(OnCollapsingListener onCollapsingListener) {
        mOnCollapsingListener = onCollapsingListener;
    }

    private boolean isCollapsingViewSnapped() {
        return mCurrentScrollY == mMaxScrollY;
    }

    private boolean isCollapsingViewExpanded() {
        return mCurrentScrollY == mMinSrollY;
    }

    private int getScrollerVelocity() {
        return mScroller == null ? 0 : (int) mScroller.getCurrVelocity();
    }

    /**
     * 判断触摸上下滑动是否为有效操作
     */
    private boolean isTouchScrollVertical(float transX, float transY) {
        return transY > mViewConfiguration.getScaledTouchSlop() && transY > transX;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float currentTouchX = event.getX();
        float currentTouchY = event.getY();
        float transX = Math.abs(currentTouchX - mTouchDownX);
        float transY = Math.abs(currentTouchY - mTouchDownY);
        float dY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                mTouchDownX = currentTouchX;
                mTouchDownY = currentTouchY;
                mLastTouchY = currentTouchY;
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_UP:
                if (isTouchScrollVertical(transX, transY)) {
                    mVelocityTracker.computeCurrentVelocity(1000, mViewConfiguration.getScaledMaximumFlingVelocity());
                    float v = -mVelocityTracker.getYVelocity();
                    if (BuildConfig.DEBUG) Log.d(TAG, "dispatchTouchEvent: YVelocity = " + v);
                    mFlingDirection = v > 0 ? FLING_UPWARD : FLING_DOWNWARD;
                    if (((mFlingDirection == FLING_DOWNWARD && isNestedScrollViewTop() && !isCollapsingViewExpanded())
                            || (mFlingDirection == FLING_DOWNWARD && isCollapsingViewSnapped())
                            || (mFlingDirection == FLING_UPWARD && !isCollapsingViewSnapped()))
                            && Math.abs(v) > mViewConfiguration.getScaledMinimumFlingVelocity()) {
                        mScroller.fling(0, getScrollY(), 0, (int) v, -Integer.MAX_VALUE, Integer.MAX_VALUE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                        mScroller.computeScrollOffset();
                        mLastScrollY = getScrollY();
                        postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                dY = mLastTouchY - currentTouchY;
                boolean isTouchScrollUp = dY > 0;
                if (isTouchScrollVertical(transX, transY)) {
                    if ((isTouchScrollUp && isCollapsingViewSnapped())
                            || (!isTouchScrollUp && isCollapsingViewSnapped() && !isNestedScrollViewTop())) {
                        mNestedScrollViewGroup.requestDisallowInterceptTouchEvent(true);
                    } else {
                        this.scrollBy(0, (int) dY);
                    }
                }
                mLastTouchY = currentTouchY;
                break;
            default:
                break;
        }
        super.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        if (mFlingDirection == FLING_DOWNWARD) {
            if (isNestedScrollViewTop()) {
                int dY = mScroller.getCurrY() - mLastScrollY;
                this.scrollTo(0, getScrollY() + dY);
            }
            postInvalidate();
        } else {
            if (isCollapsingViewSnapped()) {
                mNestedScrollViewProvider.getNestedScrollView().fling(0, getScrollerVelocity());
                mScroller.forceFinished(true);
                return;
            } else {
                this.scrollTo(0, mScroller.getCurrY());
            }
        }
        mLastScrollY = mScroller.getCurrY();
        super.computeScroll();
    }

    @Override
    public void scrollBy(@Px int x, @Px int y) {
        int targetY = getScrollY() + y;
        if (targetY >= mMaxScrollY) {
            targetY = mMaxScrollY;
        } else if (targetY <= 0) {
            targetY = 0;
        }
        super.scrollBy(x, targetY - getScrollY());
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y >= mMaxScrollY) {
            y = mMaxScrollY;
        } else if (y <= 0) {
            y = 0;
        }
        if (mOnCollapsingListener != null) {
            float progress = 1.0f - (float) y / (float) mMaxScrollY;
            if (BuildConfig.DEBUG) Log.d(TAG, "collapsing progress = " + progress);
            mOnCollapsingListener.onCollapsing(progress);
        }
        mCurrentScrollY = y;
        super.scrollTo(x, y);
    }

    private boolean isNestedScrollViewTop() {
        RecyclerView.LayoutManager layoutManager = mNestedScrollViewProvider.getNestedScrollView().getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            View itemView = mNestedScrollViewProvider.getNestedScrollView().getChildAt(0);
            if (itemView == null || (position == 0 && itemView.getTop() == 0)) {
                return true;
            }
        }
        return false;
    }

    public interface OnCollapsingListener {
        void onCollapsing(float progress);
    }

    public interface NestedScrollViewProvider {
        @NonNull
        RecyclerView getNestedScrollView();
    }
}
