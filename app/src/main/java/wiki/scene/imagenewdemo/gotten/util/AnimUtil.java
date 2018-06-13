package wiki.scene.imagenewdemo.gotten.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AnimUtil {

    public static void setShakeAnim(View view) {
        setShakeAnim(view, 10, 50, 3);
    }

    /**
     * @param view        需要抖动的view
     * @param deltax      x方向抖动的幅度
     * @param duration    持续的时间
     * @param repeatCount 重复抖动的次数
     */
    public static void setShakeAnim(View view, int deltax, long duration, int repeatCount) {
        Animation animation2 = new TranslateAnimation(-10, 10, 0, 0);
        animation2.setDuration(50);
        animation2.setRepeatCount(3);
        animation2.setRepeatMode(Animation.RESTART);
        view.startAnimation(animation2);
    }

    public static void rotateView(View view, int animation) {
        if (view == null) {
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), animation);
        view.startAnimation(anim);
    }

    public static void translateView(View view) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 400);
        view.setAnimation(anim);
        view.animate().setDuration(100).start();
    }

    private void BigPicture(View view, float scale) {
        Matrix matrix = new Matrix();
        //缩放区间 0.5-1.0
        //x y坐标同时缩放
        matrix.setScale(scale, scale, view.getMeasuredWidth() / 2, view.getMeasuredHeight() / 2);

    }

    public static void scaleAnim(View view, float scale, int time) {
        final ScaleAnimation animation = new ScaleAnimation(1.0f, scale, 1.0f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(time);//设置动画持续时间
        animation.setFillAfter(true);
        view.startAnimation(animation);
        animation.start();
    }

    public static void ScaleAnim(View view, float scale, long duration) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",
                1.0f, scale);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",
                1.0f, scale);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(duration);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }


    public static void FadedIO(View view, float toAlpha, int duration, final IAnim Ianim) {
        view.animate().alpha(toAlpha).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (Ianim != null) {
                    Ianim.IAnimEnd(animation);
                }
            }
        });
    }

    private static ObjectAnimator anim = new ObjectAnimator();

    public static void RotateView(View view, int duration) {

        anim.ofFloat(view, "rotation", 0.0f, 359.0f)
                .setDuration(duration)
                .start();
        anim.setRepeatCount(7000);
        anim.setInterpolator(new LinearInterpolator());
    }

}