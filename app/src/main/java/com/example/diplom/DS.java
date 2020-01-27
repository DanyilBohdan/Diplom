package com.example.diplom;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DS {
    public static String date = " ";

    /*public static void initDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.yyyy");
        date = mdformat.format(calendar.getTime());
    }*/

    public static Calendar setDate(String s){

        String parts[] = s.split("\\.");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, (month - 1));
        c.set(Calendar.DAY_OF_MONTH, day);

        return c;
    }

    public static String TodayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.yyyy");
        return mdformat.format(calendar.getTime());
    }

}
