package com.test.coneve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView txtTitle1, txtTitle2, desTitle;
    RelativeLayout relativeLayout;
    Animation txtAnim, layoutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fall_down);
        layoutAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_to_top);


        txtTitle1 = (TextView) findViewById(R.id.tv1);
        txtTitle2 = (TextView) findViewById(R.id.tv2);
        desTitle = (TextView) findViewById(R.id.tv3);
        relativeLayout = (RelativeLayout) findViewById(R.id.relMain);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.setAnimation(layoutAnim);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            txtTitle1.setVisibility(View.VISIBLE);
                            txtTitle2.setVisibility(View.VISIBLE);
                            desTitle.setVisibility(View.VISIBLE);

                            txtTitle1.setAnimation(txtAnim);
                            txtTitle2.setAnimation(txtAnim);
                            desTitle.setAnimation(txtAnim);

                        }
                    }, 500);
                }
            }
        }, 1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 5000);

    }
}