package wiki.scene.imagenewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.waterMark).setOnClickListener(this);
        findViewById(R.id.pintu_sx).setOnClickListener(this);
        findViewById(R.id.gotten_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waterMark:
                //水印
                start(WaterMarkActivity.class);
                break;
            case R.id.pintu_sx:
                //顺序拼图
                start(LongImageActivity.class);
                break;
            case R.id.gotten_image:
                //顺序拼图
                start(GottenImageActivity.class);
                break;
        }
    }

    private void start(Class<?> className) {
        Intent intent = new Intent(MainActivity.this, className);
        startActivity(intent);
    }
}
