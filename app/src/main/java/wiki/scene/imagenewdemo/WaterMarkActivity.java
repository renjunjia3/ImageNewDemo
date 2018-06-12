package wiki.scene.imagenewdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import java.io.File;
import java.util.Arrays;

import wiki.scene.imagenewdemo.sticker.BitmapStickerIcon;
import wiki.scene.imagenewdemo.sticker.DeleteIconEvent;
import wiki.scene.imagenewdemo.sticker.DrawableSticker;
import wiki.scene.imagenewdemo.sticker.Sticker;
import wiki.scene.imagenewdemo.sticker.StickerIconEvent;
import wiki.scene.imagenewdemo.sticker.StickerView;
import wiki.scene.imagenewdemo.sticker.TextSticker;
import wiki.scene.imagenewdemo.sticker.ZoomIconEvent;
import wiki.scene.imagenewdemo.util.FileUtil;

public class WaterMarkActivity extends AppCompatActivity {
    private static final String TAG = WaterMarkActivity.class.getSimpleName();
    public static final int PERM_RQST_CODE = 110;

    private StickerView stickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_mark);

        stickerView = (StickerView) findViewById(R.id.sticker_view);
        initstickerViewBaseAttr();
        addStickerListener();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_RQST_CODE);
        }
    }


    //初始化角落的icon
    private void initCornerIcon() {
        //currently you can config your own icons and icon event
        //the event you can custom
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.icon_sticker_delete),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.icon_sticker_drag),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon editIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.icon_sticker_edit),
                BitmapStickerIcon.RIGHT_TOP);
        editIcon.setIconEvent(new StickerIconEvent() {
            @Override
            public void onActionDown(StickerView stickerView, MotionEvent event) {

            }

            @Override
            public void onActionMove(StickerView stickerView, MotionEvent event) {

            }

            @Override
            public void onActionUp(StickerView stickerView, MotionEvent event) {
                //监听器 这儿只需要跳转到编辑界面就行
                Toast.makeText(stickerView.getContext(), "Hello World!", Toast.LENGTH_SHORT).show();
            }
        });
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, editIcon));
    }

    //初始化基础属性
    private void initstickerViewBaseAttr() {
        //设置背景颜色
        stickerView.setBackgroundColor(Color.WHITE);
        //是否锁定 锁定后就不能编辑
        stickerView.setLocked(false);
        //没看懂
        stickerView.setConstrained(true);
        //初始化角标
        initCornerIcon();
    }

    //添加监听  其实没啥用
    private void addStickerListener() {
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "权限申请完成", Toast.LENGTH_SHORT).show();
        }
    }

    //切换是否可编辑模式
    public void testLock(View view) {
        stickerView.setLocked(!stickerView.isLocked());
    }

    //移除当前选中
    public void testRemove(View view) {
        if (stickerView.removeCurrentSticker()) {
            Toast.makeText(WaterMarkActivity.this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(WaterMarkActivity.this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //移除全部水印
    public void testRemoveAll(View view) {
        stickerView.removeAllStickers();
    }

    //添加一张图片
    public void testAddImage(View view) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.paper_tie_1);
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        stickerView.addSticker(drawableSticker);
    }

    //添加一个图文模式的---标签
    public void testAddBQ(View view) {
        String string = "你好";
        TextSticker sticker = new TextSticker(this);
        sticker.setText(string);
        sticker.setTextColor(Color.parseColor("#78FFFB"));
        sticker.setShadow(Color.parseColor("#21FBFF"));


        /*
        原图大小300*123
        原图左偏移80
        原图顶部偏移32
        原图右偏移40
        原图底部偏移32
         */
        float scale = 450f / 300f;
        Rect rect = new Rect();
        //相对于原点的偏移量
        //需要文字区域 相对于标签的上下左右的偏移量
        //左偏移--实际的偏移量
        rect.left = (int) (80 * scale);
        //上偏移--实际的偏移量
        rect.top = (int) (32 * scale);
        //右偏移--真实宽度减去右偏移量
        rect.right = (int) ((300 - 40) * scale);
        //底部偏移--真实高度减去底部偏移量
        rect.bottom = (int) ((132 - 32) * scale);

        sticker.setDrawable(getResources().getDrawable(R.drawable.bq_042), rect);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();


        stickerView.addSticker(sticker);
    }


    //在标签或者纯文本基础上增加文字
    public void testChoosedItemAddText(View view) {
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) stickerView.getCurrentSticker();
            String string = textSticker.getText() + "+你好";
            textSticker.setText(string);
            textSticker.resizeText();
            stickerView.replace(textSticker);
        }
    }

    //在标签或者纯文本基础上减少文字
    public void testChoosedItemLessText(View view) {
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
            TextSticker textSticker = (TextSticker) stickerView.getCurrentSticker();
            String tempStr = textSticker.getText();
            if (tempStr != null && tempStr.length() > 2) {
                tempStr = tempStr.substring(0, tempStr.length() - 2);
            }
            textSticker.setText(tempStr);
            textSticker.resizeText();
            stickerView.replace(textSticker);
        }
    }

    //加大字号--但是做了优化导致默认显示的就是最大字号 只能先减再加
    public void onClickAddFont(View view) {
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
            TextSticker sticker = (TextSticker) stickerView.getCurrentSticker();
            //如果当前字体已经是最大则不改变
            sticker.setTextSize(sticker.getTextSize() + 1 > sticker.getMaxTextSize() ? sticker.getMaxTextSize() : sticker.getTextSize() + 1);
            sticker.resizeTextByChangeFont();
            stickerView.replace(sticker);
        }
    }

    //减小字号
    public void onClickLessFont(View view) {
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
            TextSticker sticker = (TextSticker) stickerView.getCurrentSticker();
            sticker.setTextSize(sticker.getTextSize() - 1 > sticker.getMinTextSizePixels() ? sticker.getTextSize() - 1 : sticker.getMinTextSizePixels());
            sticker.resizeTextByChangeFont();
            stickerView.replace(sticker);
        }
    }

    //保存
    public void onClickSaveImageBitmap(View view) {
        File file = FileUtil.getNewFile(WaterMarkActivity.this, "Sticker");
        if (file != null) {
            stickerView.save(file);
            Toast.makeText(WaterMarkActivity.this, "saved in " + file.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(WaterMarkActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
        }
    }

}