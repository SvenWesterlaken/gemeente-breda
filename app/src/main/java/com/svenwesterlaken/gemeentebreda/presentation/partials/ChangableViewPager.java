package com.svenwesterlaken.gemeentebreda.presentation.partials;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Sven Westerlaken on 2-6-2017.
 */

public class ChangableViewPager extends ViewPager {
    private GestureDetector mGestureDetector;
    boolean swipeable = false;

    public ChangableViewPager(Context context) {
        super(context);
        setMyScroller();
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
        setFadingEdgeLength(0);
    }

    public ChangableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return swipeable && super.onInterceptTouchEvent(event) && mGestureDetector.onTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeable && super.onTouchEvent(event);

    }

    public void enableSwiping() {
        swipeable = true;
    }

    public boolean isSwipeable() {
        return this.swipeable;
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyScroller extends Scroller {
        MyScroller(Context context) {
            super(context, new AccelerateDecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350);
        }
    }

    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) < Math.abs(distanceX);
        }
    }
}
