package wiki.scene.imagenewdemo.gotten.entity;

import android.graphics.Point;

import java.util.List;
public class LayoutEntity {
    private int x;
    private int y;
    private int width;
    private int height;
    /**
     * x : 0
     * y : 0
     */
    private List<Point> path;
    private List<Integer> border;
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
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
    public List<Point> getPath() {
        return path;
    }
    public void setPath(List<Point> path) {
        this.path = path;
    }
    public List<Integer> getBorder() {
        return border;
    }
    public void setBorder(List<Integer> border) {
        this.border = border;
    }
}