package wiki.scene.imagenewdemo.gotten.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import wiki.scene.imagenewdemo.filter.IF1977Filter;
import wiki.scene.imagenewdemo.filter.IFAmaroFilter;
import wiki.scene.imagenewdemo.filter.IFBrannanFilter;
import wiki.scene.imagenewdemo.filter.IFEarlybirdFilter;
import wiki.scene.imagenewdemo.filter.IFHefeFilter;
import wiki.scene.imagenewdemo.filter.IFHudsonFilter;
import wiki.scene.imagenewdemo.filter.IFInkwellFilter;
import wiki.scene.imagenewdemo.filter.IFLomoFilter;
import wiki.scene.imagenewdemo.filter.IFLordKelvinFilter;
import wiki.scene.imagenewdemo.filter.IFNashvilleFilter;
import wiki.scene.imagenewdemo.filter.IFRiseFilter;
import wiki.scene.imagenewdemo.filter.IFSierraFilter;
import wiki.scene.imagenewdemo.filter.IFSutroFilter;
import wiki.scene.imagenewdemo.filter.IFToasterFilter;
import wiki.scene.imagenewdemo.filter.IFValenciaFilter;
import wiki.scene.imagenewdemo.filter.IFWaldenFilter;
import wiki.scene.imagenewdemo.filter.IFXprollFilter;

import static android.opengl.GLES20.GL_MAX_TEXTURE_SIZE;

public class GPUImageUtil {

    private static GPUImageFilter filter;

    //饱和度、亮度等参数指数
    private static int count;

    /**
     * 获取过滤器
     *
     * @param GPUFlag
     * @return 滤镜类型
     */

    public static GPUImageFilter getFilter(Context context, int GPUFlag) {
        switch (GPUFlag) {
            case 1:
                filter = new IFSutroFilter(context);
                break;
            case 2:
                filter = new IFAmaroFilter(context);
                break;
            case 3:
                filter = new IFRiseFilter(context);
                break;
            case 4:
                filter = new IFHudsonFilter(context);
                break;
            case 5:
                filter = new IFXprollFilter(context);
                break;
            case 6:
                filter = new IFSierraFilter(context);
                break;
            case 7:
                filter = new IFLomoFilter(context);
                break;
            case 8:
                filter = new IFEarlybirdFilter(context);
                break;
            case 9:
                filter = new IFToasterFilter(context);
                break;
            case 10:
                filter = new IFBrannanFilter(context);
                break;
            case 11:
                filter = new IFInkwellFilter(context);
                break;
            case 12:
                filter = new IFWaldenFilter(context);
                break;
            case 13:
                filter = new IFHefeFilter(context);
                break;
            case 14:
                filter = new IFValenciaFilter(context);
                break;
            case 15:
                filter = new IFNashvilleFilter(context);
                break;
            case 16:
                filter = new IF1977Filter(context);
                break;
            case 17:
                filter = new IFLordKelvinFilter(context);
                break;
        }
        return filter;
    }

    public static Bitmap getGPUImage(Context context, Bitmap bitmap, int FilterFlag) {
        Bitmap temp;
        if (bitmap.getWidth() >= GL_MAX_TEXTURE_SIZE || bitmap.getHeight() >= GL_MAX_TEXTURE_SIZE) {
            temp = ImageUtil.getFormatedSizeBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        } else {
            temp = bitmap;
        }
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(temp);
        gpuImage.setFilter(getFilter(context, FilterFlag));
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static void displayGPUImage(Context context, Bitmap bitmap, int FilterFlag, final ImageView imageView) {
        Bitmap temp;
        if (bitmap.getWidth() >= GL_MAX_TEXTURE_SIZE || bitmap.getHeight() >= GL_MAX_TEXTURE_SIZE) {
            temp = ImageUtil.getFormatedSizeBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        } else {
            temp = bitmap;
        }
        final GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(temp);
        gpuImage.setFilter(getFilter(context, FilterFlag));
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return gpuImage.getBitmapWithFilterApplied();
            }

            @Override
            protected void onPostExecute(Bitmap aBitmap) {
                super.onPostExecute(aBitmap);
                imageView.setImageBitmap(aBitmap);
                gpuImage.deleteImage();
            }
        }.execute(null, null, null);
    }

    public static void displayGPUImage(Context context, Bitmap bitmap, int FilterFlag, final ImageView imageView, int maxWidth, int maxHeight) {
        Bitmap temp = ImageUtil.getFormatedSizeBitmap(bitmap, maxWidth, maxHeight);
        final GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(temp);
        gpuImage.setFilter(getFilter(context, FilterFlag));
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return gpuImage.getBitmapWithFilterApplied();
            }

            @Override
            protected void onPostExecute(Bitmap aBitmap) {
                super.onPostExecute(aBitmap);
                imageView.setImageBitmap(aBitmap);
                gpuImage.deleteImage();
            }
        }.execute(null, null, null);
    }

    public static Bitmap getGPUImage(Context context, Bitmap bitmap, GPUImageFilter filter) {
        Bitmap temp;
        if (bitmap.getWidth() >= GL_MAX_TEXTURE_SIZE || bitmap.getHeight() >= GL_MAX_TEXTURE_SIZE) {
            temp = ImageUtil.getFormatedSizeBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        } else {
            temp = bitmap;
        }
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(temp);
        gpuImage.setFilter(filter);
        return gpuImage.getBitmapWithFilterApplied();
    }

    //调整饱和度、亮度等
    public static void changeSaturation(int curCount) {
        GPUImageUtil.count = curCount;
    }
}