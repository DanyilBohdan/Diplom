package com.example.diplom.alarm;

import static com.example.diplom.alarm.NotificationUtils.ANDROID_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.diplom.IdBuffer;
import com.example.diplom.R;
import com.example.diplom.activity.NotifyEventActivity;

public class AlarmReceiverEvent extends BroadcastReceiver {
    private String massage = " ";
    private String tim = " ";
    private int idN = -1;
    private NotificationUtils mNotificationUtils;

    @Override
    public void onReceive(Context context, Intent intent) {
        massage = intent.getStringExtra("mes");
        idN = Integer.parseInt(intent.getStringExtra("idN"));
        tim = intent.getStringExtra("time");
        if(tim.length() != 1) tim = " в " + tim;

        Intent in = new Intent(context, NotifyEventActivity.class);

        IdBuffer.event_id = "" + idN;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, 0 );

        Notification notification = new NotificationCompat.Builder(context)
                .setChannelId(ANDROID_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_icon_to_do)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon_to_do))
                .setTicker("Напоминание о событии")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Событие")
                .setContentText(massage + tim)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000 })
                .setContentInfo(Long.toString(System.currentTimeMillis()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        mNotificationUtils = new NotificationUtils(context);
        mNotificationUtils.getManager().notify(idN, notification);
    }
}
