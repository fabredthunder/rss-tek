package com.fab.rss.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import com.fab.rss.R;
import com.fab.rss.RSSApp;
import com.fab.rss.items.FeedItem;
import com.fab.rss.utils.UtilsFunctions;
import com.fab.rss.utils.bundles.FeedBundle;
import com.fab.rss.utils.models.RSSResponse;
import com.fab.rss.utils.services.BaseApiService;
import com.fab.rss.utils.services.IApiService;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/9/17
 */
@SuppressWarnings("UnusedDeclaration")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.cv_add)
    CardView mCvAdd;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_url)
    EditText mEtUrl;
    @BindView(R.id.et_comment)
    EditText mEtComment;

    FastItemAdapter<FeedItem> mFastAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    KLog.i("Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                KLog.i("openRatio = " + openRatio + ", offsetPixels = " + offsetPixels);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFastAdapter = new FastItemAdapter<>();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFastAdapter);

        mFastAdapter.add(new FeedItem());
        mFastAdapter.withSavedInstanceState(savedInstanceState);
    }

    @OnClick(R.id.fab)
    public void onClickFab() {

        if (mCvAdd.getVisibility() == View.VISIBLE) {
            animateRevealClose();
            clearInputs();
        } else
            animateRevealShow();

    }

    @OnClick(R.id.btn_go)
    public void onClickValidate() {
        final String title = mEtTitle.getText().toString().trim();
        final String url = mEtUrl.getText().toString().trim();
        final String comment = mEtComment.getText().toString().trim();

        if (UtilsFunctions.isNetworkAvailable()) {

            if (!title.equals("") && !url.equals("") && !comment.equals("")) {

                final BaseApiService baseService = new BaseApiService();
                IApiService service = baseService.create();
                Call<RSSResponse> call = service.addRSS(RSSApp.getToken(), new FeedBundle(title, url, comment));

                call.enqueue(new Callback<RSSResponse>() {
                    @Override
                    public void onResponse(Call<RSSResponse> call, Response<RSSResponse> response) {

                        if (response.code() == 200) {
                            onClickFab();
                            KLog.json(response.body().toString());
                            Toasty.success(MainActivity.this, "Successfully added to your feeds").show();
                        } else if (response.code() == 503) {
                            Toasty.warning(MainActivity.this, "Server timeout. Trying again ...").show();
                            onClickValidate();
                        } else
                            Toasty.warning(MainActivity.this, "Server error. Try again").show();

                    }

                    @Override
                    public void onFailure(Call<RSSResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            } else {
                KLog.i("One field at least is empty !");
                Toasty.error(this, "Fill in all fields").show();
            }

        } else {
            KLog.e("No network available !");
            Toasty.warning(this, "No network available").show();
        }
    }

    private void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth(), mCvAdd.getHeight(), mFab.getWidth(), mCvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mFab.setImageResource(R.drawable.plus_x);
                mCvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    private void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth(), mCvAdd.getHeight(), mCvAdd.getHeight(), mFab.getWidth());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mFab.setImageResource(R.drawable.plus);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    private void clearInputs() {
        mEtComment.setText("");
        mEtUrl.setText("");
        mEtTitle.setText("");
        mEtComment.clearFocus();
        mEtUrl.clearFocus();
        mEtTitle.clearFocus();
    }
}
