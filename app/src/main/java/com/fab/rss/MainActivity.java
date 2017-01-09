package com.fab.rss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    KLog.i(TAG, "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                KLog.i(TAG, "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
