package wiki.scene.imagenewdemo.sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import wiki.scene.imagenewdemo.entity.BubbleLocalTemplateInfo;

/**
 * @author wupanjie
 */
public class BubbleSticker extends Sticker {

    private Drawable drawable;
    private Rect realBounds;
    private BubbleLocalTemplateInfo bubbleLocalTemplateInfo;

    public BubbleSticker(Drawable drawable, BubbleLocalTemplateInfo bubbleLocalTemplateInfo) {
        this.drawable = drawable;
        this.bubbleLocalTemplateInfo = bubbleLocalTemplateInfo;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    public BubbleLocalTemplateInfo getBubbleLocalTemplateInfo() {
        return bubbleLocalTemplateInfo;
    }

    @Override
    public BubbleSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public BubbleSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }

}
