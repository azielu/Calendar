package com.example.calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by Adas on 14.03.2016.
 */
public class BootReceiver extends BroadcastReceiver {
        boolean notificationsEnabled;
    int alarmTimeHour = 0;
    int alarmTimeMinutes = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        setAlarms(context);


    }


    public void setAlarms(Context context){
        SharedPreferences prefs =  context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        notificationsEnabled = prefs.getBoolean("NotificationsEnabled",true);
        alarmTimeHour = prefs.getInt("AlarmTimeHour", 0);
        alarmTimeMinutes = prefs.getInt("AlarmTimeMinute", 0);
        if(notificationsEnabled) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimeHour);
            calendar.set(Calendar.MINUTE, alarmTimeMinutes);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

          alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
     //       alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        }
    }

}
