package wiki.scene.imagenewdemo.gotten.weight.rotationPhoto;

import android.graphics.RectF;
import android.widget.ImageView;

public class Info {
    // 内部图片在整个窗口的位置
    RectF mRect = new RectF();
    // 控件在窗口的位置
    RectF mLocalRect = new RectF();
    RectF mImgRect = new RectF();
    RectF mWidgetRect = new RectF();
    RectF mInitImageRect = new RectF();
    float mScale;
    float mDegrees;
    ImageView.ScaleType mScaleType;

    public Info(RectF rect, RectF local, RectF img, RectF widget, float scale, float degrees, ImageView.ScaleType scaleType, RectF initImageRect) {
        mRect.set(rect);
        mLocalRect.set(local);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
        mInitImageRect = initImageRect;
    }

    @Override
    public String toString() {
        return "Info{" +
                "mRect=" + mRect +
                ", mLocalRect=" + mLocalRect +
                ", mImgRect=" + mImgRect +
                ", mWidgetRect=" + mWidgetRect +
                ", mInitImageRect=" + mInitImageRect +
                ", mScale=" + mScale +
                ", mDegrees=" + mDegrees +
                ", mScaleType=" + mScaleType +
                '}';
    }

    public RectF getmRect() {
        return mRect;
    }

    public RectF getmLocalRect() {
        return mLocalRect;
    }

    public RectF getmImgRect() {
        return mImgRect;
    }

    public RectF getmWidgetRect() {
        return mWidgetRect;
    }

    public float getmScale() {
        return mScale;
    }

    public float getmDegrees() {
        return mDegrees;
    }

    public ImageView.ScaleType getmScaleType() {
        return mScaleType;
    }

    public RectF getmInitImageRect() {
        return mInitImageRect;
    }
}