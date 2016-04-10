package com.example.calendar;

/**
 * Created by Adas on 22.03.2016.
 */
public class CalendarColors {

    String mCalendarName, mDescription, mColor, mColorDescription;

    public CalendarColors(String calendarName, String description, String color, String colorDescription) {
        mCalendarName = calendarName;
        mDescription = description;
        mColor = color;
        mColorDescription = colorDescription;


    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCalendarName() {
        return mCalendarName;
    }

    public void setCalendarName(String calendarName) {
        mCalendarName = calendarName;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColorDescription() {
        return mColorDescription;
    }

    public void setColorDescription(String colorDescription) {
        mColorDescription = colorDescription;
    }
}
