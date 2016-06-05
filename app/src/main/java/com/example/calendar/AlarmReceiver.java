package com.example.calendar;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

/**
 * Created by Adas on 14.03.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        Intent notificationIntent = new Intent(context, ScreenSlidePagerActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.ic_launcher)
                .setSmallIcon(R.drawable.ic_launcher)
               .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle("Go for your goals!")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText("Save your progress.")
                .setAutoCancel(true);

        builder.setContentIntent(resultPendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

}
