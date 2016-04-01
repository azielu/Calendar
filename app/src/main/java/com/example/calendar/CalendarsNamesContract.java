package com.example.calendar;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adas on 07.03.2016.
 */
public class CalendarsNamesContract {
    interface CalendarNamesColumns {
        String CALENDARS_NAMES_ID = "_ID";
        String CALENDARS_NAMES_NAME = "name";
        String CALENDARS_NAMES_DESCRIPTION = "description";
        String CALENDARS_NAMES_COLORS = "calendar_color";
        String CALENDARS_NAMES_COLORS_DESCRIPTION = "calendar_color_description";

    }

    public static final String CONTENT_AUTHORITY = "com.example.calendar.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_CALENDAR = "names";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CALENDAR).build();

    public static class CalendarNames implements CalendarNamesColumns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd" + CONTENT_AUTHORITY + ".names";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd" + CONTENT_AUTHORITY + ".names";

        public static Uri buildNoteUri (String calendarId){
        return URI_TABLE.buildUpon().appendEncodedPath(calendarId).build();

    }


    }
}