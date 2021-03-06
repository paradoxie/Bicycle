package com.paradoxie.bicycle.UI;/**
 * Created by xiehehe on 16/9/2.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paradoxie.bicycle.R;

/**
 * User: xiehehe
 * Date: 2016-09-02
 * Time: 20:38
 * FIXME
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        View target = findViewById(R.id.iv_bg);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha",
                0.0f, 1.0f);
        objectAnimator.setDuration(3000);
        objectAnimator.start();

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
            }
        });
    }
}
