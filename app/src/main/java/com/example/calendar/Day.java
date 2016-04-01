package com.example.calendar;

/**
 * Created by Adas on 21.03.2016.
 */
public class Day {

    private int mId, mYear, mMonth, mDay,mColor;
    private String mName;

    public Day(String name, int year, int month, int day, int color) {
        mName = name;
        mYear = year;
        mMonth = month;
        mDay = day;
        mColor = color;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
