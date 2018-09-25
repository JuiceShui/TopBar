package com.yeeyuntech.newheader.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Description: Jojo on 2018/4/10 ,Copyright YeeyunTech
 */
public class AnimationUtils {
    public static void hideAnim(final View viewTop, final View viewBottom) {
        // view.animate().translationX()

        /*viewBottom.animate().translationX(viewBottom.getWidth()).translationY(-viewBottom.getHeight()).setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewTop.animate().translationX(0).translationY(0);
                    }
                });*/
        ObjectAnimator transXAnim = ObjectAnimator.ofFloat(viewTop, "translationX", 0, -viewTop.getWidth());
        ObjectAnimator transYAnim = ObjectAnimator.ofFloat(viewTop, "translationY", 0, viewTop.getHeight());
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(viewBottom, "alpha", 1f, 0f);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator transXAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationX", viewBottom.getWidth(), 0);
        ObjectAnimator transYAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationY", viewBottom.getHeight(), 0);
        ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(viewBottom, "alpha", 0f, 1f);
        set.playTogether( transYAnim, alphaAnim,  transYAnim1, alphaAnim1);
//                set.playSequentially(alphaAnim, scaleXAnim, scaleYAnim, rotateAnim, transXAnim, transYAnim);
        set.setDuration(300);
        set.start();
       /* set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ObjectAnimator transXAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationX", viewBottom.getWidth(), 0);
                ObjectAnimator transYAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationY", viewBottom.getHeight(), 0);
                ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(viewBottom, "alpha", 0f, 1f);
                AnimatorSet set1 = new AnimatorSet();
                set1.playTogether(transXAnim1, transYAnim1, alphaAnim);
                set1.setDuration(300);
                set1.start();
            }
        });*/
    }

    public static void showAnim(final View viewTop, final View viewBottom) {
        /*viewTop.animate().translationX(-viewTop.getWidth()).translationY(viewTop.getHeight()).setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewBottom.animate().translationX(0).translationY(0);
                    }
                });*/

        ObjectAnimator transXAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationX", 0, viewBottom.getWidth());
        ObjectAnimator transYAnim1 = ObjectAnimator.ofFloat(viewBottom, "translationY", 0, viewBottom.getHeight());
        ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(viewBottom, "alpha", 1f, 0f);

        ObjectAnimator transXAnim = ObjectAnimator.ofFloat(viewTop, "translationX", -viewTop.getWidth(), 0);
        ObjectAnimator transYAnim = ObjectAnimator.ofFloat(viewTop, "translationY", viewTop.getHeight(), 0);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(viewBottom, "alpha", 0f, 1f);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(transYAnim1, alphaAnim1, transYAnim, alphaAnim);
        set1.setDuration(300);
        set1.start();
        /*set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ObjectAnimator transXAnim = ObjectAnimator.ofFloat(viewTop, "translationX", -viewTop.getWidth(), 0);
                ObjectAnimator transYAnim = ObjectAnimator.ofFloat(viewTop, "translationY", viewTop.getHeight(), 0);
                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(viewBottom, "alpha", 0f, 1f);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(transXAnim, transYAnim, alphaAnim);
//                set.playSequentially(alphaAnim, scaleXAnim, scaleYAnim, rotateAnim, transXAnim, transYAnim);
                set.setDuration(300);
                set.start();
            }
        });*/
    }

    public static void initAnim(View viewTop, View viewBottom) {
        // viewTop.animate().translationX(300).translationY(90).setDuration(100);
    }
}
