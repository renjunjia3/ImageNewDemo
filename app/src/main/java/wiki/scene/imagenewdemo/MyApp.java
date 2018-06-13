package wiki.scene.imagenewdemo;

import android.app.Application;
import android.util.DisplayMetrics;

import com.squareup.leakcanary.LeakCanary;

public class MyApp extends Application {

    public static int screenWidth = 1080;
    public static int screenHeight = 1920;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
}
