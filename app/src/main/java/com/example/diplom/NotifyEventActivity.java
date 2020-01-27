package com.example.diplom;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NotifyEventActivity extends AppCompatActivity {

    private TextView et_name, et_coment;
    Button btn;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DBHelper(this, 1);
        et_name = (TextView) findViewById(R.id.et_taskNameN);
        et_coment = (TextView) findViewById(R.id.Comment_taskN);


        Toolbar toolbar = findViewById(R.id.toolbar_taskN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Событие");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String id = ID.event_id;
        String[] task = db.viewEventID(id);
        String N = task[0] + " в " + task[2];
        et_name.setText(N);
        et_coment.setText(task[3]);

        btn = (Button) findViewById(R.id.btn_taskOK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase mdb = db.getWritableDatabase();
                mdb.execSQL("update Event set ch = '1' where _id = " + id);
                Toast.makeText(getApplicationContext(), "Завершена", Toast.LENGTH_SHORT).show();
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
}