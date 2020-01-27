package com.example.diplom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiverT extends BroadcastReceiver {


    private NotificationManager nm;
    private int idN = 1;


    @Override
    public void onReceive(Context context, Intent intent) {

        DBHelper db = new DBHelper(context, 1);
        SQLiteDatabase mdb = db.getWritableDatabase();
        Cursor cursor = mdb.rawQuery("SELECT * FROM Task where date = '" + DS.TodayDate() + "' and ch = '0'", null);

        if(cursor.getCount() > 0) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent in = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, idN, in, 0);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_icon_to_do)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon_to_do))
                    .setTicker("Остались незавершенные задачи")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Остались незавершенные задачи")
                    .setContentText("Незавершеных задач " + cursor.getCount())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1000, 1000})
                    .setContentInfo(Long.toString(System.currentTimeMillis()))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();

            nm.notify(idN, notification);
        }
    }
}
