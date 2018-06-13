package wiki.scene.imagenewdemo.gotten.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/8/11.
 */
public class CombineTemplateBean {

    /**
     * 模板id
     */
    private int id;
    private String templateJson;
    private int imgId;
    private boolean choosed;

    public Template getTemplateBean() {
        try {
            return new Gson().fromJson(templateJson, Template.class);
        } catch (Exception e) {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateJson() {
        return templateJson;
    }

    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }
}
