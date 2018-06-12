package wiki.scene.imagenewdemo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;

public class MethodUtil {
    public static boolean ScreenShotAndSaveImage(ViewGroup container) {
        int width1 = container.getMeasuredWidth();
        int height1 = container.getMeasuredHeight();
        Bitmap bmp1 = Bitmap.createBitmap(width1, height1,
                Bitmap.Config.ARGB_8888);
        container.draw(new Canvas(bmp1));
        Canvas canvas = new Canvas(bmp1);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawBitmap(bmp1, 0, 0, paint);
        return saveBitmap(bmp1);
    }

    public static boolean ScreenShotAndSaveImage(View container, String fileNme) {
        int width1 = container.getMeasuredWidth();
        int height1 = container.getMeasuredHeight();
        Bitmap bmp1 = Bitmap.createBitmap(width1, height1,
                Bitmap.Config.ARGB_4444);
        container.draw(new Canvas(bmp1));
        Canvas canvas = new Canvas(bmp1);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawBitmap(bmp1, 0, 0, null);
        return saveBitmap(bmp1, fileNme, getSavePicPath());
    }

    public static boolean saveBitmap(Bitmap btm, String fileName, String dirPath) {
        return saveBitmap(btm, fileName, dirPath, false);
    }

    public static boolean saveBitmap(Bitmap btm, String fileName, String dirPath, boolean isjpg) {
        File f = MethodUtil.CreateFileFromPath(dirPath, fileName);
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (isjpg) {
                btm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } else {
                btm.compress(Bitmap.CompressFormat.PNG, 90, out);
            }
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 保存拼图和水印图片的目录
     *
     * @return
     */
    public static String getSavePicPath() {
        return getExternalPath() + "/WSManager/";
    }

    private static boolean saveBitmap(Bitmap btm) {
        File f = MethodUtil.CreateFileFromPath(MethodUtil.getRootPath() + "weishang", "text.png");
        try {
            FileOutputStream out = new FileOutputStream(f);
            btm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getExternalPath() {
        String path = Environment.getExternalStorageDirectory().getPath();
        Log.e("data", "path " + path);
        return path;
    }

    public static String getRootPath() {
        return getExternalPath() + "/";
    }

    public static String saveBitmapWithPath(Bitmap btm) {
        String path = "";
        File f = MethodUtil.CreateFileFromPath(MethodUtil.getRootPath() + "weishang", "water_ink_" + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(f);
            btm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return path;
        } catch (Exception ex) {
            return null;
        }
    }

    public static File CreateFileFromPath(String dirPath, String name) {
        File destDir = new File(dirPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File f = new File(dirPath, name);
        if (f.exists()) f.delete();
        return f;
    }
}
