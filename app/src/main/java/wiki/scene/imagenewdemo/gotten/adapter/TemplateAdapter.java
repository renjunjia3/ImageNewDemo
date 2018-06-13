package wiki.scene.imagenewdemo.gotten.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import wiki.scene.imagenewdemo.R;
import wiki.scene.imagenewdemo.gotten.entity.CombineTemplateBean;
import wiki.scene.imagenewdemo.gotten.entity.CombineTemplateData;

/**
 * Created by Administrator on 2016/8/11.
 */
public class TemplateAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<CombineTemplateBean> mDataList = new ArrayList<CombineTemplateBean>();
    private int selectedIndex = 0;

    public TemplateAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDataSet(CombineTemplateData data) {
        if (this.mDataList != null) {
            this.mDataList.clear();
        }
        this.mDataList.addAll(data.getDataSet());
        selectedIndex = data.getSelectedIndex();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public CombineTemplateBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_template, null);
            mHolder.imageIv = (ImageView) convertView.findViewById(R.id.iv_preview);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.imageIv.setImageResource(mDataList.get(position).getImgId());
        if (selectedIndex == position) {
            mHolder.imageIv.setBackgroundResource(R.drawable.template_selected);
        } else {
            mHolder.imageIv.setBackgroundResource(0);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView imageIv;
    }
}


