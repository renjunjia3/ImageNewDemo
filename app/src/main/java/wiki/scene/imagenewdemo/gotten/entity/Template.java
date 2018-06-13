package wiki.scene.imagenewdemo.gotten.entity;

import android.graphics.Point;

import java.util.List;

/**
 * Created by LV on 8月9日.
 */
public class Template {


    /**
     * width : 400
     * height : 800
     * layout : [{"x":0,"y":0,"width":400,"height":400,"path":[{"x":0,"y":0},{"x":200,"y":400},{"x":400,"y":0}]},{"x":0,"y":0,"width":200,"height":800,"path":[{"x":0,"y":0},{"x":0,"y":800},{"x":200,"y":800},{"x":200,"y":400}]},{"x":200,"y":0,"width":200,"height":800,"path":[{"x":200,"y":0},{"x":0,"y":400},{"x":0,"y":800},{"x":200,"y":800}]}]
     */

    private int width;

    private int height;
    /**
     * x : 0
     * y : 0
     * width : 400
     * height : 400
     * path : [{"x":0,"y":0},{"x":200,"y":400},{"x":400,"y":0}]
     */

    private List<LayoutEntity> layout;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<LayoutEntity> getLayout() {
        return layout;
    }

    public void setLayout(List<LayoutEntity> layout) {
        this.layout = layout;
    }

    public void setFitScreen(int measuredWidth, int measuredHeight, boolean uniform) {
        float scaleWidth = measuredWidth * 1.0f / width;
        float scaleHeight = measuredHeight * 1.0f / height;

        if (uniform) {
            float scale = scaleHeight > scaleWidth ? scaleWidth : scaleHeight;
            scaleWidth = scale;
            scaleHeight = scale;
        }

        this.height = (int) (height * scaleHeight);
        this.width = (int) (width * scaleWidth);
        for (LayoutEntity entity : layout) {
            entity.setHeight((int) (entity.getHeight() * scaleHeight));
            entity.setWidth((int) (entity.getWidth() * scaleWidth));
            entity.setX((int) (entity.getX() * scaleWidth));
            entity.setY((int) (entity.getY() * scaleHeight));
            for (Point p : entity.getPath()) {
                p.set((int) (p.x * scaleWidth), (int) (p.y * scaleHeight));
            }
        }
    }
}