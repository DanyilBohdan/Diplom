package com.example.diplom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class EventCursorAdapter extends CursorAdapter {

    private Context con;
    private String ch;
    CheckBox cb;
    DBHelper db;
    Cursor cu;
    private LayoutInflater cursorInflater;

    public EventCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) LayoutInflater.from(context);
        con = (Context) context;
        cu = c;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final int pos = cursor.getPosition();

        TextView tv = (TextView) view.findViewById(R.id.tv_event_time);
        tv.setText(cursor.getString(cursor.getColumnIndex("time_event")));

        cb = (CheckBox) view.findViewById(R.id.cb_event);
        cb.setText(cursor.getString(cursor.getColumnIndex("name_event")));

        ch = cursor.getString(cursor.getColumnIndex("ch"));

        if(ch.equals("1")){ cb.setChecked(true);}
        else {cb.setChecked(false);}

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(con, 1);
                SQLiteDatabase mdb = db.getWritableDatabase();
                cursor.moveToPosition(pos);

                if(cb.isChecked()) {
                    mdb.execSQL("update Event set ch = '1' where _id = " + cursor.getString(0));
                    changeCursor(cursor);
                    Toast.makeText(con, "Завершено", Toast.LENGTH_SHORT).show();
                }
                else {
                    mdb.execSQL("update Event set ch = '0' where _id = " + cursor.getString(0));
                    changeCursor(cursor);
                    Toast.makeText(con, "Не завершено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.list_item_event, parent, false);
    }
}
