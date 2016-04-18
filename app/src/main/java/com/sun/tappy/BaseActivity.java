package com.sun.tappy;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sunje on 2016-02-22.
 */
public class BaseActivity extends AppCompatActivity implements IInitializer {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void initialize() {
        initData();
        initView();
        initControl();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
        }
    }

    @Override
    public void initControl() {

    }
}
