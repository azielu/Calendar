package com.example.calendar;

/**
 * Created by Adas on 20.03.2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calendar.db";
    private static final int DATABASE_VERSION = 1;

    interface Tables {
        String CALENDAR = "calendar";
        String CALENDARS_NAMES = "names";

    }

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.CALENDAR + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CalendarContract.CalendarColumns.CALENDAR_NAME + " TEXT NOT NULL,"
                        + CalendarContract.CalendarColumns.CALENDAR_YEAR + " TEXT NOT NULL,"
                        + CalendarContract.CalendarColumns.CALENDAR_MONTH + " TEXT NOT NULL,"
                        + CalendarContract.CalendarColumns.CALENDAR_DAY + " TEXT NOT NULL,"
                        + CalendarContract.CalendarColumns.CALENDAR_COLOR + " TEXT NOT NULL)"
        );

        db.execSQL("CREATE TABLE " + Tables.CALENDARS_NAMES + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME + " TEXT NOT NULL,"
                        + CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_DESCRIPTION + " TEXT NOT NULL,"
                        + CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS + " TEXT NOT NULL,"
                        + CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS_DESCRIPTION + " TEXT NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.CALENDAR);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


}
