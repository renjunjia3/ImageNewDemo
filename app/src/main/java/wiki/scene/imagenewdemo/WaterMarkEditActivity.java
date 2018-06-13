package wiki.scene.imagenewdemo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wiki.scene.imagenewdemo.entity.BubbleLocalTemplateInfo;
import wiki.scene.imagenewdemo.event.UpdateBubbleEvent;
import wiki.scene.imagenewdemo.util.NinePatchUtil;

public class WaterMarkEditActivity extends AppCompatActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_new_content)
    EditText tvNewContent;
    @BindView(R.id.bg_layout)
    LinearLayout bgLayout;

    private int MAX_SIZE = 30;
    private int MIN_SIZE = 6;

    private BubbleLocalTemplateInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_mark_edit);
        ButterKnife.bind(this);
        info = (BubbleLocalTemplateInfo) getIntent().getSerializableExtra("data");
        tvContent.setMaxWidth(900);
        tvNewContent.setText(info.getContent());
        reDrawTextView();
    }

    @OnClick({R.id.confirm_content, R.id.add_text_size, R.id.less_text_size,
            R.id.change_text_color_red, R.id.change_text_color_yellow, R.id.change_text_color_green,
            R.id.change_text_font_default, R.id.change_text_font_1, R.id.change_text_font_2,
            R.id.confirm_update_bubble})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_content:
                info.setContent(tvNewContent.getText().toString());
                break;
            case R.id.add_text_size:
                info.setText_size(info.getText_size() + 1 > MAX_SIZE ? MAX_SIZE : info.getText_size() + 1);
                break;
            case R.id.less_text_size:
                info.setText_size(info.getText_size() - 1 < MIN_SIZE ? MIN_SIZE : info.getText_size() - 1);
                break;
            case R.id.change_text_color_red:
                info.setText_color("#FF0000");
                break;
            case R.id.change_text_color_yellow:
                info.setText_color("#FFFF00");
                break;
            case R.id.change_text_color_green:
                info.setText_color("#00FF00");
                break;
            case R.id.change_text_font_default:
                info.setText_font(null);
                break;
            case R.id.change_text_font_1:
                info.setText_font(Environment.getExternalStorageDirectory() + File.separator + "WSManager" + File.separator + "Font" + File.separator + "Azedo");
                break;
            case R.id.change_text_font_2:
                info.setText_font(Environment.getExternalStorageDirectory() + File.separator + "WSManager" + File.separator + "Font" + File.separator + "DFKaiSho-SU");
                break;
            case R.id.confirm_update_bubble:
                EventBus.getDefault().post(new UpdateBubbleEvent(info));
                finish();
                return;
        }
        reDrawTextView();
    }

    private void reDrawTextView() {
        if (!TextUtils.isEmpty(info.getBuddle_template_path())) {
            if (NinePatchUtil.getLocalNinePatch(WaterMarkEditActivity.this, info.getBuddle_template_path()) != null) {
                bgLayout.setBackground(NinePatchUtil.getLocalNinePatch(WaterMarkEditActivity.this, info.getBuddle_template_path()));
            }
        }
        if (!TextUtils.isEmpty(info.getText_font())) {
            Typeface typeface = Typeface.createFromFile(info.getText_font());
            tvContent.setTypeface(typeface);
        } else {
            tvContent.setTypeface(Typeface.DEFAULT);
        }
        tvContent.setText(info.getContent());
        tvContent.setTextSize(info.getText_size());
        tvContent.setTextColor(Color.parseColor(info.getText_color()));
        if (!TextUtils.isEmpty(info.getShader_color())) {
            tvContent.setShadowLayer(20, 0, 0, Color.parseColor(info.getShader_color()));
        }
    }

}
