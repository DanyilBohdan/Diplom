package com.example.diplom.alarm;

import static com.example.diplom.alarm.NotificationUtils.ANDROID_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.diplom.DBHelper;
import com.example.diplom.DateService;
import com.example.diplom.R;
import com.example.diplom.activity.MainActivity;

public class AlarmReceiverEvening extends BroadcastReceiver {

    private NotificationUtils mNotificationUtils;
    private int idN = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        DBHelper db = new DBHelper(context, 1);
        SQLiteDatabase mdb = db.getWritableDatabase();
        Cursor cursor = mdb.rawQuery("SELECT * FROM Task where date = '" + DateService.TodayDate() + "' and ch = '0'", null);

        if(cursor.getCount() > 0) {
            Intent in = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, idN, in, 0);
            Notification notification = new NotificationCompat.Builder(context)
                    .setChannelId(ANDROID_CHANNEL_ID)
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

            mNotificationUtils = new NotificationUtils(context);
            mNotificationUtils.getManager().notify(idN, notification);
        }
    }
}
