package com.example.vophungquang.activities;
/**
 * Created by vophungquang
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vophungquang.R;
import com.example.vophungquang.define.Conts;
import com.example.vophungquang.sqlite.DatabaseUtil;
import com.example.vophungquang.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static int SPLASH_TIME_OUT = 1000;

    @BindView(R.id.content_splash)
    RelativeLayout contentSplash;
    @BindView(R.id.icon_splash)
    RelativeLayout iconSplash;

    private SharedPreferencesUtil preUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        loadAnimation();
        initDataBase();

//        Log.e("CREATE TABLE PLACE", SqliteHelper.CREATE_TABLE_PLACE_SQL);
//        Log.e("CREATE CATOGY", SqliteHelper.CREATE_TABLE_CATEGORY_SQL);
//        Log.e("INSEST", SqliteHelper.INSERT_CATEGORY_SQL);
    }

    private void loadAnimation() {
        Animation transAnimation = AnimationUtils.loadAnimation(this, R.anim.trasition_icon);
        contentSplash.setAnimation(transAnimation);
        transAnimation.setAnimationListener(this);
    }
    private void loaSplash() {
        Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_background);
        iconSplash.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(this);
    }

    private void initDataBase() {
        preUtil = new SharedPreferencesUtil(this);
        final boolean firstCreateDb = preUtil.getBoolean(Conts.FIRST_RUN_APP, false);
        if (!firstCreateDb) {
            new CreateDataBase(this, firstCreateDb).execute();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"Make by VO PHUNG QUANG",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SplashActivity.this, CategoriesActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class CreateDataBase extends AsyncTask<Void, Void, Void> {

        private Context context;
        private boolean firstRun;

        public CreateDataBase(Context context, boolean firstRun) {
            this.context = context;
            this.firstRun = firstRun;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseUtil databaseUtil = DatabaseUtil.getInstance(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            preUtil.saveBoolean(Conts.FIRST_RUN_APP, true);
        }
    }
}
