package com.sun.tappy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.TextView;

/**
 * Created by sunje on 2016-02-22.
 */
public class ActionTouchView extends TextView {
    private final int TOUCH_STATE_DRAG = 1;
    private final int TOUCH_STATE_IDLE = 2;
    private final int TOUCH_STATE_DRAG_OVER = 0;
    private final int TOUCH_STATE_FLING = 3;
    private final int LIMIT_SLOPE = 200;
    private float mFirstTouchedX;
    private float mFirstTouchedY;
    private float mPrevTouchedX;
    private float mPrevTouchedY;
    private int mTouchState = TOUCH_STATE_IDLE;
    private boolean isDragging = false;
    private OnTouchStateListener mOnTouchStateListener;

    private VelocityTracker velocityTracker;

    public ActionTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setOnTouchStateListener(OnTouchStateListener l) {
        mOnTouchStateListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = false;
                mTouchState = TOUCH_STATE_IDLE;
                mPrevTouchedX = mFirstTouchedX = event.getRawX();
                mPrevTouchedY = mFirstTouchedY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                isDragging = isDragging(event);
                if (isDragging && mTouchState != TOUCH_STATE_DRAG_OVER) {
                    Log.d("sdklfjsdlfjsld", "skdfjsldjflsdf");
                    mTouchState = TOUCH_STATE_DRAG_OVER;
                    mOnTouchStateListener.onDragStart(this);
                    return true;
                }
                velocityTracker.computeCurrentVelocity(1);
                float velocityX = velocityTracker.getXVelocity();
                float velocityY = velocityTracker.getYVelocity();
//                Log.d("hatti.view.velocity", "x : " + velocityX + " | y : " + velocityY);
                Log.d("hatti.view.velocity", "velocity : " + Math.max(Math.abs(velocityX), Math.abs(velocityY)));

                if (mTouchState != TOUCH_STATE_DRAG_OVER && (mTouchState == TOUCH_STATE_FLING || Math.max(Math.abs(velocityX), Math.abs(velocityY)) > 2f)) {
                    Log.d("hatti.view.touch.state", "fling");
                    mTouchState = TOUCH_STATE_FLING;
                    return true;
                }
                if (mTouchState == TOUCH_STATE_IDLE) {
                    Log.d("hatti.view.touch.state", "dragging");
                    int dist = (int) Math.max((Math.abs(mFirstTouchedY - event.getRawY())), Math.abs(mFirstTouchedX - event.getRawX()));
                    dist = dist > LIMIT_SLOPE ? LIMIT_SLOPE : dist;
                    mOnTouchStateListener.onDragging(this, dist / (float) LIMIT_SLOPE);
                }
                break;
            case MotionEvent.ACTION_UP:
                mOnTouchStateListener.onDragEnd(this);
                if (velocityTracker != null) {
                    velocityTracker.clear();
                    velocityTracker = null;
                }
                if (mTouchState == TOUCH_STATE_FLING) {
                    mOnTouchStateListener.onDragStart(this);
                    return true;
                }
                if (mTouchState == TOUCH_STATE_DRAG_OVER) {
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isDragging(MotionEvent currentEvent) {
        return isDragging || (Math.abs(mFirstTouchedY - currentEvent.getRawY()) > LIMIT_SLOPE || Math.abs(mFirstTouchedX - currentEvent.getRawX()) > LIMIT_SLOPE);
    }
}
