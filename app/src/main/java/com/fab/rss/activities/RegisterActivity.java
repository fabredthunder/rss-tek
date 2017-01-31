package com.fab.rss.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import com.fab.rss.R;
import com.fab.rss.RSSApp;
import com.fab.rss.utils.UtilsFunctions;
import com.fab.rss.utils.bundles.SignBundle;
import com.fab.rss.utils.models.AuthUser;
import com.fab.rss.utils.services.BaseApiService;
import com.fab.rss.utils.services.IApiService;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    private FloatingActionButton mFab;
    @BindView(R.id.cv_add)
    private CardView mCvAdd;
    @BindView(R.id.et_password)
    private EditText mEtPassword;
    @BindView(R.id.et_repeatpassword)
    private EditText mEtRepeatPassword;
    @BindView(R.id.et_username)
    private EditText mEtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick(R.id.btn_go)
    public void onClickBtnGo() {
        final String password = mEtPassword.getText().toString().trim();
        final String repeat = mEtRepeatPassword.getText().toString().trim();
        final String username = mEtUsername.getText().toString().trim();

        if (UtilsFunctions.isNetworkAvailable()) {

            if (password.equals(repeat)) {

                if (!"".equals(password) && !"".equals(repeat) && !"".equals(username)) {

                    final BaseApiService baseService = new BaseApiService();
                    IApiService service = baseService.create();
                    Call<AuthUser> call = service.signUp(new SignBundle(username, password));

                    call.enqueue(new Callback<AuthUser>() {
                        @Override
                        public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {

                            if (response.code() == 200) {

                                // Create a new user in the DAO
                                RSSApp.createUser(response.body());

                                KLog.i("New user created with username \"" + username + "\"");
                                Toasty.success(RegisterActivity.this, "Account successfully created").show();

                                // Animate transition and start new activity

                                Explode explode = new Explode();
                                explode.setDuration(500);

                                getWindow().setExitTransition(explode);
                                getWindow().setEnterTransition(explode);

                                ActivityOptionsCompat oc = ActivityOptionsCompat.makeSceneTransitionAnimation(RegisterActivity.this);

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent, oc.toBundle());
                                finish();

                            } else
                                Toasty.error(RegisterActivity.this, "Server error. Try again").show();

                        }

                        @Override
                        public void onFailure(Call<AuthUser> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                } else {
                    KLog.i("One input at least is empty !");
                    Toasty.error(this, "Fill in all fields").show();
                }

            } else {
                KLog.e("The two passwords are different !");
                Toasty.error(this, "The two passwords are different").show();
            }

        } else {
            KLog.e("No network available !");
            Toasty.warning(this, "No network available").show();
        }
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mCvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                // No need to do something here
            }

            @Override
            public void onTransitionPause(Transition transition) {
                // No need to do something here
            }

            @Override
            public void onTransitionResume(Transition transition) {
                // No need to do something here
            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mFab.getWidth() / 2, mCvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mCvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mCvAdd.getHeight(), mFab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mFab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
}
