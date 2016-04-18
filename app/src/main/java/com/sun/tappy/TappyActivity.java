package com.sun.tappy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sun.tappy.widget.ActionTouchView;
import com.sun.tappy.widget.ActionTouchViewGroup;
import com.sun.tappy.widget.OnTouchStateListener;

public class TappyActivity extends BaseActivity implements View.OnClickListener, OnTouchStateListener, View.OnTouchListener {

    private TextView txtCounter;
    private int mCount = 0;
    private ActionTouchViewGroup container;
    private TextView btn1;
    private TextView btn2;
    private boolean isResetConfig;
    private boolean isCountConfig;
    private TextView btn3;
    private View div2;
    private boolean ismodeReverse;
    private boolean mIsLocked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tappy);

        initialize();
    }

    @Override
    public void initView() {
        super.initView();

        txtCounter = (ActionTouchView) findViewById(R.id.txt_cnt);
        container = (ActionTouchViewGroup) findViewById(R.id.container);

        btn1 = (TextView) findViewById(R.id.btn1);
        btn2 = (TextView) findViewById(R.id.btn2);
        btn3 = (TextView) findViewById(R.id.btn3);
        div2 = findViewById(R.id.div2);
        txtCounter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void initControl() {
        super.initControl();
        txtCounter.setOnClickListener(this);
//        btn1.setOnClickListener(this);
//        btn2.setOnClickListener(this);
        ((ActionTouchView) txtCounter).setOnTouchStateListener(this);
        container.setOnTouchStateListener(this);
        btn1.setOnTouchListener(this);
        btn2.setOnTouchListener(this);
        btn3.setOnTouchListener(this);
    }

    private void switchView() {
        if (txtCounter.getVisibility() == View.VISIBLE) {
            txtCounter.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.GONE);
            div2.setVisibility(View.GONE);
            Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            anim.setDuration(350);
            container.startAnimation(anim);
            container.setVisibility(View.VISIBLE);
        } else {
            Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            anim.setDuration(350);
            txtCounter.startAnimation(anim);
            txtCounter.setVisibility(View.VISIBLE);
            container.setVisibility(View.INVISIBLE);
            isCountConfig = false;
            isResetConfig = false;
            btn1.setText("Reset");
            btn3.setText("To Zero");
            txtCounter.setText(String.valueOf(mCount));
            txtCounter.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.txt_cnt) {
            if (mIsLocked) {
                return;
            }
            if (ismodeReverse) {
                if (mCount > 0) {
                    txtCounter.setText(String.valueOf(--mCount));
                } else {
                    txtCounter.setText(String.valueOf(++mCount));
                }
            } else {
                txtCounter.setText(++mCount + "");
            }
            if (mCount == 0) {
                mIsLocked = true;
                ismodeReverse = false;
                txtCounter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                txtCounter.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (id == R.id.btn1) {
            if (isResetConfig) {
                container.setVisibility(View.INVISIBLE);
                txtCounter.setVisibility(View.VISIBLE);
                isResetConfig = false;
                txtCounter.setText(String.valueOf(mCount = 0));
                btn1.setText("Reset");
                btn3.setText("To Zero");
            } else if (isCountConfig) {
//                isCountConfig = false;
            } else {
                isResetConfig = true;
                btn1.setText("Yes");
                Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                anim.setDuration(1350);
                btn1.startAnimation(anim);
                btn3.setText("No");
            }
        } else if (id == R.id.btn2) {
            btn1.setText(String.valueOf(--mCount));
        } else if (id == R.id.btn3) {
            if (isCountConfig) {
                btn1.setText(String.valueOf(++mCount));
                isResetConfig = false;
            } else if (isResetConfig) {
                isResetConfig = false;
                txtCounter.setText(String.valueOf(mCount));
                container.setVisibility(View.INVISIBLE);
                txtCounter.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                anim.setDuration(250);
                txtCounter.startAnimation(anim);
                Animation anim2 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                anim.setDuration(250);
                container.startAnimation(anim2);
                btn1.setText("Reset");
                btn3.setText("To Zero");
            } else {
                isCountConfig = true;
                btn2.setVisibility(View.VISIBLE);
                div2.setVisibility(View.VISIBLE);
                btn1.setText(String.valueOf(mCount));
                btn2.setText("-");
                btn3.setText("+");
            }
        }
    }

    @Override
    public void onDragStart(View view) {
        switchView();
    }

    @Override
    public void onDragEnd(View view) {
        txtCounter.setTextColor(Color.argb(255, 255, 255, 255));
    }

    @Override
    public void onFling(View view) {

    }

    @Override
    public void onDragging(View view, float offset) {
//        if (mIsLocked) {
//            return;
//        }
        float ap = 255 * offset;
        Log.d("apapapap", ap + "");
        if (view instanceof ActionTouchView) {
            int argb = Color.argb(255 - (int) ap, 255, 255, 255);
            txtCounter.setTextColor(argb);
        } else if (view instanceof ActionTouchViewGroup) {
            int argb = Color.argb(255 - (int) ap, 144, 202, 249);
            Log.d("asdfasdf", argb + "");
            container.setBackgroundColor(argb);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public void onTapping(View view, MotionEvent event) {
        final int id = view.getId();
//        Utils.dp2px(this, 56)
        boolean isInsideBtn1 = event.getRawX() > 0 && event.getRawX() < btn1.getWidth() && event.getRawY() > btn1.getTop() && event.getRawY() < btn1.getBottom();
        boolean isInsideBtn2 = btn2.getVisibility() != View.GONE && event.getRawX() > btn1.getWidth() && event.getRawX() < btn1.getWidth() + btn2.getWidth() &&
                event.getY() > btn2.getTop() && event.getY() < btn2.getBottom();
        boolean isInsideBtn3 = event.getRawX() > btn1.getWidth() && event.getRawX() < btn1.getWidth() + btn3.getWidth() &&
                event.getY() > btn3.getTop() && event.getY() < btn3.getBottom();
        Log.d("hasdfasdfasjdf", "y : " + event.getY());
        Log.d("hasdfasdfasjdf", "btn2.getBottom : " + btn2.getBottom() + " | div2.gettop : " + div2.getTop());
        Log.d("hasdfasdfasjdf", "btn3.getTop: " + btn3.getTop() + " | btn3.getBottom: " + btn3.getBottom());
        if (isInsideBtn1) {
            if (isResetConfig) {
                container.setVisibility(View.INVISIBLE);
                txtCounter.setVisibility(View.VISIBLE);
                isResetConfig = false;
                txtCounter.setText(String.valueOf(mCount = 0));
                btn1.setText("Reset");
                btn3.setText("To Zero");
                Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                anim.setDuration(350);
                txtCounter.startAnimation(anim);
            } else if (isCountConfig) {
//                isCountConfig = false;
            } else {
                isResetConfig = true;
                btn1.setText("Yes");
                btn3.setText("No");
            }
        } else if (isInsideBtn2) {
            btn1.setText(String.valueOf(--mCount));
            mIsLocked = mCount == 0 ? true : false;
            ismodeReverse = true;
        } else if (isInsideBtn3) {
            if (isCountConfig) {
                ismodeReverse = true;
                btn1.setText(String.valueOf(++mCount));
                isResetConfig = false;
                mIsLocked = mCount == 0 ? true : false;
            } else if (isResetConfig) {
                isResetConfig = false;
                txtCounter.setText(String.valueOf(mCount));
                txtCounter.setVisibility(View.VISIBLE);
                container.setVisibility(View.INVISIBLE);
                Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                anim.setDuration(350);
                txtCounter.startAnimation(anim);
                btn1.setText("Reset");
                btn3.setText("To Zero");
            } else {
                isCountConfig = true;
                btn2.setVisibility(View.VISIBLE);
                div2.setVisibility(View.VISIBLE);
                btn1.setText(String.valueOf(mCount));
                btn2.setText("-");
                btn3.setText("+");
            }
        }
    }

    @Override
    public void onLongPress(int cnt) {
        mCount += cnt;
        Log.i("gesture", mCount + "");
        btn1.post(new Runnable() {
            @Override
            public void run() {
                btn1.setText(String.valueOf(mCount));
            }
        });

    }
}
