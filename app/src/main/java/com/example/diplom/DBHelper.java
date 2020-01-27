package com.example.diplom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.print.PrinterId;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static String LOG_TAG = "MyLog";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoCount";

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG_TAG,"create table Date");
        db.execSQL("CREATE TABLE Date " +
                "( " +
                "date TEXT PRIMARY KEY, " +
                "diary TEXT " +
                ")");

        Log.d(LOG_TAG,"create table Spending");
        db.execSQL("CREATE TABLE Spending " +
                "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "purpose TEXT," +
                "suma TEXT," +
                "date TEXT" +
                " )");

        Log.d(LOG_TAG,"create table Event");
        db.execSQL("CREATE TABLE Event " +
                "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name_event TEXT," +
                "reminder_event TEXT," +
                "time_event TEXT," +
                "comm_event TEXT," +
                "ch TEXT," +
                "date TEXT" +
                " )");

        Log.d(LOG_TAG,"create table Task");
        db.execSQL("CREATE TABLE Task " +
                "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name_task TEXT," +
                "reminder_task TEXT," +
                "comm_task TEXT," +
                "ch TEXT," +
                "date TEXT" +
                " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Date");
        onCreate(db);
    }

    //create method to insert data
    public boolean insertDate(String dat, String diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", dat);
        contentValues.put("diary", diary);

        long result = db.insert("Date", null, contentValues);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean insertSpending(String purp, String suma, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("purpose", purp);
        contentValues.put("suma", suma);
        contentValues.put("date", date);

        long result = db.insert("Spending", null, contentValues);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean insertEvent(String name, String reminder, String time, String comm, String date, String i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name_event", name);
        contentValues.put("reminder_event", reminder);
        contentValues.put("time_event", time);
        contentValues.put("comm_event", comm);
        contentValues.put("date", date);
        contentValues.put("ch", i);


        long result = db.insert("Event", null, contentValues);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean insertTask(String name, String reminder, String comm, String date, String i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name_task", name);
        contentValues.put("reminder_task", reminder);
        contentValues.put("comm_task", comm);
        contentValues.put("date", date);
        contentValues.put("ch", i);

        long result = db.insert("Task", null, contentValues);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public Cursor viewSpending(String dat){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, purpose, suma  FROM Spending where date = '" + dat +"'", null);
        return cursor;
    }

    public Cursor viewEvent(String dat){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, name_event, reminder_event, time_event, comm_event, ch FROM Event where date = '" + dat +"'", null);
        return cursor;
    }

    public Cursor viewTask(String dat){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, name_task, reminder_task, comm_task, ch FROM Task where date = '" + dat +"'", null);
        return cursor;
    }

    public String viewDiary(String dat){
        String d = " ";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT diary FROM Date where date = '" + dat +"'", null);
        if(cursor.moveToFirst())
            d = cursor.getString(cursor.getColumnIndex("diary"));
        return d;
    }

    public int checkKolDate(String dat){
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Date where date = '" + dat +"'", null);
        return cursor.getCount();
    }

    public int checkKolEvent(String dat){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Event where date = '" + dat +"'", null);
        return cursor.getCount();
    }

    public int checkKolTask(String dat){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Task where date = '" + dat +"'", null);
        return cursor.getCount();
    }

    public void delEvent(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Event", "_id" + " = " + id, null);
    }

    public void delTask(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Task", "_id" + " = " + id, null);
    }

    public void delSpending(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Spending", "_id" + " = " + id, null);
    }

    public int viewSpendingSum(String dat){
        int total = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(suma) as Total FROM Spending where date = '" + dat +"'", null);
        if(cursor.moveToFirst())
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        return total;
    }

    public boolean updateDiary(String dat, String diary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("diary", diary);

        long result = db.update("Date", contentValues, "date = '" + dat + "'", null);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean updateSpending(long id, String purp, String suma){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("purpose", purp);
        contentValues.put("suma", suma);

        long result = db.update("Spending", contentValues, "_id = '" + id + "'", null);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean updateEvent(String id, String name, String reminder, String time, String comm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name_event", name);
        contentValues.put("reminder_event", reminder);
        contentValues.put("time_event", time);
        contentValues.put("comm_event", comm);

        long result = db.update("Event", contentValues, "_id = '" + id + "'", null);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public boolean updateTask(String id, String name, String reminder, String comm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name_task", name);
        contentValues.put("reminder_task", reminder);
        contentValues.put("comm_task", comm);

        long result = db.update("Task", contentValues, "_id = '" + id + "'", null);

        return result != 1;// if result = -1 data dosen`t insert
    }

    public String[] viewSpendingID(long id){
        String []str = new String[2];
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT purpose, suma FROM Spending where _id = '" + id +"'", null);
        if(cursor.moveToFirst()) {
            str[0] = cursor.getString(cursor.getColumnIndex("purpose"));
            str[1] = cursor.getString(cursor.getColumnIndex("suma"));
        }
        return str;
    }

    public String[] viewEventID(String id){
        String []str = new String[4];
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name_event, reminder_event, time_event, comm_event FROM Event where _id = '" + id +"'", null);
        if(cursor.moveToFirst()) {
            str[0] = cursor.getString(cursor.getColumnIndex("name_event"));
            str[1] = cursor.getString(cursor.getColumnIndex("reminder_event"));
            str[2] = cursor.getString(cursor.getColumnIndex("time_event"));
            str[3] = cursor.getString(cursor.getColumnIndex("comm_event"));
        }
        return str;
    }

    public String[] viewTaskID(String id){
        String []str = new String[3];
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name_task, reminder_task, comm_task FROM Task where _id = '" + id +"'", null);
        if(cursor.moveToFirst()) {
            str[0] = cursor.getString(cursor.getColumnIndex("name_task"));
            str[1] = cursor.getString(cursor.getColumnIndex("reminder_task"));
            str[2] = cursor.getString(cursor.getColumnIndex("comm_task"));
        }
        return str;
    }

    public int lastIDTask() {
        int i = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(_id) FROM Task", null);
        if (cursor.moveToFirst())
            i = cursor.getInt(cursor.getColumnIndex("MAX(_id)"));
        return i;
    }

    public int lastIDEvent() {
        int i = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(_id) FROM Event", null);
        if (cursor.moveToFirst())
            i = cursor.getInt(cursor.getColumnIndex("MAX(_id)"));
        return i;
    }

    public void delEventAll(String dat) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Event", "date" + " = " + dat, null);
    }

    public void delTaskAll(String dat) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Task", "date" + " = " + dat, null);
    }

    public void delSpendingAll(String dat) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Spending", "date" + " = " + dat, null);
    }

}
