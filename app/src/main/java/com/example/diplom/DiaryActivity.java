package com.example.diplom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class DiaryActivity extends AppCompatActivity {

    private String textD;
    protected DBHelper db;
    private EditText diary;
    private String strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        diary = (EditText) findViewById(R.id.Edit_diary);
        strDate = DS.date;
        db = new DBHelper(this, 1);

        diary.setText(db.viewDiary(strDate));

        Toolbar toolbar = findViewById(R.id.toolbar_diary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Дневник");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        textD = diary.getText().toString();

        int id = item.getItemId();
        if (id == android.R.id.home) {

                db.updateDiary(strDate, textD);
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
