package com.fab.rss.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
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
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (RSSApp.userIsConnected()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.btn_go)
    public void onClickBtnGo() {
        final String password = mEtPassword.getText().toString().trim();
        final String username = mEtUsername.getText().toString().trim();

        if (UtilsFunctions.isNetworkAvailable()) {

            if (!"".equals(password) && !"".equals(username)) {

                final BaseApiService baseService = new BaseApiService();
                IApiService service = baseService.create();
                Call<AuthUser> call = service.signIn(new SignBundle(username, password));

                call.enqueue(new Callback<AuthUser>() {
                    @Override
                    public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {

                        if (response.code() == 200) {

                            // Create a new user in the DAO
                            RSSApp.createUser(response.body());

                            KLog.i("Now logged in with username \"" + username + "\"");
                            Toasty.success(LoginActivity.this, "Successfully logged in").show();

                            launchMainActivity();

                        } else if (response.code() == 500) {
                            Toasty.error(LoginActivity.this, "Invalid username or password").show();
                        } else if (response.code() == 503) {
                            Toasty.warning(LoginActivity.this, "Server timeout. Trying again ...").show();
                            onClickBtnGo();
                        } else
                            Toasty.warning(LoginActivity.this, "Server error. Try again").show();

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
            KLog.e("No network available !");
            Toasty.warning(this, "No network available").show();
        }
    }

    @OnClick(R.id.fab)
    public void onClickFab() {
        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
            startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
        } else {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    // Animate transition and start new activity
    private void launchMainActivity() {
        Explode explode = new Explode();
        explode.setDuration(500);

        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);

        ActivityOptionsCompat oc = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent, oc.toBundle());
        finish();
    }
}
