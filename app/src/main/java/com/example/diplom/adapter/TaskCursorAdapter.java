package com.example.diplom.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.diplom.DBHelper;
import com.example.diplom.R;
import com.example.diplom.alarm.AlarmReceiverTask;

public class TaskCursorAdapter extends CursorAdapter  {

    private Context con;
    private String ch;
    CheckBox cb;
    Cursor cu;
    private LayoutInflater cursorInflater;
    private AlarmManager alarmManager;

    public TaskCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) LayoutInflater.from(context);
        con = (Context) context;
        cu = c;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final int pos = cursor.getPosition();
        cursor.moveToPosition(pos);

        cb = (CheckBox) view.findViewById(R.id.cb_task);
        cb.setText(cursor.getString(cursor.getColumnIndex("name_task")));

        ch = cursor.getString(cursor.getColumnIndex("ch"));

        if(ch.equals("1")){cb.setChecked(true);}
        else {cb.setChecked(false);}


        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper db = new DBHelper(con, 1);
                SQLiteDatabase mdb = db.getWritableDatabase();
                cursor.moveToPosition(pos);
                String id = cursor.getString(0);

                if(cb.isChecked()) {
                    mdb.execSQL("update Task set ch = '1' where _id = " + id);
                    changeCursor(cursor);
                    cancelAlarm(Integer.parseInt(id));
                    Toast.makeText(con, " Завершено", Toast.LENGTH_SHORT).show();
                }
                else {
                    mdb.execSQL("update Task set ch = '0' where _id = " + id);
                    changeCursor(cursor);
                    Toast.makeText(con, " Не завершено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.list_item_task, parent, false);
    }

    private void cancelAlarm( int id){
        alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(con, AlarmReceiverTask.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, id, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(con, "Напоминание отключено", Toast.LENGTH_SHORT).show();
    }
}
