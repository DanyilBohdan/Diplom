package com.example.diplom.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diplom.alarm.AlarmReceiverTask;
import com.example.diplom.DBHelper;
import com.example.diplom.DateService;
import com.example.diplom.R;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    private Switch switchTimeTask;
    private FloatingActionButton fab;
    private String time = " ";
    private String strDate;
    private EditText et_name, et_coment;

    private AlarmManager alarmManager;
    private Calendar cal;
    int h = -1;
    int m = -1;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DBHelper(this, 1);
        et_name = (EditText) findViewById(R.id.et_taskName);
        et_coment = (EditText) findViewById(R.id.Comment_task);


        Toolbar toolbar = findViewById(R.id.toolbar_task);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Задача");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        switchTimeTask = (Switch) findViewById(R.id.sw_timeTask_reminder);
        switchTimeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchTimeTask.isChecked())
                    openDialog();
                else
                if (!(switchTimeTask.isChecked())) {
                    switchTimeTask.setText("Напоминание");
                    time = " ";
                    Toast.makeText(
                            getApplicationContext(),
                            "Отключено",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


        fab = findViewById(R.id.fab_OK_task);
        //Нажатие на кнопку OK
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = -1;
                Intent intent = getIntent();
                strDate = intent.getStringExtra("message");

                db.insertTask(et_name.getText().toString(), time, et_coment.getText().toString(), strDate, "0");


                if(switchTimeTask.isChecked()) {

                    cal = Calendar.getInstance();

                    cal = DateService.setDate(strDate);
                    cal.set(Calendar.HOUR_OF_DAY, h);
                    cal.set(Calendar.MINUTE, m);
                    cal.set(Calendar.SECOND, 0);

                    i = db.lastIDTask();

                    startAlarm(cal, i);
                }

                Toast.makeText(
                        getApplicationContext(),
                        "Сохранено",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Toast.makeText(
                    getApplicationContext(),
                    "Cancel",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void openDialog() {
        // Get Current Time
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        h = hourOfDay;
                        m = minute;

                        if(hourOfDay < 10)
                            if(minute < 10) time = "0" + hourOfDay + ":0" + minute;
                            else time = "0" + hourOfDay + ":" + minute;
                        else
                            if(minute < 10) time = hourOfDay + ":0" + minute;
                            else time = hourOfDay + ":" + minute;
                        switchTimeTask.setText("Напоминания установлено на " + time);
                        Toast.makeText(getApplicationContext(),time, Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                switchTimeTask.setChecked(false);
            }
        });
        timePickerDialog.show();
    }

    private void startAlarm(Calendar c, int id){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiverTask.class);
        intent.putExtra("mes", et_name.getText().toString());
        intent.putExtra("idN", Integer.toString(id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm( int id){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiverTask.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
