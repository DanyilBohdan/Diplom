package com.example.diplom.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diplom.DBHelper;
import com.example.diplom.DateService;
import com.example.diplom.adapter.EventCursorAdapter;
import com.example.diplom.R;
import com.example.diplom.activity.EventActivity;
import com.example.diplom.activity.UpdateEventActivity;
import com.example.diplom.alarm.AlarmReceiverEvent;

import java.util.Calendar;

public class EventsFragment extends Fragment {

    final String LOG_TAG = "myLogs";

    private ListView ListEvent;
    private FloatingActionButton fab;
    private TextView EventDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String strDate;
    EventCursorAdapter adapter;
    private AlarmManager alarmManager;

    protected DBHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        db = new DBHelper(getActivity(), 1);
        /////DatePicker///
        EventDate = (TextView) getActivity().findViewById(R.id.toolbar_event_title);

        strDate = DateService.date;
        EventDate.setText(strDate);

        EventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        R.style.AppCompatAlertDialogStyle,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;

                if(month < 10)
                    if(dayOfMonth<10) strDate = "0" + dayOfMonth + ".0" + month + "." + year;
                    else strDate = dayOfMonth + ".0" + month + "." + year;
                else
                    if(dayOfMonth<10) strDate = "0" + dayOfMonth + "." + month + "." + year;
                    else strDate = dayOfMonth + "." + month + "." + year;

                DateService.date = strDate;

                EventDate.setText(strDate);

                viewEvent();
            }
        };

        ListEvent = (ListView) getActivity().findViewById(R.id.lv_event);
        viewEvent();

        //click ITEM
        ListEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Log.d(LOG_TAG,"itemClick: position = " + position + ", id = " + id);

                final Dialog dialogEventSetting = new Dialog(getActivity());
                dialogEventSetting.setContentView(R.layout.dialog_setting);

                Button btnEdit = (Button) dialogEventSetting.findViewById(R.id.setting_edit);
                Button btnDelete = (Button) dialogEventSetting.findViewById(R.id.setting_delete);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), UpdateEventActivity.class);
                        intent.putExtra("message", Long.toString(id));
                        startActivity(intent);

                        viewEvent();
                        dialogEventSetting.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelAlarm((int)id);
                        db.delEvent(id);
                        Toast.makeText(
                                getActivity(),
                                "Удалено",
                                Toast.LENGTH_SHORT
                        ).show();
                        viewEvent();
                        dialogEventSetting.dismiss();
                    }
                });
                dialogEventSetting.show();
            }
        });

        fab = getActivity().findViewById(R.id.fab_event);

        //Нажатие на кнопку добавить
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra("message", strDate);
                startActivity(intent);

                viewEvent();
            }
        });
    }

    private void viewEvent(){
        Cursor cursor = db.viewEvent(strDate);

//        // формируем столбцы сопоставления
//        String[] from = new String[] {"name_event", "time_event"};
//        int[] to = {R.id.cb_event, R.id.tv_event_time};

        // создаем адаптер и настраиваем список
        adapter = new EventCursorAdapter(getActivity(),  cursor, 0);
        ListEvent.setAdapter(adapter);
    }

    private void cancelAlarm( int id){
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiverEvent.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
