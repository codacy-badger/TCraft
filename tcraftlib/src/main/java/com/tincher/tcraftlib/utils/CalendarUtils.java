package com.tincher.tcraftlib.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 日历事件、提醒等的增删改查
 * Created by dks on 2018/12/26.
 */
public class CalendarUtils {
    private final static String CALENDAR_URL          = "content://com.android.calendar/calendars";
    private final static String CALENDAR_EVENT_URL    = "content://com.android.calendar/events";
    private final static String CALENDAR_REMINDER_URL = "content://com.android.calendar/reminders";


    //如果不存在日历账户，添加一个
    private final static String CALENDARS_NAME         = "tcraft";
    private final static String CALENDARS_ACCOUNT_NAME = "tcraft@tincher.com";
    private final static String CALENDARS_ACCOUNT_TYPE = "com.android.tcraft";
    private final static String CALENDARS_DISPLAY_NAME = "tcraft";

    private CalendarUtils() {
    }

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再次进行查询
     * 获取账户成功返回账户id，否则返回-1
     */
    public static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDAR_URL),
                null, null, null, null);
        try {
            if (userCursor == null) { // 查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { // 存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }


    private static long addCalendarAccount(Context context) {
        TimeZone      timeZone = TimeZone.getDefault();
        ContentValues value    = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDAR_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                        CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri  result = context.getContentResolver().insert(calendarUri, value);
        long id     = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }


    /**
     * return eventId, 如果 eventId <= 0 ,则添加失败
     * 建议该方法在子线程执行
     */
    public static long insertCalendarEvent(Context context, String title, long beginTimeMillis
            , long endTimeMillis, String description, String location) {

        long eventId;

        if (context == null || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            return -2;
        }

        int calId = checkAndAddCalendarAccount(context); // 获取日历账户的id
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            return -1;
        }

        // 如果起始时间为零，使用当前时间
        if (beginTimeMillis == 0) {
            Calendar beginCalendar = Calendar.getInstance();
            beginTimeMillis = beginCalendar.getTimeInMillis();
        }
        // 如果结束时间为零，使用起始时间+30分钟
        if (endTimeMillis == 0) {
            endTimeMillis = beginTimeMillis + 30 * 60 * 1000;
        }

        try {
            /* 插入日程 */
            ContentValues eventValues = new ContentValues();
            eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis);
            eventValues.put(CalendarContract.Events.DTEND, endTimeMillis);
            eventValues.put(CalendarContract.Events.TITLE, title);
            eventValues.put(CalendarContract.Events.DESCRIPTION, description);
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, location);

            TimeZone tz = TimeZone.getDefault(); // 获取默认时区
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

            Uri eUri = context.getContentResolver().insert(Uri.parse(CALENDAR_EVENT_URL), eventValues);
            eventId = ContentUris.parseId(eUri);
            if (eventId == 0) { // 插入失败
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return eventId;
    }


    public static int updateCalendarEvent(Context context, long eventID, String title
            , long beginTimeMillis, long endTimeMillis, String description, String location) {
        if (context == null) {
            return -2;
        }
        int rows;
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DTSTART, beginTimeMillis);
            values.put(CalendarContract.Events.DTEND, endTimeMillis);
            values.put(CalendarContract.Events.DESCRIPTION, description);
            values.put(CalendarContract.Events.EVENT_LOCATION, location);

            Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            rows = context.getContentResolver().update(updateUri, values, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return rows;


    }

    /**
     * return 返回受影响的条数， >0 时 表示成功，=0，执行成功但未找到eventID相应的数据
     * 建议该方法在子线程执行
     */
    public static int deleteCalendarEvent(Context context, long eventID) {
        if (context == null) {
            return -2;
        }
        int rows;
        try {
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            rows = context.getContentResolver().delete(deleteUri, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return rows;
    }

    /**
     * 获取日历所有event的id
     */
    public static List<Integer> getAllEventID(Context context) {
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDAR_EVENT_URL),
                null, null, null, null);

        List<Integer> ids = new ArrayList<>();
        try {
            if (eventCursor == null) { // 查询返回空值
                return null;
            }
            if (eventCursor.getCount() > 0) {
                // 遍历所有事件
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    int id = eventCursor.getInt(eventCursor
                            .getColumnIndex(CalendarContract.Calendars._ID));
                    ids.add(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return ids;
    }

    /**
     * 插入提醒
     */
    public static boolean insertEventReminder(Context context, long eventId, int priorMinutes) {
        try {
            ContentValues reminderValues = new ContentValues();
            // uri.getLastPathSegment();
            reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminderValues.put(CalendarContract.Reminders.MINUTES, priorMinutes);
            reminderValues.put(CalendarContract.Reminders.METHOD,
                    CalendarContract.Reminders.METHOD_ALERT);
            Uri rUri = context.getContentResolver().insert(Uri.parse(CALENDAR_REMINDER_URL),
                    reminderValues);
            if (rUri == null || ContentUris.parseId(rUri) == 0) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 打开日历并定位到特定时间点
     * startMillis = 0 时，不定位具体时间点，默认
     */
    public static void startCalendar(Context context, long startMillis) {
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        if (startMillis != 0) ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        context.startActivity(intent);
    }

    /**
     * 打开外部日历添加事件，不必获取Calendar权限
     */
    public static void insertCalendarEventByIntent(Context context, String title, long beginTimeMillis
            , long endTimeMillis, String description, String location) {

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTimeMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        context.startActivity(intent);

    }


    /**
     * 打开外部日历更新事件，不必获取Calendar权限
     * 字段 title ; beginTimeMillis ; endTimeMillis ; description ; location 为Null时不更新相应字段
     */
    public static void updateCalendarEventByIntent(Context context, long eventID, String title
            , long beginTimeMillis, long endTimeMillis, String description, String location) {
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(uri);
        if (title != null)
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTimeMillis);
        if (title != null) intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis);
        if (title != null) intent.putExtra(CalendarContract.Events.TITLE, title);
        if (title != null) intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        if (title != null) intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        context.startActivity(intent);

    }
}
