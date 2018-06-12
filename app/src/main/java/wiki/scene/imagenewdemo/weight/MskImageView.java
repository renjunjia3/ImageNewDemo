package wiki.scene.imagenewdemo.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import wiki.scene.imagenewdemo.R;

public class MskImageView extends RelativeLayout {

    private ImageView imageView;
    private ImageView mask;

    public MskImageView(Context context) {
        super(context);
        init();
    }

    public MskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_msk_image_view, this);
        imageView = findViewById(R.id.imageview);
        mask = findViewById(R.id.mask);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setImageBitmap(String path) {
        String imagePath = null;
        Glide.with(getContext()).asBitmap().load(imagePath).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }
        });
    }

    public ImageView getImageview() {
        return imageView;
    }
}
