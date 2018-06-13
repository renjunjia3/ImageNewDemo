package wiki.scene.imagenewdemo.gotten.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/11.
 * 封装一下拼图模板对象
 */
public class CombineTemplateData {
    /**
     * 模板的列表
     */
    private ArrayList<CombineTemplateBean> dataSet;
    /**
     *已选择的模板下标
     */
    private int SelectedIndex;

    /**
     * 显示模板的gridview的列数
     */
    private int columnNum;

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public int getSelectedIndex() {
        return SelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        SelectedIndex = selectedIndex;
    }

    public ArrayList<CombineTemplateBean> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<CombineTemplateBean> dataSet) {
        this.dataSet = dataSet;
    }

}
