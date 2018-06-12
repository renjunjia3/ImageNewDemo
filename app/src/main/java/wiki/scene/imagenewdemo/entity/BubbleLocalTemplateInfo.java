package wiki.scene.imagenewdemo.entity;

import java.io.Serializable;

/**
 * 气泡模板
 */
public class BubbleLocalTemplateInfo implements Serializable {
    private int id;
    private String buddle_name;
    private String buddle_template_path;
    private String buddle_real_path;
    private String content;
    private String text_color;
    private int text_size;
    private String text_font;
    private String shader_color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuddle_name() {
        return buddle_name;
    }

    public void setBuddle_name(String buddle_name) {
        this.buddle_name = buddle_name;
    }

    public String getBuddle_template_path() {
        return buddle_template_path;
    }

    public void setBuddle_template_path(String buddle_template_path) {
        this.buddle_template_path = buddle_template_path;
    }

    public String getBuddle_real_path() {
        return buddle_real_path;
    }

    public void setBuddle_real_path(String buddle_real_path) {
        this.buddle_real_path = buddle_real_path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public int getText_size() {
        return text_size;
    }

    public void setText_size(int text_size) {
        this.text_size = text_size;
    }

    public String getText_font() {
        return text_font;
    }

    public void setText_font(String text_font) {
        this.text_font = text_font;
    }

    public String getShader_color() {
        return shader_color;
    }

    public void setShader_color(String shader_color) {
        this.shader_color = shader_color;
    }
}
