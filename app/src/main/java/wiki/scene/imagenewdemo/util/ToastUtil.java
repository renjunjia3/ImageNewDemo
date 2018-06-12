package wiki.scene.imagenewdemo.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Poker on 2016/8/9.
 */
public class ToastUtil {
    private static Toast toast;

    public static void dismiss() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    public static void show(Context mContext, String msg, boolean lastForever) {
        if (toast == null) {
            toast = Toast.makeText(mContext.getApplicationContext(), msg, 60 * 60 * 1000);
        } else {
            toast.cancel();
            toast = Toast.makeText(mContext.getApplicationContext(), msg, 60 * 60 * 1000);
        }
        //放在左上角。如果你想往右边移动，将第二个参数设为>0；往下移动，增大第三个参数；后两个参数都只得像素
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = null;
            }
        }, 60 * 60 * 1000);
    }

    public static void show(Context mContext, String msg) {
        show(mContext, msg, Toast.LENGTH_LONG);
    }

    public static void show(Context mContext, String msg, int time) {
        if (toast == null) {
            toast = Toast.makeText(mContext.getApplicationContext(), msg + "", time);
        } else {
            toast.cancel();
            toast = Toast.makeText(mContext.getApplicationContext(), msg + "", time);
        }
        //放在左上角。如果你想往右边移动，将第二个参数设为>0；往下移动，增大第三个参数；后两个参数都只得像素
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = null;
            }
        }, 800);
    }
}
