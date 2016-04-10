package com.example.calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Adas on 21.03.2016.
 */


public class CalendarLoader extends AsyncTaskLoader<List<Day>> {

    private List<Day> mDays;
    private ContentResolver mContentResolver;
    private Cursor mCursor;


    public CalendarLoader(Context context, ContentResolver contentResolver) {
        super(context);
        mContentResolver = contentResolver;

    }

    @Override
    public List<Day> loadInBackground() {
        return null;
    }


    public List<CalendarColors> loadCalendarsInBackground() {
        List<CalendarColors> entries = new ArrayList<>();
        String[] projection = {
                BaseColumns._ID,
                CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME,
                CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_DESCRIPTION,
                CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS,
                CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS_DESCRIPTION,
        };


        Uri uri = CalendarsNamesContract.URI_TABLE;
        mCursor = mContentResolver.query(uri, projection, null, null, BaseColumns._ID + " ASC");
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {

                    String name = mCursor.getString(mCursor.getColumnIndex(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME));
                    String description = mCursor.getString(mCursor.getColumnIndex(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_DESCRIPTION));
                    String color = mCursor.getString(mCursor.getColumnIndex(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS));
                    String colorDescription = mCursor.getString(mCursor.getColumnIndex(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS_DESCRIPTION));


                    CalendarColors singleEntry = new CalendarColors(name, description, color, colorDescription);

                    entries.add(singleEntry);


                } while (mCursor.moveToNext());
            }
            mCursor.close();

        }

        return entries;
    }

    public List<Day> loadInBackground(Calendar calendar, String currentName) {
        List<Day> entries = new ArrayList<>();
        String[] projection = {
                BaseColumns._ID,
                CalendarContract.CalendarColumns.CALENDAR_NAME,
                CalendarContract.CalendarColumns.CALENDAR_YEAR,
                CalendarContract.CalendarColumns.CALENDAR_MONTH,
                CalendarContract.CalendarColumns.CALENDAR_DAY,
                CalendarContract.CalendarColumns.CALENDAR_COLOR,


        };
        String selection = CalendarContract.Calendar.CALENDAR_NAME + " =? and " + CalendarContract.Calendar.CALENDAR_YEAR + " =? and " + CalendarContract.Calendar.CALENDAR_MONTH + " =?";
        String[] selectionArgs = {currentName, String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)),};
        Uri uri = CalendarContract.URI_TABLE;
        mCursor = mContentResolver.query(uri, projection, selection, selectionArgs, BaseColumns._ID + " ASC");
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {

                    String name = (mCursor.getString(mCursor.getColumnIndex(CalendarContract.CalendarColumns.CALENDAR_NAME)));
                    int year = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(CalendarContract.CalendarColumns.CALENDAR_YEAR)));
                    int month = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(CalendarContract.CalendarColumns.CALENDAR_MONTH)));
                    int day = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(CalendarContract.CalendarColumns.CALENDAR_DAY)));
                    int color = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(CalendarContract.CalendarColumns.CALENDAR_COLOR)));

                    Day singleDay = new Day(name, year, month, day, color);

                    entries.add(singleDay);


                } while (mCursor.moveToNext());
            }
            mCursor.close();

        }

        return entries;
    }

    protected void saveToDatabase(List<Day> dayList) {
        for (Day d : dayList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.CalendarColumns.CALENDAR_COLOR, d.getColor());
            String selection = CalendarContract.CalendarColumns.CALENDAR_NAME + " =? and " + CalendarContract.Calendar.CALENDAR_YEAR + " =? and " + CalendarContract.Calendar.CALENDAR_MONTH + " =? and " + CalendarContract.Calendar.CALENDAR_DAY + " =?";
            String[] selectionArgs = {String.valueOf(d.getName()), String.valueOf(d.getYear()), String.valueOf(d.getMonth()), String.valueOf(d.getDay())};
            int rowUpdated = mContentResolver.update(CalendarContract.URI_TABLE, contentValues, selection, selectionArgs);
            if (rowUpdated == 0) {
                contentValues.put(CalendarContract.CalendarColumns.CALENDAR_NAME, d.getName());
                contentValues.put(CalendarContract.CalendarColumns.CALENDAR_YEAR, d.getYear());
                contentValues.put(CalendarContract.CalendarColumns.CALENDAR_MONTH, d.getMonth());
                contentValues.put(CalendarContract.CalendarColumns.CALENDAR_DAY, d.getDay());
                mContentResolver.insert(CalendarContract.URI_TABLE, contentValues);
            }
        }
    }

    protected void saveCalendarsToDatabase(String oldName, String name, String description, String colors, String colorDescription) {
        ContentValues contentValues;
        if (!(oldName == null)) {
            contentValues = new ContentValues();
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME, name);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_DESCRIPTION, description);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS, colors);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS_DESCRIPTION, colorDescription);

            String selection = CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME + " =?";
            String[] selectionArgs = {oldName};
            mContentResolver.update(CalendarsNamesContract.URI_TABLE, contentValues, selection, selectionArgs);

            contentValues = new ContentValues();
            contentValues.put(CalendarContract.CalendarColumns.CALENDAR_NAME, name);
            selection = CalendarContract.CalendarColumns.CALENDAR_NAME + " =?";
            selectionArgs[0] = oldName;
            mContentResolver.update(CalendarContract.URI_TABLE, contentValues, selection, selectionArgs);

        } else {
            contentValues = new ContentValues();
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME, name);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_DESCRIPTION, description);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS, colors);
            contentValues.put(CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_COLORS_DESCRIPTION, colorDescription);
            mContentResolver.insert(CalendarsNamesContract.URI_TABLE, contentValues);
        }
    }


    protected void deleteFromDatabase(String name) {

        String selection = CalendarsNamesContract.CalendarNamesColumns.CALENDARS_NAMES_NAME + " =?";
        String[] selectionArgs = {name};
        mContentResolver.delete(CalendarsNamesContract.URI_TABLE, selection, selectionArgs);

        selection = CalendarContract.CalendarColumns.CALENDAR_NAME + " =?";
        selectionArgs[0] = name;
        mContentResolver.delete(CalendarsNamesContract.URI_TABLE, selection, selectionArgs);

    }


    @Override
    public void deliverResult(List<Day> days) {
        if (isReset()) {
            if (days != null) {
                releaseResources();
                return;
            }
        }
        List<Day> oldDays = mDays;
        mDays = days;
        if (isStarted()) {
            super.deliverResult(days);

        }
        if (oldDays != null && oldDays != days) {
            releaseResources();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mDays != null) {
            deliverResult(mDays);
        }
        if (takeContentChanged()) {
            forceLoad();
        } else if (mDays == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mDays != null) {
            releaseResources();
            mDays = null;
        }
    }

    @Override
    public void onCanceled(List<Day> days) {
        super.onCanceled(days);
        releaseResources();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    private void releaseResources() {
        mCursor.close();
    }
}