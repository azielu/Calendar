package com.example.calendar;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adas on 07.03.2016.
 */
public class CalendarContract {
    interface CalendarColumns {
        String CALENDAR_ID = "_ID";
        String CALENDAR_NAME = "name";
        String CALENDAR_YEAR = "calendar_year";
        String CALENDAR_MONTH = "calendar_month";
        String CALENDAR_DAY = "calendar_day";
        String CALENDAR_COLOR = "calendar_color";

    }

    public static final String CONTENT_AUTHORITY = "com.example.calendar.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_CALENDAR = "calendar";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CALENDAR).build();

    public static class Calendar implements CalendarColumns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd" + CONTENT_AUTHORITY + ".calendar";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd" + CONTENT_AUTHORITY + ".calendar";

        public static Uri buildNoteUri (String calendarId){
        return URI_TABLE.buildUpon().appendEncodedPath(calendarId).build();

    }


    }
}