package wiki.scene.imagenewdemo.gotten.weight;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wiki.scene.imagenewdemo.R;
import wiki.scene.imagenewdemo.gotten.entity.LayoutEntity;
import wiki.scene.imagenewdemo.gotten.entity.MediaBean;
import wiki.scene.imagenewdemo.gotten.entity.Template;
import wiki.scene.imagenewdemo.gotten.event.ECode;
import wiki.scene.imagenewdemo.gotten.event.EventMsg;
import wiki.scene.imagenewdemo.gotten.util.AnimUtil;
import wiki.scene.imagenewdemo.gotten.util.GPUImageUtil;
import wiki.scene.imagenewdemo.gotten.util.ImageUtil;


/**
 * Created by LV on 8月9日.
 * Modified by Shawn on 10/8/2016
 * 修改内容为图片地址，图片加载方式使用Glide
 */
public class TemplateLayout extends RelativeLayout {

    private Template mTemplate;
    private boolean moving = false;
    private int position_from;
    private int position_to;
    private int position_operator;
    private int move_width = 200;
    private int move_height = 0;
    private ImageView moveView;
    private View operatorView;
    private List<View> mMaskLayouts = new ArrayList<>();
    private List<MaskableFrameLayout> mMaskableFrameLayouts = new ArrayList<>();
    private List<ImageView> mImageViews = new ArrayList<>();
    private List<ImageView> mRedLines = new ArrayList<>();
    private List<Path> mPaths = new ArrayList<>();
    private GestureDetector mGestureDetector;
    private Paint mPaint = null;
    private Path mBorderPath = null;
    private int inner_padding = 20;
    private int outter_padding = 20;
    private boolean filterViewVisible;
    private OnClickListener mListener;
    private int boardColorId = R.color.white;

    private List<MediaBean> mMediaBeanList = new ArrayList<>();


    public TemplateLayout(Context context) {
        super(context);
        init();
    }

    public TemplateLayout(Context context, Template mTemplate) {
        super(context);
        this.mTemplate = mTemplate;
        init();
    }

    public TemplateLayout(Context context, Template mTemplate, List<MediaBean> mMediaBeanList) {
        super(context);
        this.mTemplate = mTemplate;
        this.mMediaBeanList = mMediaBeanList;
        init();
    }

    public void setBackground(int colorId) {
        this.boardColorId = colorId;
        invalidate();
    }

    public TemplateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TemplateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        operatorView.post(new Runnable() {
            @Override
            public void run() {
                removeView(operatorView);
            }
        });
        super.onAttachedToWindow();
    }

    public void setListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    public void init() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers
        }
        initFrame();
        operatorView = LayoutInflater.from(getContext()).inflate(R.layout.layout_operator, this, false);
        operatorView.findViewById(R.id.operation_init).setOnClickListener(mOperatorClickListener);
        operatorView.findViewById(R.id.operation_change).setOnClickListener(mOperatorClickListener);
        operatorView.findViewById(R.id.operation_mirror).setOnClickListener(mOperatorClickListener);
        operatorView.findViewById(R.id.operation_filter).setOnClickListener(mOperatorClickListener);
        addView(operatorView);

        setBackgroundResource(boardColorId);
        setLongClickable(true);
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.e("mGestureDetector", "onDown");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.e("mGestureDetector", "onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e("mGestureDetector", "onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("mGestureDetector", "onScroll");
                clearMask();
                return false;
            }

            @Override
            public void onLongPress(final MotionEvent e) {
                Log.e("mGestureDetector", "onLongPress");
                clearMask();
                moving = true;
                moveView = new ImageView(getContext());
                moveView.setAdjustViewBounds(true);
                position_from = getTouchePosition(e);
                if (position_from < 0) {
                    return;
                }
                mImageViews.get(position_from).setVisibility(INVISIBLE);
                Bitmap bp = ((BitmapDrawable) mImageViews.get(position_from).getDrawable()).getBitmap();
                moveView.setImageBitmap(bp);
                move_height = bp.getHeight() * move_width / bp.getWidth();
                LayoutParams lp = new LayoutParams(move_width, move_height);
                lp.leftMargin = (int) e.getX() - move_width / 2;
                lp.topMargin = (int) e.getY() - move_height / 2;
                lp.rightMargin = mTemplate.getWidth() - (int) e.getX() - move_width / 2;
                lp.bottomMargin = mTemplate.getHeight() - (int) e.getY() - move_height / 2;
                lp.rightMargin = lp.rightMargin < 0 ? lp.rightMargin : 0;
                lp.bottomMargin = lp.bottomMargin < 0 ? lp.bottomMargin : 0;
                addView(moveView, lp);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e("mGestureDetector", "onFling");
                clearMask();
                return false;
            }
        });
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e("mGestureDetector", "onSingleTapConfirmed " + e.getRawX() + " " + e.getRawY());
                if (operatorView == null) {
                    return false;
                }

                position_operator = getTouchePosition(e);
                Log.e("FilterView", "" + isFilterViewVisible());
                if (isFilterViewVisible()) {
                    EventBus.getDefault().post(new EventMsg(ECode.HIDE_TEMPLATE_FILTER, null));
                } else if (operatorView.getParent() != null) {
                    if (operatorView != null) {
                        ((ViewGroup) operatorView.getParent()).removeView(operatorView);
                        setRedLine(-1);
                    }
                } else if (position_operator >= 0) {
                    setRedLine(position_operator);
                    int parentWidth = ((View) getParent()).getWidth();
                    int parentHeight = ((View) getParent()).getHeight();
                    int move_width = operatorView.getWidth();
                    int move_height = operatorView.getHeight();
                    LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    int centerX = (int) ((parentWidth - mTemplate.getWidth() * scale) / 2 + mTemplate.getLayout().get(position_operator).getX() * scale + mTemplate.getLayout().get(position_operator).getWidth() * scale / 2);
                    int centerY = (int) ((parentHeight - mTemplate.getHeight() * scale) / 2 + mTemplate.getLayout().get(position_operator).getY() * scale + mTemplate.getLayout().get(position_operator).getHeight() * scale / 2);
                    lp.leftMargin = centerX - move_width / 2;
                    lp.topMargin = centerY - move_height / 2;
                    lp.rightMargin = ((View) getParent()).getWidth() - centerX - move_width / 2;

                    if (lp.leftMargin < 10) {
                        lp.leftMargin = 10;
                        lp.rightMargin = 10;
                    }

                    if (lp.rightMargin < 10) {
                        lp.leftMargin = lp.leftMargin + lp.rightMargin - 10;
                        lp.rightMargin = 0;
                    }

                    ((ViewGroup) getParent()).addView(operatorView, lp);
                    operatorView.setVisibility(VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.e("mGestureDetector", "onDoubleTap");
                clearMask();
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.e("mGestureDetector", "onDoubleTapEvent");
                clearMask();
                return false;
            }
        });
    }

    private void initFrame() {
        mPaths.clear();
        mBaseMaskBitmaps.clear();
        if (mTemplate != null) {
            LayoutParams thislp = new LayoutParams(mTemplate.getWidth(), mTemplate.getHeight());
            thislp.addRule(RelativeLayout.CENTER_IN_PARENT);
            setLayoutParams(thislp);
            for (int i = 0; i < mTemplate.getLayout().size(); i++) {
                LayoutEntity layout = mTemplate.getLayout().get(i);

                View maskLayout = null;
                MaskableFrameLayout maskableFrameLayout = null;
                ImageView imageview = null;
                ImageView redline = null;
                boolean only_relayout = false;
                if (mMaskLayouts != null && mMaskLayouts.size() == mTemplate.getLayout().size()) {
                    only_relayout = true;
                    maskLayout = mMaskLayouts.get(i);
                    maskableFrameLayout = mMaskableFrameLayouts.get(i);
                    imageview = mImageViews.get(i);
                    redline = mRedLines.get(i);
//                    imageview.setVisibility(INVISIBLE);
                } else {
                    maskLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_mask_layout, this, false);
                    maskableFrameLayout = (MaskableFrameLayout) maskLayout.findViewById(R.id.mask_layout);
                    imageview = (ImageView) maskLayout.findViewById(R.id.imageview);
                    redline = (ImageView) maskLayout.findViewById(R.id.red_line);
                }

                LayoutParams lp = new LayoutParams(layout.getWidth(), layout.getHeight());
                lp.leftMargin = layout.getX();
                lp.topMargin = layout.getY();
                Log.e("initFrame1", layout.getWidth() + "  " + layout.getHeight());
                if (only_relayout) {
                    maskableFrameLayout.setLayoutParams(lp);
                } else {
                    addView(maskLayout, lp);
                    mMaskableFrameLayouts.add(maskableFrameLayout);
                    mMaskLayouts.add(maskLayout);
                    mImageViews.add(imageview);
                    mRedLines.add(redline);
                }

                Path path = getPath(layout.getPath());
                mBaseMaskBitmaps.add(getBaseMaskBitmap(path, layout.getWidth(), layout.getHeight()));
                maskableFrameLayout.setMaskBitmap(getNewMaskBitmap(mBaseMaskBitmaps.get(i), layout.getPath(), layout.getBorder()), path);
//                Path path = maskableFrameLayout.setPath(layout.getPath(), inner_padding / 2);
//                redline.setImageBitmap(getBitmap(path, layout.getWidth(), layout.getHeight()));
                redline.setImageResource(R.color.template_mask);
                mPaths.add(path);

                if (only_relayout) {
                    final ImageView finalImageview1 = imageview;
                    imageview.post(new Runnable() {
                        @Override
                        public void run() {
                            finalImageview1.setImageBitmap(((BitmapDrawable) finalImageview1.getDrawable()).getBitmap());
                        }
                    });
                } else if (!mMediaBeanList.isEmpty()) {
                    String imagePath = null;
                    final MediaBean mediaBean = mMediaBeanList.get(i);
                    if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200) {
                        imagePath = mediaBean.getThumbnailBigPath();
                    }
                    if (TextUtils.isEmpty(imagePath)) {
                        imagePath = mediaBean.getOriginalPath();
                    }
                    final ImageView finalImageview = imageview;
                    Glide.with(getContext()).asBitmap().load(imagePath).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @NonNull Transition<? super Bitmap> transition) {
                            final Bitmap bitmap = mediaBean.isMirror ? ImageUtil.getMirrorBitmap(resource) : resource;
                            finalImageview.post(new Runnable() {
                                @Override
                                public void run() {
                                    finalImageview.setImageBitmap(bitmap);
                                }
                            });
                            if (mediaBean.getFilterType() > 0) {
                                GPUImageUtil.displayGPUImage(getContext(), bitmap, mediaBean.getFilterType(), finalImageview);
                            }
                        }
                    });
                }

//                if (i == mTemplate.getLayout().size() - 1 && animation != null) {
//                    animation.start();
//                }
            }

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setAntiAlias(true);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

            mBorderPath = new Path();
            mBorderPath.moveTo(0, 0);
            mBorderPath.lineTo(0, mTemplate.getHeight());
            mBorderPath.lineTo(mTemplate.getWidth(), mTemplate.getHeight());
            mBorderPath.lineTo(mTemplate.getWidth(), 0);
            mBorderPath.close();
        }
    }

    public void clearMask() {
        EventBus.getDefault().post(new EventMsg(ECode.HIDE_TEMPLATE_FILTER, null));
        if (operatorView != null && operatorView.getParent() != null) {
            ((ViewGroup) operatorView.getParent()).removeView(operatorView);
            setRedLine(-1);
        }
    }

    /**
     * 选择了新的图片回调
     *
     * @param mediaBean
     */
    public void setCurBitmap(MediaBean mediaBean) {
        mMediaBeanList.remove(position_operator);
        mMediaBeanList.add(position_operator, mediaBean);
        String imagePath = null;
        if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200) {
            imagePath = mediaBean.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(imagePath)) {
            imagePath = mediaBean.getOriginalPath();
        }
        Glide.with(getContext()).asBitmap().load(imagePath).into(mImageViews.get(position_operator));
    }

    public void setCurBitmap(Bitmap bitmap) {
        mImageViews.get(position_operator).setImageBitmap(bitmap);
    }

    public void setInitBitmap() {
        String imagePath = null;
        MediaBean mediaBean = mMediaBeanList.get(position_operator);
        mediaBean.isMirror = false;
        mediaBean.setFilterType(0);
        if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200) {
            imagePath = mediaBean.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(imagePath)) {
            imagePath = mediaBean.getOriginalPath();
        }
        Glide.with(getContext()).asBitmap().load(imagePath).into(mImageViews.get(position_operator));
    }

    public int getPositionOperator() {
        return position_operator;
    }

    public boolean isFilterViewVisible() {
        return filterViewVisible;
    }

    public void setFilterViewVisible(boolean filterViewVisible) {
        this.filterViewVisible = filterViewVisible;
    }

    ViewPropertyAnimator animation;

    public void setTemplate(Template mTemplate) {
        this.mTemplate = mTemplate;
        final ImageView mask = new ImageView(getContext());
        mask.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mask.setScaleType(ImageView.ScaleType.FIT_XY);
        mask.setImageBitmap(createBitmap());
        addView(mask);
        animation = mask.animate();
        animation.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeView(mask);
                animation = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                removeView(mask);
                animation = null;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation.alpha(0).setDuration(500).start();
        initFrame();
    }

    public void reTemplate(int width, int height, Bitmap bitmap) {
        if (mTemplate != null) {
            mTemplate.setFitScreen(width, height, true);

            final ImageView mask = new ImageView(getContext());
            mask.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mask.setScaleType(ImageView.ScaleType.FIT_XY);
            mask.setImageBitmap(bitmap);
            addView(mask);
            animation = mask.animate();
            animation.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    removeView(mask);
                    animation = null;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    removeView(mask);
                    animation = null;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animation.alpha(0).setDuration(500).start();

            initFrame();
        }
    }

    float scale = 1;

    public void reTemplate(int width, int height) {
        float scaleWidth = width * 1.0f / mTemplate.getWidth();
        float scaleHeight = height * 1.0f / mTemplate.getHeight();
        scale = scaleHeight > scaleWidth ? scaleWidth : scaleHeight;
        AnimUtil.ScaleAnim(this, scale, 150);
    }

    public Bitmap createBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return bitmap;
    }

    public void setInnerPadding(int inner_padding) {
        this.inner_padding = inner_padding;
        for (int i = 0; i < mMaskableFrameLayouts.size(); i++) {
            mMaskableFrameLayouts.get(i).setMaskBitmap(getNewMaskBitmap(mBaseMaskBitmaps.get(i), mTemplate.getLayout().get(i).getPath(), mTemplate.getLayout().get(i).getBorder()), mPaths.get(i));
        }
    }

    private Bitmap getNewMaskBitmap(Bitmap baseBitmap, List<Point> pathPoints, List<Integer> border) {

        Bitmap newBitmap = Bitmap.createBitmap(baseBitmap);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.RED);

        List<Integer> inner = new ArrayList<>();
        for (int i = 0; i < pathPoints.size(); i++) {
            inner.add(i);
        }
        inner.removeAll(border);

        if (outter_padding > 0) {
            paint.setStrokeWidth(outter_padding);
            for (int i : border) {
                canvas.drawLine(pathPoints.get(i).x, pathPoints.get(i).y, pathPoints.get((i + 1) % pathPoints.size()).x, pathPoints.get((i + 1) % pathPoints.size()).y, paint);
            }
        }

        if (inner_padding > 0) {
            paint.setStrokeWidth(inner_padding / 2);
            for (int i : inner) {
                canvas.drawLine(pathPoints.get(i).x, pathPoints.get(i).y, pathPoints.get((i + 1) % pathPoints.size()).x, pathPoints.get((i + 1) % pathPoints.size()).y, paint);
            }
        }
        return newBitmap;
    }

    public void setOutterPadding(int outter_padding) {
        this.outter_padding = outter_padding;
        for (int i = 0; i < mMaskableFrameLayouts.size(); i++) {
            mMaskableFrameLayouts.get(i).setMaskBitmap(getNewMaskBitmap(mBaseMaskBitmaps.get(i), mTemplate.getLayout().get(i).getPath(), mTemplate.getLayout().get(i).getBorder()), mPaths.get(i));
        }
    }

    public Bitmap getBitmap(Path path, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
        return bitmap;
    }

    private int getTouchePosition(MotionEvent event) {
        for (int i = 0; i < mPaths.size(); i++) {
            Path path = mPaths.get(i);
            RectF r = new RectF();
            Region re = new Region();
            path.computeBounds(r, true);
            re.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
            if (re.contains((int) event.getX() - mTemplate.getLayout().get(i).getX(), (int) event.getY() - mTemplate.getLayout().get(i).getY())) {
                return i;
            }
        }
        return -1;
    }

    private void setRedLine(int position) {
        for (int i = 0; i < mRedLines.size(); i++) {
            mRedLines.get(i).setVisibility(i == position ? VISIBLE : GONE);
        }
    }

    private OnClickListener mOperatorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ViewGroup) operatorView.getParent()).removeView(operatorView);
            setRedLine(-1);
            switch (v.getId()) {
                case R.id.operation_init:
                    v.setTag(mImageViews.get(position_operator));
                    mListener.onClick(v);
                    setInitBitmap();
                    break;
                case R.id.operation_change:
                    v.setTag(mImageViews.get(position_operator));
                    mListener.onClick(v);
                    break;
                case R.id.operation_mirror:
                    v.setTag(mImageViews.get(position_operator));
                    mListener.onClick(v);

                    String imagePath = null;
                    final MediaBean mediaBean = mMediaBeanList.get(position_operator);
                    mediaBean.isMirror = !mediaBean.isMirror;
                    if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200) {
                        imagePath = mediaBean.getThumbnailBigPath();
                    }
                    if (TextUtils.isEmpty(imagePath)) {
                        imagePath = mediaBean.getOriginalPath();
                    }

                    Glide.with(getContext()).asBitmap().load(imagePath).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap bitmap = mediaBean.isMirror ? ImageUtil.getMirrorBitmap(resource) : resource;
                            if (mediaBean.getFilterType() == 0) {
                                mImageViews.get(position_operator).setImageBitmap(bitmap);
                            } else {
                                GPUImageUtil.displayGPUImage(getContext(), bitmap, mediaBean.getFilterType(), mImageViews.get(position_operator));
                            }
                        }
                    });

                    break;
                case R.id.operation_filter:
                    setFilterViewVisible(true);
                    mListener.onClick(v);
                    break;
            }
        }
    };


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onInterceptTouchEvent", "ACTION_DOWN " + ev.getX() + " " + ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("onInterceptTouchEvent", "ACTION_MOVE " + ev.getX() + " " + ev.getY());
                if (moving && moveView != null && moveView.getParent() != null) {
                    LayoutParams lp = (LayoutParams) moveView.getLayoutParams();
                    lp.leftMargin = (int) ev.getX() - move_width / 2;
                    lp.topMargin = (int) ev.getY() - move_height / 2;
                    lp.rightMargin = mTemplate.getWidth() - (int) ev.getX() - move_width / 2;
                    lp.bottomMargin = mTemplate.getHeight() - (int) ev.getY() - move_height / 2;
                    lp.rightMargin = lp.rightMargin < 0 ? lp.rightMargin : 0;
                    lp.bottomMargin = lp.bottomMargin < 0 ? lp.bottomMargin : 0;
                    moveView.setLayoutParams(lp);

                    int position = getTouchePosition(ev);
                    if (position >= 0 && position_from != position) {
                        setRedLine(position);
                    } else {
                        setRedLine(-1);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.e("onInterceptTouchEvent", "ACTION_UP " + ev.getX() + " " + ev.getY());
                if (moving) {
                    if (moveView != null && moveView.getParent() != null) {
                        ((ViewGroup) moveView.getParent()).removeView(moveView);
                    }
                    position_to = getTouchePosition(ev);
                    if (position_to >= 0 && position_from != position_to) {
                        try {
                            Collections.swap(mMediaBeanList, position_from, position_to);

                            Bitmap resourceFrom = ((BitmapDrawable) mImageViews.get(position_from).getDrawable()).getBitmap();
                            Bitmap resourceTo = ((BitmapDrawable) mImageViews.get(position_to).getDrawable()).getBitmap();
                            mImageViews.get(position_from).setImageBitmap(resourceTo);
                            mImageViews.get(position_to).setImageBitmap(resourceFrom);
                        } catch (Exception ex) {
                        }
                    }
                    if (position_from >= 0) {
                        mImageViews.get(position_from).setVisibility(VISIBLE);
                    }
                    setRedLine(-1);
                }
                moving = false;
                break;
        }
        boolean intercept = mGestureDetector.onTouchEvent(ev);
        return intercept;
    }

    public Path getPath(List<Point> pathPoints) {
        Path path = null;
        if (pathPoints != null && pathPoints.size() >= 3) {
            path = new Path();
            path.moveTo(pathPoints.get(0).x, pathPoints.get(0).y);
            for (int i = 1; i < pathPoints.size(); i++) {
                path.lineTo(pathPoints.get(i).x, pathPoints.get(i).y);
            }
            path.close();
        }
        return path;
    }

    private List<Bitmap> mBaseMaskBitmaps = new ArrayList<>();

    public Bitmap getBaseMaskBitmap(Path path, int w, int h) {

        Bitmap bitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(0f, 0f, w * 1f, h * 1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setColor(Color.BLACK);
        if (path != null) {
            canvas.drawPath(path, paint);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setXfermode(null);
//            canvas.drawPath(path, paint);
        }
        return bitmap;
    }
}