package wiki.scene.imagenewdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wiki.scene.imagenewdemo.gotten.adapter.TemplateAdapter;
import wiki.scene.imagenewdemo.gotten.entity.CombineTemplateBean;
import wiki.scene.imagenewdemo.gotten.entity.CombineTemplateData;
import wiki.scene.imagenewdemo.gotten.entity.MediaBean;
import wiki.scene.imagenewdemo.gotten.entity.Template;
import wiki.scene.imagenewdemo.gotten.weight.TemplateLayout;
import wiki.scene.imagenewdemo.util.MethodUtil;
import wiki.scene.imagenewdemo.util.ToastUtil;

/**
 * 拼图
 */
public class GottenImageActivity extends AppCompatActivity {

    @BindView(R.id.filter)
    RecyclerView mFilterRecyclerView;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.template_gridView)
    GridView templateGridView;

    //具体怎么切分的点的json
    private String[] templateJSon;
    //图片源数据
    private List<MediaBean> mMediaBeanList = new ArrayList<>();
    //模板的view
    private TemplateLayout templateLayout;
    //模板监听器
    private CommonListener mListener;
    //正方形模板
    private ArrayList<CombineTemplateBean> templateSquareDataSet = new ArrayList<>();
    //矩形模板
    private ArrayList<CombineTemplateBean> templateRectangleDataSet = new ArrayList<>();
    //模板
    private ArrayList<CombineTemplateData> mTemplates = new ArrayList<>();

    private boolean isSquare = true;

    private int choosedPosition = 0;

    private int currentInnerBorder = 20;
    private int currentOutterBorder = 20;
    //拼图模板的adapter
    private TemplateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotten_image);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        mListener = new CommonListener();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterRecyclerView.setLayoutManager(linearLayoutManager);
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                templateJSon = getResources().getStringArray(R.array.TemplatesJson);
                Template template = new Gson().fromJson(templateJSon[(mMediaBeanList.size() - 1) * 12], Template.class);
                template.setFitScreen(container.getMeasuredWidth(), container.getMeasuredHeight(), true);
                templateLayout = new TemplateLayout(GottenImageActivity.this, template, mMediaBeanList);
                templateLayout.setListener(mListener);
                container.addView(templateLayout, 0);
            }
        });

        initPintuTypeGridView(mMediaBeanList.size());
    }

    private void initData() {
        MediaBean mediaBean1 = new MediaBean();
        mediaBean1.setBucketDisplayName("小精灵美化壁纸");
        mediaBean1.setBucketId("1287629322");
        mediaBean1.setCreateDate(1528886352);
        mediaBean1.setHeight(1920);
        mediaBean1.setId(60);
        mediaBean1.setModifiedDate(1528886352);
        mediaBean1.setMimeType("image");
        mediaBean1.setWidth(560);
        mediaBean1.setLength(823024);
        mediaBean1.setTitle("20180613180000@7ab4fefa60967a624c656300e8ebae26");
        mediaBean1.setOriginalPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@7ab4fefa60967a624c656300e8ebae26.jpg");
        mediaBean1.setThumbnailBigPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@7ab4fefa60967a624c656300e8ebae26.jpg");
        mediaBean1.setThumbnailSmallPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@7ab4fefa60967a624c656300e8ebae26.jpg");


        MediaBean mediaBean2 = new MediaBean();
        mediaBean2.setBucketDisplayName("小精灵美化壁纸");
        mediaBean2.setBucketId("1287629322");
        mediaBean2.setCreateDate(1528886352);
        mediaBean2.setHeight(1280);
        mediaBean2.setId(60);
        mediaBean2.setModifiedDate(1528886365);
        mediaBean2.setMimeType("image");
        mediaBean2.setWidth(720);
        mediaBean2.setLength(39018);
        mediaBean2.setTitle("20180613180000@fcd7796316d73b9affd4f6a23fe18d1b");
        mediaBean2.setOriginalPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@fcd7796316d73b9affd4f6a23fe18d1b.jpg");
        mediaBean2.setThumbnailBigPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@fcd7796316d73b9affd4f6a23fe18d1b.jpg");
        mediaBean2.setThumbnailSmallPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@fcd7796316d73b9affd4f6a23fe18d1b.jpg");

        MediaBean mediaBean3 = new MediaBean();
        mediaBean3.setBucketDisplayName("小精灵美化壁纸");
        mediaBean3.setBucketId("1287629322");
        mediaBean3.setCreateDate(1528886345);
        mediaBean3.setHeight(960);
        mediaBean3.setId(60);
        mediaBean3.setModifiedDate(1528886352);
        mediaBean3.setMimeType("image");
        mediaBean3.setWidth(540);
        mediaBean3.setLength(39018);
        mediaBean3.setTitle("20180613180000@43c65774bdc2b53fce7dc566c538e678");
        mediaBean3.setOriginalPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@43c65774bdc2b53fce7dc566c538e678.jpg");
        mediaBean3.setThumbnailBigPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@43c65774bdc2b53fce7dc566c538e678.jpg");
        mediaBean3.setThumbnailSmallPath("/storage/emulated/0/小精灵美化/小精灵美化壁纸/20180613180000@43c65774bdc2b53fce7dc566c538e678.jpg");

        mMediaBeanList.add(mediaBean1);
        mMediaBeanList.add(mediaBean2);
        mMediaBeanList.add(mediaBean3);
    }

    @OnClick({R.id.save,
            R.id.square_template, R.id.rect_template,
            R.id.change_bg_red, R.id.change_bg_green, R.id.change_bg_image,
            R.id.change_inner_border_big, R.id.change_inner_border_small,
            R.id.change_outter_border_big, R.id.change_outter_border_small})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                saveImage();
                break;
            case R.id.square_template:
                isSquare = true;
                refresh(templateSquareDataSet.get(choosedPosition).getTemplateBean());
                setTemplateTypeView();
                break;
            case R.id.rect_template:
                isSquare = false;
                refresh(templateSquareDataSet.get(choosedPosition).getTemplateBean());
                setTemplateTypeView();
                break;
            case R.id.change_bg_red:
                changeBackground("#0000FF");
                break;
            case R.id.change_bg_green:
                changeBackground("#00FF00");
                break;
            case R.id.change_bg_image:
                changeBackground("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3354977464,1094655506&fm=173&app=25&f=JPEG?w=218&h=146&s=EDE2BC44380222470AAD89900300C093");
                break;
            case R.id.change_inner_border_big:
                currentInnerBorder = currentInnerBorder + 2 > 60 ? 60 : currentInnerBorder + 2;
                changeInnerPadding(currentInnerBorder);
                break;
            case R.id.change_inner_border_small:
                currentInnerBorder = currentInnerBorder - 2 < 0 ? 0 : currentInnerBorder - 2;
                changeInnerPadding(currentInnerBorder);
                break;
            case R.id.change_outter_border_big:
                currentOutterBorder = currentOutterBorder + 2 > 60 ? 60 : currentOutterBorder + 2;
                changeOutterPadding(currentOutterBorder);
                break;
            case R.id.change_outter_border_small:
                currentOutterBorder = currentOutterBorder - 2 < 0 ? 0 : currentOutterBorder - 2;
                changeOutterPadding(currentOutterBorder);
                break;
        }
    }

    /**
     * 方法有内存泄漏 修改成静态的AsyncTask就行 参考其他Activity
     */
    private void saveImage() {
        if (templateLayout != null) {
            templateLayout.clearMask();
        }
        final String fileName = "wsManager_" + System.currentTimeMillis() + ".png";
        final View v = container.getChildAt(0);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return MethodUtil.ScreenShotAndSaveImage(v, fileName);
            }

            @Override
            protected void onPostExecute(Boolean aBool) {
                super.onPostExecute(aBool);
                if (aBool) {
                    ToastUtil.show(GottenImageActivity.this, "保存成功");
                    String fullPath = MethodUtil.getSavePicPath() + fileName;
                    Bundle bundle = new Bundle();
                    bundle.putString("path", fullPath);
                } else {
                    ToastUtil.show(GottenImageActivity.this, "保存出错");
                }
            }
        }.execute(null, null, null);
    }

    private void refresh(Template t) {
        if (!isSquare) {
            t.setFitScreen(600, 800, false);
        }
        if (templateLayout != null) {
            templateLayout.clearMask();
        }
        t.setFitScreen(container.getMeasuredWidth(), container.getMeasuredHeight(), true);
        templateLayout.setTemplate(t);
    }

    //修改背景
    private void changeBackground(String colorStrOrDrawableRes) {
        if (templateLayout != null) {
            templateLayout.clearMask();
        }

        if (colorStrOrDrawableRes.startsWith("#")) {
            container.getChildAt(0).setBackgroundColor(Color.parseColor(colorStrOrDrawableRes));
        } else if (colorStrOrDrawableRes.startsWith("http")) {
            Glide.with(this).asBitmap().load(colorStrOrDrawableRes).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    container.getChildAt(0).setBackground(new BitmapDrawable(getResources(), resource));
                }
            });

        } else {
            container.getChildAt(0).setBackgroundResource(this.getResources().getIdentifier(colorStrOrDrawableRes, "drawable", getPackageName()));
        }
    }

    //修改内边距
    private void changeInnerPadding(int padding) {
        if (templateLayout != null) {
            templateLayout.clearMask();
        }
        templateLayout.setInnerPadding(padding);
    }

    private void changeOutterPadding(int padding) {
        if (templateLayout != null) {
            templateLayout.clearMask();
        }
        templateLayout.setOutterPadding(padding);
    }


    private void initPintuTypeGridView(int imageCount) {
        String[] templateJSon = getResources().getStringArray(R.array.TemplatesJson);
        Resources res = getResources();
        String packageName = getPackageName();
        for (int i = imageCount * 12; i < (imageCount + 1) * 12; i++) {
            //方形
            CombineTemplateBean bean = new CombineTemplateBean();
            bean.setId(i);
            bean.setChoosed(i % 12 == 0);
            bean.setImgId(res.getIdentifier("pic_square_0" + imageCount + (i % 12 < 9 ? ("_0" + (i % 12 + 1)) : ("_" + (i % 12 + 1))), "drawable", packageName));
            bean.setTemplateJson(templateJSon[i - 12]);
            templateSquareDataSet.add(bean);
            //矩形
            CombineTemplateBean bean1 = new CombineTemplateBean();
            bean1.setId(i);
            bean1.setImgId(res.getIdentifier("pic_length_0" + imageCount + (i % 12 < 9 ? ("_0" + (i % 12 + 1)) : ("_" + (i % 12 + 1))), "drawable", packageName));
            bean1.setTemplateJson(templateJSon[i]);
            templateRectangleDataSet.add(bean1);
        }
        CombineTemplateData data = new CombineTemplateData();
        int column1 = templateSquareDataSet.size() % 2 == 0 ? templateSquareDataSet.size() / 2 : templateSquareDataSet.size() / 2 + 1;
        data.setColumnNum(column1);
        data.setDataSet(templateSquareDataSet);
        mTemplates.add(data);

        CombineTemplateData data1 = new CombineTemplateData();
        int column2 = templateRectangleDataSet.size() % 2 == 0 ? templateRectangleDataSet.size() / 2 : templateRectangleDataSet.size() / 2 + 1;
        data1.setDataSet(templateRectangleDataSet);
        data1.setColumnNum(column2);
        mTemplates.add(data1);

        adapter = new TemplateAdapter(this);
        adapter.setDataSet(data);
        templateGridView.setAdapter(adapter);
        templateGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosedPosition = position;
                mTemplates.get(0).setSelectedIndex(choosedPosition);
                mTemplates.get(1).setSelectedIndex(choosedPosition);

                adapter.setDataSet(mTemplates.get(isSquare ? 0 : 1));
                adapter.notifyDataSetChanged();
                Template template = templateSquareDataSet.get(position).getTemplateBean();
                refresh(template);
            }
        });
    }

    private void setTemplateTypeView() {
        int selectIndex = mTemplates.get(isSquare ? 0 : 1).getSelectedIndex();
        mTemplates.get(isSquare ? 0 : 1).setSelectedIndex(selectIndex);
        adapter.setDataSet(mTemplates.get(isSquare ? 0 : 1));
        adapter.notifyDataSetChanged();
        Template t = adapter.getItem(selectIndex).getTemplateBean();
        if (!isSquare) {
            t.setFitScreen(600, 800, false);
        }
    }

    class CommonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.operation_init:
                    break;
                case R.id.operation_change:
                    break;
                case R.id.operation_mirror:
                    break;
                case R.id.operation_filter:
                    break;
            }
        }
    }
}
