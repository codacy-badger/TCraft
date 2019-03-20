package com.dkaishu.tcraft.feature.sample;

import android.Manifest;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;
import com.dkaishu.tcraftlib.utils.CalendarUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by dks on 2018/12/25.
 */
public class CalendarActivity extends BaseActivity {


    private TextView tv_show;
    private Button   bt_insert, bt_update, bt_insert_intent, bt_update_intent, bt_delete, bt_start_calendar, bt_all_event;

    private long mEventId;

    @Override
    protected String[] addCheckPermissions() {
        return new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {


        tv_show = findViewById(R.id.tv_show);
        bt_insert = findViewById(R.id.bt_insert);
        bt_insert_intent = findViewById(R.id.bt_insert_intent);
        bt_update = findViewById(R.id.bt_update);
        bt_update_intent = findViewById(R.id.bt_update_intent);
        bt_delete = findViewById(R.id.bt_delete);
        bt_start_calendar = findViewById(R.id.bt_start_calendar);
        bt_all_event = findViewById(R.id.bt_all_event);


        bt_insert.setOnClickListener(v -> {
            mEventId = CalendarUtils.insertCalendarEvent(CalendarActivity.this
                    , "000", 0, 0, "这是备注0", "这是地点0");
            //添加提醒
            CalendarUtils.insertEventReminder(CalendarActivity.this, mEventId, 10);
            ToastUtils.showShort("EventId: " + mEventId);
        });

        bt_insert_intent.setOnClickListener(v -> {
            Calendar beginCalendar = Calendar.getInstance();
            long     millis        = beginCalendar.getTimeInMillis();
            CalendarUtils.insertCalendarEventByIntent(CalendarActivity.this
                    , "111", millis, millis + 10 * 60 * 1000, "这是备注1", "这是地点1");
        });

        bt_update.setOnClickListener(v -> {
            Calendar beginCalendar = Calendar.getInstance();
            long     millis        = beginCalendar.getTimeInMillis();
            CalendarUtils.updateCalendarEvent(CalendarActivity.this, mEventId
                    , "000-000", millis, millis + 10 * 60 * 1000, "这是备注000-000", "这是地点000-000");
        });

        bt_update_intent.setOnClickListener(v -> {
            Calendar beginCalendar = Calendar.getInstance();
            long     millis        = beginCalendar.getTimeInMillis();
            CalendarUtils.updateCalendarEventByIntent(CalendarActivity.this, mEventId
                    , "000-000", millis, millis + 10 * 60 * 1000, "这是备注000-000", "这是地点000-000");
        });

        bt_delete.setOnClickListener(v -> {
            CalendarUtils.deleteCalendarEvent(CalendarActivity.this, mEventId);
        });
        bt_start_calendar.setOnClickListener(v -> {
            Calendar beginCalendar = Calendar.getInstance();
            long     millis        = beginCalendar.getTimeInMillis();
            CalendarUtils.startCalendar(CalendarActivity.this, millis);
        });

        bt_all_event.setOnClickListener(v -> {
            List<Integer> ids    = CalendarUtils.getAllEventID(CalendarActivity.this);
            StringBuffer  result = new StringBuffer();
            result.append("EventID:\n");
            for (int id : ids) {
                result.append(id);
                result.append("\n");
            }
            tv_show.setText(result);
//            if (!ids.isEmpty()) {
//                CalendarUtils.deleteCalendarEvent(CalendarActivity.this, ids.get(0));
//            }
        });

        int code = CalendarUtils.checkAndAddCalendarAccount(this);
        tv_show.setText("日历用户id： " + code);
    }

    @Override
    protected void initData() {

    }


}
