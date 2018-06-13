package wiki.scene.imagenewdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import wiki.scene.imagenewdemo.entity.BubbleLocalTemplateInfo;
import wiki.scene.imagenewdemo.event.UpdateBubbleEvent;
import wiki.scene.imagenewdemo.sticker.BitmapStickerIcon;
import wiki.scene.imagenewdemo.sticker.BubbleSticker;
import wiki.scene.imagenewdemo.sticker.DeleteIconEvent;
import wiki.scene.imagenewdemo.sticker.DrawableSticker;
import wiki.scene.imagenewdemo.sticker.Sticker;
import wiki.scene.imagenewdemo.sticker.StickerIconEvent;
import wiki.scene.imagenewdemo.sticker.StickerView;
import wiki.scene.imagenewdemo.sticker.TextSticker;
import wiki.scene.imagenewdemo.sticker.ZoomIconEvent;
import wiki.scene.imagenewdemo.util.MethodUtil;
import wiki.scene.imagenewdemo.util.NinePatchUtil;
import wiki.scene.imagenewdemo.util.ToastUtil;

public class WaterMarkActivity extends AppCompatActivity {
    private static final String TAG = WaterMarkActivity.class.getSimpleName();
    public static final int PERM_RQST_CODE = 110;

    private StickerView stickerView;
    private RelativeLayout container;
    private LinearLayout base_container;
    private ImageView bg_waterMark;

    private ProgressDialog progressDialog;

    private SaveBitmapTask saveBitmapTask;

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (saveBitmapTask != null && saveBitmapTask.getStatus() == AsyncTask.Status.RUNNING) {
            saveBitmapTask.cancel(true);
            saveBitmapTask = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_mark);
        EventBus.getDefault().register(this);
        stickerView = findViewById(R.id.sticker_view);
        container = findViewById(R.id.container);
        base_container = findViewById(R.id.base_container);
        bg_waterMark = findViewById(R.id.bg_waterMark);

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "WSManager" + File.separator + "wsManager_1528782022697.png");

        Glide.with(this)
                .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3588772980,2454248748&fm=27&gp=0.jpg")
                .into(bg_waterMark);

        initstickerViewBaseAttr();
        addStickerListener();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在保存图片");

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
                if (stickerView.getCurrentSticker() instanceof BubbleSticker) {
                    BubbleSticker bubbleSticker = (BubbleSticker) stickerView.getCurrentSticker();
                    BubbleLocalTemplateInfo info = bubbleSticker.getBubbleLocalTemplateInfo();
                    Intent intent = new Intent(WaterMarkActivity.this, WaterMarkEditActivity.class);
                    intent.putExtra("data", info);
                    WaterMarkActivity.this.startActivity(intent);
                }

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

        stickerView.setLocked(true);
        final String fileName = "wsManager_" + System.currentTimeMillis() + ".png";
        Log.e("保存的图片地址", fileName);
        progressDialog.show();

        if (saveBitmapTask != null) {
            //cancel方法依然会执行doInBackground 然后执行onCancel
            saveBitmapTask.cancel(true);
            saveBitmapTask = null;
        }
        saveBitmapTask = new SaveBitmapTask(WaterMarkActivity.this);
        saveBitmapTask.execute(base_container, fileName);
    }

    private Bitmap fullBitmap;

    //添加气泡
    public void onClickAddQp(View view) {
        final BubbleLocalTemplateInfo templateInfo = new BubbleLocalTemplateInfo();
        templateInfo.setId(102);
        templateInfo.setBuddle_name("标签1");
        templateInfo.setBuddle_template_path(Environment.getExternalStorageDirectory() + File.separator + "bubble_3.9.png");
        templateInfo.setContent("你好吗你好吗你好吗你好吗？");
        templateInfo.setText_color("#FFFFFF");
        templateInfo.setText_size(16);

        container.addView(createBubbleTextView(templateInfo));
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        container.setDrawingCacheEnabled(true);
                        Bitmap bitmap = convertViewToBitmap(container);
                        fullBitmap = Bitmap.createBitmap(bitmap);
                        container.setDrawingCacheEnabled(false);
                        BubbleSticker drawableSticker = new BubbleSticker(new BitmapDrawable(getResources(), fullBitmap), templateInfo);
                        stickerView.addSticker(drawableSticker);
                        fullBitmap = null;
                        container.removeAllViews();
                    }
                }, 300);
            }
        });

    }

    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBubbleSuccess(UpdateBubbleEvent event) {
        if (event != null) {
            final BubbleLocalTemplateInfo templateInfo = event.getInfo();
            container.addView(createBubbleTextView(templateInfo));
            container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            container.setDrawingCacheEnabled(true);
                            Bitmap bitmap = convertViewToBitmap(container);
                            fullBitmap = Bitmap.createBitmap(bitmap);
                            container.setDrawingCacheEnabled(false);
                            BubbleSticker drawableSticker = new BubbleSticker(new BitmapDrawable(getResources(), fullBitmap), templateInfo);
                            stickerView.replace(drawableSticker);
                            fullBitmap = null;
                            container.removeAllViews();
                        }
                    }, 300);
                }
            });

        }
    }

    private TextView bubbleContentView;
    private LinearLayout bubbleBgLayout;
    private View bubbleView;

    private View createBubbleTextView(BubbleLocalTemplateInfo templateInfo) {

        if (bubbleView == null) {
            bubbleView = LayoutInflater.from(this).inflate(R.layout.layout_bubble, null);
            bubbleBgLayout = bubbleView.findViewById(R.id.bubble_bg_layout);
            bubbleContentView = bubbleView.findViewById(R.id.bubble_content_view);
        }

        if (!TextUtils.isEmpty(templateInfo.getBuddle_template_path())) {
            if (NinePatchUtil.getLocalNinePatch(WaterMarkActivity.this, templateInfo.getBuddle_template_path()) != null) {
                bubbleBgLayout.setBackground(NinePatchUtil.getLocalNinePatch(WaterMarkActivity.this, templateInfo.getBuddle_template_path()));
            }
        }
        if (!TextUtils.isEmpty(templateInfo.getText_font())) {
            Typeface typeface = Typeface.createFromFile(templateInfo.getText_font());
            bubbleContentView.setTypeface(typeface);
        } else {
            bubbleContentView.setTypeface(Typeface.DEFAULT);
        }
        bubbleContentView.setText(templateInfo.getContent());
        bubbleContentView.setTextSize(templateInfo.getText_size());
        bubbleContentView.setTextColor(Color.parseColor(templateInfo.getText_color()));
        if (!TextUtils.isEmpty(templateInfo.getShader_color())) {
            bubbleContentView.setShadowLayer(20, 0, 0, Color.parseColor(templateInfo.getShader_color()));
        }
        bubbleContentView.setMaxWidth(900);
        return bubbleView;
    }

    private void showToast(String message) {
        ToastUtil.show(this, message);
    }


    static class SaveBitmapTask extends AsyncTask<Object, Void, Boolean> {
        private WeakReference<WaterMarkActivity> weakAty;

        private SaveBitmapTask(WaterMarkActivity activity) {
            weakAty = new WeakReference<>(activity);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            //AsyncTask特性不管调没调用都会把方法执行完
            return MethodUtil.ScreenShotAndSaveImage((LinearLayout) params[0], (String) params[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (weakAty.get() != null) {
                weakAty.get().progressDialog.cancel();
                if (result) {
                    weakAty.get().showToast("保存成功");
                } else {
                    weakAty.get().showToast("保存失败");
                }
            }
        }
    }

}
