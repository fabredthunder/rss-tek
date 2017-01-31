package com.fab.rss.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.socks.library.KLog;

import java.util.List;

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
    private FlowingDrawer mDrawer;

    @BindView(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    private FloatingActionButton mFab;
    @BindView(R.id.cv_add)
    private CardView mCvAdd;
    @BindView(R.id.et_title)
    private EditText mEtTitle;
    @BindView(R.id.et_url)
    private EditText mEtUrl;
    @BindView(R.id.et_comment)
    private EditText mEtComment;

    FastItemAdapter<FeedItem> mFastAdapter;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    FeedItem.DeleteCallback deleteCallback = new FeedItem.DeleteCallback() {
        @Override
        public void onDelete(int position) {
            mFastAdapter.remove(position);
        }
    };

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
        mRecyclerView.setItemAnimator(new AlphaCrossFadeAnimator());
        mRecyclerView.setAdapter(mFastAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFastAdapter.clear();
                bind(null);
            }
        });

        bind(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mCvAdd.getVisibility() == View.VISIBLE)
            onClickFab();
        else
            super.onBackPressed();
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
                            mFastAdapter.add(new FeedItem(response.body(), deleteCallback, MainActivity.this));
                            KLog.json(response.body().toString());
                            Toasty.success(MainActivity.this, "Successfully added to your feed").show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            RSSApp.deleteUser();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void bind(final Bundle savedInstanceState) {

        if (UtilsFunctions.isNetworkAvailable()) {

            final BaseApiService baseService = new BaseApiService();
            IApiService service = baseService.create();
            Call<List<RSSResponse>> call = service.getListRSS(RSSApp.getToken());

            call.enqueue(new Callback<List<RSSResponse>>() {
                @Override
                public void onResponse(Call<List<RSSResponse>> call, Response<List<RSSResponse>> response) {

                    swipeRefreshLayout.setRefreshing(false);

                    if (response.code() == 200) {
                        KLog.json(response.body().toString());

                        for (RSSResponse item : response.body()) {
                            mFastAdapter.add(new FeedItem(item, deleteCallback, MainActivity.this));
                        }

                        if (savedInstanceState != null)
                            mFastAdapter.withSavedInstanceState(savedInstanceState);

                    } else if (response.code() == 503) {
                        Toasty.warning(MainActivity.this, "Server timeout. Trying again ...").show();
                        bind(savedInstanceState);
                    } else
                        Toasty.warning(MainActivity.this, "Server error. Can't get your feed").show();

                }

                @Override
                public void onFailure(Call<List<RSSResponse>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }
}
