package com.example.diplom.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diplom.DBHelper;
import com.example.diplom.DateService;
import com.example.diplom.R;

import java.util.Calendar;

public class SpendingFragment extends Fragment {

    final String LOG_TAG = "myLogs";
    private ListView ListSpending;
    private FloatingActionButton fab;
    private TextView SpendingDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String strDate;
    SimpleCursorAdapter adapter;

    protected DBHelper db;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spending, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        db = new DBHelper(getActivity(), 1);

        SpendingDate = (TextView) getActivity().findViewById(R.id.toolbar_spending_title);

        strDate = DateService.date;
        SpendingDate.setText(strDate);

        SpendingDate.setOnClickListener(new View.OnClickListener() {
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

                SpendingDate.setText(strDate);
                viewSpending();
            }
        };

        ListSpending = (ListView) getActivity().findViewById(R.id.lv_spending);
        viewSpending();
        ///Нажатие на пункт ListView
        ListSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Log.d(LOG_TAG,"itemClick: position = " + position + ", id = " + id);

                final Dialog dialogSpendingSetting = new Dialog(getActivity());
                dialogSpendingSetting.setContentView(R.layout.dialog_setting);

                Button btnEdit = (Button) dialogSpendingSetting.findViewById(R.id.setting_edit);
                Button btnDelete = (Button) dialogSpendingSetting.findViewById(R.id.setting_delete);

        //////////////////////////////////////////////////////////////////buttonEDIT
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSpendingSetting.dismiss();
                        final Dialog dialogSpendingAdd = new Dialog(getActivity());
                        dialogSpendingAdd.setContentView(R.layout.dialog_add_spending);

                        Button btnOk_sp = (Button) dialogSpendingAdd.findViewById(R.id.btnOK_sp);
                        Button btnCancel_sp = (Button) dialogSpendingAdd.findViewById(R.id.btnCansel_sp);

                        final TextView purpose_sp = (TextView) dialogSpendingAdd.findViewById(R.id.ed_purpose);
                        final TextView suma_sp = (TextView) dialogSpendingAdd.findViewById(R.id.ed_suma);

                        String []sp = db.viewSpendingID(id);
                        purpose_sp.setText(sp[0]);
                        suma_sp.setText(sp[1]);

                        btnOk_sp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                db.updateSpending(id,purpose_sp.getText().toString(), suma_sp.getText().toString());

                                Toast.makeText(
                                        getActivity(),
                                        "Сохранено",
                                        Toast.LENGTH_SHORT
                                ).show();
                                viewSpending();
                                dialogSpendingAdd.dismiss();
                            }
                        });
                        btnCancel_sp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogSpendingAdd.dismiss();
                            }
                        });
                        dialogSpendingAdd.show();
                    }
                });
///////////////////////////////////////////////////////////////////////////buttonEDIT

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.delSpending(id);
                        Toast.makeText(
                                getActivity(),
                                "Удалено",
                                Toast.LENGTH_SHORT
                        ).show();
                        viewSpending();
                        dialogSpendingSetting.dismiss();
                    }
                });
                dialogSpendingSetting.show();
            }
        });


        fab = getActivity().findViewById(R.id.fab_spending);
//////Нажатие на кнопку добавить////////////////////////////////////////////////////////////////////////////////////////
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogSpendingAdd = new Dialog(getActivity());
                dialogSpendingAdd.setContentView(R.layout.dialog_add_spending);

                Button btnOk_sp = (Button) dialogSpendingAdd.findViewById(R.id.btnOK_sp);
                Button btnCancel_sp = (Button) dialogSpendingAdd.findViewById(R.id.btnCansel_sp);

                final TextView purpose_sp = (TextView) dialogSpendingAdd.findViewById(R.id.ed_purpose);
                final TextView suma_sp = (TextView) dialogSpendingAdd.findViewById(R.id.ed_suma);

                btnOk_sp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(
                                getActivity(),
                                "Сохранено",
                                Toast.LENGTH_SHORT
                        ).show();

                        db.insertSpending(purpose_sp.getText().toString(), suma_sp.getText().toString(), strDate);

                        viewSpending();
                        dialogSpendingAdd.dismiss();
                    }
                });
                btnCancel_sp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSpendingAdd.dismiss();
                    }
                });
                dialogSpendingAdd.show();
            }
        });
//////////////////////////////////////////////////////////////////
    }

    private void viewSpending() {

        Cursor cursor = db.viewSpending(strDate);

        // формируем столбцы сопоставления
        String[] from = new String[] {"purpose", "suma"};
        int[] to = {R.id.tv_spending_name, R.id.tv_spending_money};

        // создаем адаптер и настраиваем список
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_spending, cursor, from, to, 0);
        ListSpending.setAdapter(adapter);

    }
}
