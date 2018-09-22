package com.orobator.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView) findViewById(R.id.image);

        Button translateButton = (Button) findViewById(R.id.translate_Button);
        Button scaleButton = (Button) findViewById(R.id.scale_Button);
        Button spinButton = (Button) findViewById(R.id.spin_Button);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator slideRight = ObjectAnimator.ofFloat(imageView, "translationX", 420);
                slideRight.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator slideLeft = ObjectAnimator.ofFloat(imageView, "translationX", 0);
                        slideLeft.setInterpolator(new OvershootInterpolator(0.5f));
                        slideLeft.setDuration(1000);
                        slideLeft.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                slideRight.setDuration(1000);
                slideRight.start();
            }
        });

        scaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.animate()
                        .scaleY(4)
                        .scaleX(4)
                        .setDuration(1000)
                        .setInterpolator(new BounceInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageView.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .setDuration(1000)
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        });

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater
                        .loadAnimator(MainActivity.this, R.animator.fade_spin);
                animatorSet.setTarget(imageView);
                animatorSet.start();
            }
        });
    }
}
