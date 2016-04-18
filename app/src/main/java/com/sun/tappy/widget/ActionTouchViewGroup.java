package com.sun.tappy.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

import com.sun.tappy.R;

/**
 * Created by sunje on 2016-02-22.
 */
public class ActionTouchViewGroup extends RelativeLayout {
    private final int TOUCH_STATE_DRAG = 1;
    private final int TOUCH_STATE_IDLE = 2;
    private final int TOUCH_STATE_DRAG_OVER = 0;
    private final int TOUCH_STATE_FLING = 3;
    private final int LIMIT_SLOPE = 200;
    private final GestureDetector mGd;
    private float mFirstTouchedX;
    private float mFirstTouchedY;
    private float mPrevTouchedX;
    private float mPrevTouchedY;
    private int mTouchState = TOUCH_STATE_IDLE;
    private boolean isDragging = false;
    private OnTouchStateListener mOnTouchStateListener;
    private VelocityTracker velocityTracker;
    public static boolean isPressing = false;

    GestureDetector.SimpleOnGestureListener simpleGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public void onLongPress(MotionEvent event) {
            Log.i("hatti.view.gesture", "long press");
            isPressing = true;
            View btn1 = findViewById(R.id.btn1);
            View btn2 = findViewById(R.id.btn2);
            View btn3 = findViewById(R.id.btn3);
            final boolean isInsideBtn1 = event.getRawX() > 0 && event.getRawX() < btn1.getWidth() && event.getRawY() > btn1.getTop() && event.getRawY() < btn1.getBottom();
            final boolean isInsideBtn2 = btn2.getVisibility() != View.GONE && event.getRawX() > btn1.getWidth() && event.getRawX() < btn1.getWidth() + btn2.getWidth() &&
                    event.getY() > btn2.getTop() && event.getY() < btn2.getBottom();
            boolean isInsideBtn3 = event.getRawX() > btn1.getWidth() && event.getRawX() < btn1.getWidth() + btn3.getWidth() &&
                    event.getY() > btn3.getTop() && event.getY() < btn3.getBottom();

            if (isInsideBtn2 || isInsideBtn3) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int cnt = 0;
                                while (isPressing) {
                                    try {
                                        cnt++;
                                        mOnTouchStateListener.onLongPress(isInsideBtn2 ? -1 : 1);
                                        if (cnt < 5) {
                                            Thread.sleep(400);
                                        } else if (cnt < 10) {
                                            Thread.sleep(300);
                                        } else if (cnt < 20) {
                                            Thread.sleep(150);
                                        } else {
                                            Thread.sleep(80);
                                        }

                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }).start();

                    }
                });
            }
            super.onLongPress(event);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("hatti.view.gesture", "single tap up");
            return super.onSingleTapUp(e);
        }
    };

    public ActionTouchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGd = new GestureDetector(getContext(), simpleGestureListener);
    }

    public void setOnTouchStateListener(OnTouchStateListener l) {
        mOnTouchStateListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGd.onTouchEvent(event);
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
                return true;
            case MotionEvent.ACTION_MOVE:
                isDragging = isDragging(event);
                if (!isPressing && isDragging && mTouchState != TOUCH_STATE_DRAG_OVER) {
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

                if (!isPressing && mTouchState != TOUCH_STATE_DRAG_OVER && (mTouchState == TOUCH_STATE_FLING || Math.max(Math.abs(velocityX), Math.abs(velocityY)) > 2f)) {
                    mTouchState = TOUCH_STATE_FLING;
                    return true;
                }
                if (mTouchState == TOUCH_STATE_IDLE) {
                    int dist = (int) Math.max((Math.abs(mFirstTouchedY - event.getRawY())), Math.abs(mFirstTouchedX - event.getRawX()));
                    dist = dist > LIMIT_SLOPE ? LIMIT_SLOPE : dist;
                    mOnTouchStateListener.onDragging(this, dist / (float) LIMIT_SLOPE);
                }
                return true;
            case MotionEvent.ACTION_UP:
                Log.i("gesture", "UP");
                Log.d("hatti.view.touch.state", mTouchState + "");
                if (velocityTracker != null) {
                    velocityTracker.clear();
                    velocityTracker = null;
                }
                if (!isPressing && mTouchState == TOUCH_STATE_FLING) {
                    mOnTouchStateListener.onDragStart(this);
                    return true;
                }
                if (!isPressing && mTouchState == TOUCH_STATE_IDLE) {
                    mOnTouchStateListener.onTapping(this, event);
                    return true;
                }
                isPressing = false;
                if (mTouchState == TOUCH_STATE_DRAG_OVER) {
                    return true;
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isDragging(MotionEvent currentEvent) {
        return isDragging || (Math.abs(mFirstTouchedY - currentEvent.getRawY()) > LIMIT_SLOPE || Math.abs(mFirstTouchedX - currentEvent.getRawX()) > LIMIT_SLOPE);
    }
}
