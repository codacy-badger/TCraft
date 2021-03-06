package com.dkaishu.tcraft.feature.sample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;
import com.dkaishu.tcraftlib.widget.SelectorFactory;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;

/**
 * 入口
 * Created by dks on 2018/9/27.
 */

public class ClickEntranceActivity extends BaseActivity {

    private List<Entrance> entrances = new ArrayList<>();


    private void setData() {
        entrances.add(new Entrance("定时任务", ScheduledTimerActivity.class));
        entrances.add(new Entrance("上传下载", FileDownloadUploadActivity.class));
        entrances.add(new Entrance("Http请求", HttpRequestActivity.class));
//        entrances.add(new Entrance("获取坐标", LocationActivity.class));//存bug
        entrances.add(new Entrance("拍照选取", TakePhotoActivity.class));
        entrances.add(new Entrance("通知提醒", NotificationActivity.class));
        entrances.add(new Entrance("日历通知", CalendarActivity.class));
        entrances.add(new Entrance("LoadingDialog", LoadingDialogActivity.class));
        entrances.add(new Entrance("防误点击", TrimClickActivity.class));
        entrances.add(new Entrance("Cmd", CmdActivity.class));
//        entrances.add(new Entrance("自定义View", CustomViewTestActivity.class));


    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_click_entrance;
    }

    @Override
    protected void initView() {
        setData();

        SelectorFactory.ShapeSelector shapeSelector = SelectorFactory.newShapeSelector()
                .setStrokeWidth(2)
                .setCornerRadius(15)
                .setDefaultStrokeColor(GRAY)
                .setDefaultBgColor(WHITE)
                .setPressedBgColor(getResources().getColor(R.color.colorPrimary));

        LinearLayout ll_container = findViewById(R.id.ll_container);

        for (int i = 0; i < entrances.size(); i++) {
            Button    bt     = new Button(this);
            final int finalI = i;
            bt.setText(entrances.get(i).getTitle());
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ClickEntranceActivity.this, entrances.get(finalI).getCls()));
                }
            });
            shapeSelector.bind(bt);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 4, 0, 2);
            ll_container.addView(bt, layoutParams);
        }

    }


    @Override
    protected void initData() {

    }

    private static class Entrance {
        private String   title;
        private Class<?> cls;

        Entrance(String title, Class<?> cls) {
            this.title = title;
            this.cls = cls;
        }

        String getTitle() {
            return title;
        }

        Class<?> getCls() {
            return cls;
        }
    }
}
