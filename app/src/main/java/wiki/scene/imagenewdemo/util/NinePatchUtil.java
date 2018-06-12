package wiki.scene.imagenewdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.drawable.NinePatchDrawable;

public class NinePatchUtil {

    public static NinePatchDrawable getLocalNinePatch(Context context, String path) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            byte[] chunk = bitmap.getNinePatchChunk();  // 如果.9.png没有经过第一步，那么chunk就是null
            if (chunk != null && NinePatch.isNinePatchChunk(chunk) && NinePatchChunk.deserialize(chunk) != null) {
                return new NinePatchDrawable(context.getResources(), bitmap, chunk, NinePatchChunk.deserialize(chunk).mPaddings, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
