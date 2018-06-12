package wiki.scene.imagenewdemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wiki.scene.imagenewdemo.util.MethodUtil;
import wiki.scene.imagenewdemo.util.ToastUtil;

public class SxPintuActivity extends AppCompatActivity {
    @BindView(R.id.layout_container)
    LinearLayout container;

    private ProgressDialog progressDialog;

    private List<String> imageUrls = new ArrayList<>();
    //内间距
    private int currentInnerSpace = 10;
    //外间距
    private int currentOutSpace = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sx_pintu);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在保存图片");

        initData();
    }

    private void initData() {
        imageUrls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3588772980,2454248748&fm=27&gp=0.jpg");
        imageUrls.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=635704053,1460945271&fm=27&gp=0.jpg");
        imageUrls.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=98013154,1220740613&fm=27&gp=0.jpg");
        imageUrls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3014892180,449401099&fm=27&gp=0.jpg");
        imageUrls.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2661055612,1771836516&fm=27&gp=0.jpg");
        imageUrls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=973931041,1439083005&fm=27&gp=0.jpg");
        imageUrls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1848984829,2212207435&fm=27&gp=0.jpg");

        int size = imageUrls.size();
        for (int i = 0; i < size; i++) {

            RelativeLayout relativeLayout = new RelativeLayout(this);
            ImageView imageView = new ImageView(this);
            Glide.with(SxPintuActivity.this).asBitmap().load(imageUrls.get(i)).into(imageView);
            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(imageLayoutParams);
            relativeLayout.addView(imageView);
            container.addView(relativeLayout);
        }
        changeInnerSpace();
        changeOutSpace();

    }


    @OnClick(R.id.save)
    public void onClickSave() {
        final String fileName = "wsManager_" + System.currentTimeMillis() + ".png";
        Log.e("保存的图片地址", fileName);
        progressDialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return MethodUtil.ScreenShotAndSaveImage(container, fileName);
            }

            @Override
            protected void onPostExecute(Boolean aBool) {
                super.onPostExecute(aBool);
                progressDialog.cancel();
                if (aBool) {
                    ToastUtil.show(SxPintuActivity.this, "保存成功");
                } else {
                    ToastUtil.show(SxPintuActivity.this, "保存出错");
                }
            }
        }.execute(null, null, null);
    }

    @OnClick(R.id.update_bg)
    public void onClickUpdateBg() {
        container.setBackgroundColor(Color.RED);
    }

    @OnClick(R.id.add_margin)
    public void onClickAddMargin() {
        //增大外间距
        currentOutSpace = currentOutSpace + 1 > 30 ? 30 : currentOutSpace + 1;
        Log.e("onClickAddMargin", "currentOutSpace:" + currentOutSpace);
        Log.e("onClickAddMargin", "currentInnerSpace:" + currentInnerSpace);
        changeOutSpace();
    }

    @OnClick(R.id.less_margin)
    public void onClickLessMargin() {
        //减小外间距
        currentOutSpace = currentOutSpace - 1 < 0 ? 0 : currentOutSpace - 1;
        Log.e("onClickLessMargin", "currentOutSpace:" + currentOutSpace);
        Log.e("onClickLessMargin", "currentInnerSpace:" + currentInnerSpace);
        changeOutSpace();
    }

    @OnClick(R.id.add_padding)
    public void onClickAddPadding() {
        //增大内间距
        currentInnerSpace = currentInnerSpace + 1 > 30 ? 30 : currentInnerSpace + 1;
        Log.e("onClickAddPadding", "currentOutSpace:" + currentOutSpace);
        Log.e("onClickAddPadding", "currentInnerSpace:" + currentInnerSpace);
        changeInnerSpace();
    }

    @OnClick(R.id.less_padding)
    public void onClickLessPadding() {
        //减小内间距
        currentInnerSpace = currentInnerSpace - 1 < 0 ? 0 : currentInnerSpace - 1;
        Log.e("onClickLessPadding", "currentOutSpace:" + currentOutSpace);
        Log.e("onClickLessPadding", "currentInnerSpace:" + currentInnerSpace);
        changeInnerSpace();
    }


    private void changeInnerSpace() {
        for (int i = 0; i < imageUrls.size(); i++) {
            if (i != imageUrls.size() - 1) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) container.getChildAt(i).getLayoutParams();
                lp.bottomMargin = currentInnerSpace;
                container.getChildAt(i).setLayoutParams(lp);
            } else {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) container.getChildAt(i).getLayoutParams();
                lp.bottomMargin = 0;
                container.getChildAt(i).setLayoutParams(lp);
            }
        }
    }

    private void changeOutSpace() {
        container.setPadding(currentOutSpace, currentOutSpace, currentOutSpace, currentOutSpace);
    }

}
