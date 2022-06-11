package com.example.diplom.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.diplom.R;
import com.example.diplom.adapter.TaskCursorAdapter;
import com.example.diplom.activity.TaskActivity;
import com.example.diplom.activity.UpdateTaskActivity;
import com.example.diplom.alarm.AlarmReceiverTask;

import java.util.Calendar;

public class TasksFragment extends Fragment {

    final String LOG_TAG = "myLogs";

    private ListView ListTask;
    private FloatingActionButton fab;
    private TextView TaskDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenerTransfer;
    private String strDate;
    private String dStr;
    TaskCursorAdapter adapter;
    private AlarmManager alarmManager;

    protected DBHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        db = new DBHelper(getActivity(), 1);

        TaskDate = (TextView) getActivity().findViewById(R.id.toolbar_task_title);

        strDate = DateService.date;
        TaskDate.setText(strDate);

        TaskDate.setOnClickListener(new View.OnClickListener() {
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
                TaskDate.setText(strDate);
                viewTask();
            }
        };

        ListTask = (ListView) getActivity().findViewById(R.id.lv_task);
        viewTask();

        ListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                final Dialog dialogTaskSetting = new Dialog(getActivity());
                dialogTaskSetting.setContentView(R.layout.dialog_setting_task);

                Button btnEdit = (Button) dialogTaskSetting.findViewById(R.id.setting_edit_task);
                Button btnDelete = (Button) dialogTaskSetting.findViewById(R.id.setting_delete_task);
                Button btnTransfer = (Button) dialogTaskSetting.findViewById(R.id.setting_transfer_date);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), UpdateTaskActivity.class);
                        intent.putExtra("message", Long.toString(id));
                        startActivity(intent);

                        viewTask();
                        dialogTaskSetting.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancelAlarm(Integer.valueOf((int) id));
                        db.delTask(id);
                        Toast.makeText(
                                getActivity(),
                                "Удалено",
                                Toast.LENGTH_SHORT
                        ).show();
                        viewTask();
                        dialogTaskSetting.dismiss();
                    }
                });
                btnTransfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar c = Calendar.getInstance();
                        int y = c.get(Calendar.YEAR);
                        int m = c.get(Calendar.MONTH);
                        int d = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dial = new DatePickerDialog(
                                getActivity(),
                                R.style.AppCompatAlertDialogStyle,
                                mDateSetListenerTransfer,
                                y, m, d);
                        dial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dial.show();

                        mDateSetListenerTransfer = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int ye, int mon, int day) {
                                mon = mon +1;
                                if(mon < 10)
                                    if(day<10) dStr = "0" + day + ".0" + mon + "." + ye;
                                    else dStr = day + ".0" + mon + "." + ye;
                                else
                                    if(day<10) dStr = "0" + day + "." + mon + "." + ye;
                                    else dStr = day + "." + mon + "." + ye;
                                SQLiteDatabase mdb = db.getWritableDatabase();
                                mdb.execSQL("update Task set date = '"+ dStr +"' where _id = " + id);
                                mdb.execSQL("update Task set date = '"+ dStr +"' where _id = " + id);
                                Toast.makeText(getActivity(), dStr, Toast.LENGTH_SHORT).show();
                                viewTask();
                            }
                        };
                        dialogTaskSetting.dismiss();
                    }
                });
                dialogTaskSetting.show();
            }
        });


        fab = getActivity().findViewById(R.id.fab_task);

        //Нажатие на кнопку добавить
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("message", strDate);
                startActivity(intent);

                viewTask();
            }
        });
    }

    private void viewTask() {
        Cursor cursor = db.viewTask(strDate);

//        // формируем столбцы сопоставления
//        String[] from = {"name_task"};
//        int[] to = {R.id.cb_task};

        // создаем адаптер и настраиваем список
        adapter = new TaskCursorAdapter(getActivity(), cursor, 0);
        ListTask.setAdapter(adapter);
    }

    private void cancelAlarm( int id){
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiverTask.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Напоминание отключено", Toast.LENGTH_SHORT).show();
    }

}
