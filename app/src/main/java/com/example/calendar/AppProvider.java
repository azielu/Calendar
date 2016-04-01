package com.example.calendar;

/**
 * Created by Adas on 20.03.2016.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by Adas on 07.03.2016.
 */
public class AppProvider extends ContentProvider {


    protected AppDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int CALENDAR = 100;
    private static final int CALENDAR_ID = 101;
    private static final int CALENDAR_NAMES = 200;
    private static final int CALENDAR_NAMES_ID = 201;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = CalendarContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "calendar", CALENDAR);
        matcher.addURI(authority, "calendar/*", CALENDAR_ID);
        matcher.addURI(authority, "names", CALENDAR_NAMES);
        matcher.addURI(authority, "names/*", CALENDAR_NAMES_ID);


        return matcher;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        AppDatabase.deleteDatabase(getContext());
        mOpenHelper = new AppDatabase(getContext());
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CALENDAR:
                return com.example.calendar.CalendarContract.Calendar.CONTENT_TYPE;
            case CALENDAR_ID:
                return com.example.calendar.CalendarContract.Calendar.CONTENT_ITEM_TYPE;

            case CALENDAR_NAMES:
                return com.example.calendar.CalendarsNamesContract.CalendarNames.CONTENT_TYPE;
            case CALENDAR_NAMES_ID:
                return com.example.calendar.CalendarsNamesContract.CalendarNames.CONTENT_ITEM_TYPE;


            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        switch (match) {
            case CALENDAR:
                queryBuilder.setTables(AppDatabase.Tables.CALENDAR);
                break;
            case CALENDAR_ID:
                queryBuilder.setTables(AppDatabase.Tables.CALENDAR);
                break;
            case CALENDAR_NAMES:
                queryBuilder.setTables(AppDatabase.Tables.CALENDARS_NAMES);
                break;
            case CALENDAR_NAMES_ID:
                queryBuilder.setTables(AppDatabase.Tables.CALENDARS_NAMES);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CALENDAR:
                long noteRecordId = db.insertOrThrow(AppDatabase.Tables.CALENDAR, null, values);
                return com.example.calendar.CalendarsNamesContract.CalendarNames.buildNoteUri(String.valueOf(noteRecordId));

            case CALENDAR_NAMES:
                noteRecordId = db.insertOrThrow(AppDatabase.Tables.CALENDARS_NAMES, null, values);
                return com.example.calendar.CalendarsNamesContract.CalendarNames.buildNoteUri(String.valueOf(noteRecordId));

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CALENDAR:
                return db.update(AppDatabase.Tables.CALENDAR, values, selection, selectionArgs);
            case CALENDAR_ID:
                return db.update(AppDatabase.Tables.CALENDAR, values, selection, selectionArgs);
            case CALENDAR_NAMES:
                return db.update(AppDatabase.Tables.CALENDARS_NAMES, values, selection, selectionArgs);
            case CALENDAR_NAMES_ID:
                return db.update(AppDatabase.Tables.CALENDARS_NAMES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri.equals(com.example.calendar.CalendarContract.BASE_CONTENT_URI)) {
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case CALENDAR:

                return db.delete(AppDatabase.Tables.CALENDAR, selection, selectionArgs);

            case CALENDAR_ID:

                return db.delete(AppDatabase.Tables.CALENDAR, selection, selectionArgs);

            case CALENDAR_NAMES:

                return db.delete(AppDatabase.Tables.CALENDARS_NAMES, selection, selectionArgs);

            case CALENDAR_NAMES_ID:

                return db.delete(AppDatabase.Tables.CALENDARS_NAMES, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
    }

}


