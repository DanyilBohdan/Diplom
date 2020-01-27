package com.example.diplom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.view.DragStartHelper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private Button diary;
    private CalendarView mCalendarView;
    private TextView date, tv_spending, tv_event, tv_task;
    private String strDate, diaryTxt = " ", dat;

    protected DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mCalendarView = (CalendarView) getActivity().findViewById(R.id.calendarView);
        date = (TextView) getActivity().findViewById(R.id.toolbar_title_calendar);
        tv_spending = (TextView) getActivity().findViewById(R.id.tv_spendingday);
        tv_event = (TextView) getActivity().findViewById(R.id.tv_event);
        tv_task = (TextView) getActivity().findViewById(R.id.tv_task);
        diary = (Button) getActivity().findViewById(R.id.btn_diary);


        db = new DBHelper(getActivity(), 1);

        //Today date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.yyyy");
        strDate = mdformat.format(calendar.getTime());
        date.setText(strDate);


/////////////////
        if(DS.date == " ") {
            DS.date = strDate;
        }

        dat = DS.date;
        date.setText(dat);
        viewSpSum(dat);
        viewEventCount(dat);
        viewTaskCount(dat);
        String parts[] = dat.split("\\.");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month - 1));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long milliTime = calendar.getTimeInMillis();
///////////////////
        mCalendarView.setDate(milliTime,true,true);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                if(month < 10)
                    if(dayOfMonth<10) strDate = "0" + dayOfMonth + ".0" + month + "." + year;
                    else strDate = dayOfMonth + ".0" + month + "." + year;
                else
                    if(dayOfMonth<10) strDate = "0" + dayOfMonth + "." + month + "." + year;
                    else strDate = dayOfMonth + "." + month + "." + year;
                date.setText(strDate);
                viewSpSum(strDate);
                viewEventCount(strDate);
                viewTaskCount(strDate);
                DS.date = strDate;
            }
        });


//DIARY BUTTON////
        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), DiaryActivity.class);
                if(db.checkKolDate(strDate) == 0) {
                    db.insertDate(strDate, diaryTxt);
                }
                startActivity(intent);


            }
        });
//DIARY BUTTON////
    }

   private void viewSpSum(String d){
        int sum = db.viewSpendingSum(d);
        tv_spending.setText(sum + "грн");
    }

    private void viewEventCount(String d){
        int kolEvent = db.checkKolEvent(d);
        if(kolEvent > 0) tv_event.setText("Событий " + kolEvent);
        else tv_event.setText("Событий нет");
    }

    private void viewTaskCount(String d){
        int kolTask = db.checkKolTask(d);
        if(kolTask > 0) tv_task.setText("Задач " + kolTask);
        else tv_task.setText("Задач нет");
    }

}
