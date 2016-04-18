package com.sun.tappy.widget;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sunje on 2016-02-22.
 */
public interface OnTouchStateListener {
    void onDragStart(View view);
    void onDragEnd(View view);
    void onFling(View view);
    void onDragging(View view, float offset);
    void onTapping(View view, MotionEvent event);
    void onLongPress(int cnt);
}
