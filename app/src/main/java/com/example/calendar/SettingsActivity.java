package com.example.calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox notificationEnabledCheckBox;
    private static TextView timeTextView;
    private static TextView delayTimeTextView;
    private boolean notificationsEnabled;
    private  SharedPreferences prefs;
    private Toolbar mToolbar;


    private static int  sHour,  sMinute, sDelayHour, sDelayMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notificationEnabledCheckBox = (CheckBox) findViewById(R.id.notification_enabled_checkbox);
        prefs = getSharedPreferences("Settings",MODE_PRIVATE);
        notificationsEnabled =prefs.getBoolean("NotificationsEnabled", true);
        notificationEnabledCheckBox.setChecked(notificationsEnabled);
        timeTextView= (TextView) findViewById(R.id.time_text_view);
        delayTimeTextView= (TextView) findViewById(R.id.delay_time_text_view);

        if (notificationsEnabled) {
            timeTextView.setVisibility(View.VISIBLE);
        }
        sHour = prefs.getInt("AlarmTimeHour",0);
        sMinute = prefs.getInt("AlarmTimeMinute",0);
        sDelayHour = prefs.getInt("DelayTimeHour",0);
        sDelayMinute = prefs.getInt("DelayTimeMinute",0);

        if(sMinute<10){
            timeTextView.setText("TIME: "+sHour + ":0"+sMinute);
        } else {
            timeTextView.setText("TIME: "+sHour + ":"+sMinute);
        }


        if(sDelayMinute<10){
            delayTimeTextView.setText("TIME: "+sDelayHour + ":0"+sDelayMinute);
        } else {
            delayTimeTextView.setText("TIME: "+sDelayHour + ":"+sDelayMinute);
        }

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppTimePickerDialog timePickerDialog = new AppTimePickerDialog();
                timePickerDialog.show(getSupportFragmentManager(), "TIME_PICKER");



            }
        });

        delayTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelayTimePickerDialog delayTimePickerDialog = new DelayTimePickerDialog();
                delayTimePickerDialog.show(getSupportFragmentManager(), "DELAY_TIME_PICKER");



            }
        });



    }



    public static class AppTimePickerDialog extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener{
        private int mHour, mMinute;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            mHour=c.get(Calendar.HOUR_OF_DAY);
            mMinute=c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(minute<10){
                timeTextView.setText("TIME: "+hourOfDay + ":0"+minute);
            } else {
                timeTextView.setText("TIME: "+hourOfDay + ":"+minute);
            }
            sHour=hourOfDay;
            sMinute=minute;

            SharedPreferences.Editor editor = ((SettingsActivity) getActivity()).prefs.edit();
            editor.putInt("AlarmTimeHour",sHour);
            editor.putInt("AlarmTimeMinute",sMinute);
            editor.apply();
            ((SettingsActivity) getActivity()).setAlarms();

        }
    }

    public static class DelayTimePickerDialog extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener{
        private int mHour, mMinute;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            mHour=0;
            mMinute=0;
            return new TimePickerDialog(getActivity(), this, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(minute<10){
                delayTimeTextView.setText("TIME: "+hourOfDay + ":0"+minute);
            } else {
                delayTimeTextView.setText("TIME: "+hourOfDay + ":"+minute);
            }
            sDelayHour=hourOfDay;
            sDelayMinute=minute;

            SharedPreferences.Editor editor = ((SettingsActivity) getActivity()).prefs.edit();
            editor.putInt("DelayTimeHour",sDelayHour);
            editor.putInt("DelayTimeMinute",sDelayMinute);
            editor.apply();


        }
    }

    public void setNotification(View view){


                SharedPreferences.Editor editor = prefs.edit();
                if (notificationEnabledCheckBox.isChecked()){
                    notificationEnabledCheckBox.setChecked(true);
                    editor.putBoolean("NotificationsEnabled",true);

                    notificationsEnabled =true;
                    setAlarms();
                    timeTextView.setVisibility(View.VISIBLE);
                    editor.apply();

                } else{
                    notificationEnabledCheckBox.setChecked(false);
                    editor.putBoolean("NotificationsEnabled",false);
                    editor.apply();
                    notificationsEnabled =false;
                    timeTextView.setVisibility(View.INVISIBLE);
                    cancelAlarms();
                }


            }

    public void setAlarms(){

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, sHour);
            calendar.set(Calendar.MINUTE, sMinute);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

          alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Alarm set daily at "+sHour+":"+ (sMinute<10?"0"+sMinute:sMinute), Toast.LENGTH_LONG).show();

    }

    public void cancelAlarms(){

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, sHour);
            calendar.set(Calendar.MINUTE, sMinute);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            alarmManager.cancel(pendingIntent);

            Toast.makeText(this, "Alarm cancelled.", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

onBackPressed();


        }
        return (super.onOptionsItemSelected(menuItem));

    }
}
