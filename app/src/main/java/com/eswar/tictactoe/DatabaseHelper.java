package com.eswar.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private final static String tag = "tag";

    public static final String TABLE_NAME = "best_times";
    public static final String COLUMN_NAME_TIME = "time_taken";
    public static final String COLUMN_NAME_MOVES = "moves";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_RESULT = "result";
    public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
    private static final String COLUMN_NAME_ID = DatabaseHelper._ID;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                    DatabaseHelper._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_DIFFICULTY + " INTEGER," +
                    COLUMN_NAME_TIME + " REAL," +
                    COLUMN_NAME_DATE + " TEXT," +
                    COLUMN_NAME_RESULT + " INTEGER, " +
                    COLUMN_NAME_MOVES + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BestTimes.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void add(Row row){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TIME, row.getTime());
        values.put(COLUMN_NAME_DATE, row.getDate());
        values.put(COLUMN_NAME_RESULT, row.getResult());
        values.put(COLUMN_NAME_MOVES, row.getMoves());
        values.put(COLUMN_NAME_DIFFICULTY, row.getDifficulty());

        SQLiteDatabase database = this.getWritableDatabase();

        long newId = database.insert(TABLE_NAME, null, values);
        database.close();

        Log.d(tag, "Row might be added in DBH add(). newId = " + String.valueOf(newId));
    }

    public ArrayList<Row> getAllRows(){
        ArrayList itemIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_DATE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);


        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper._ID));
//            Log.d(tag, "itemId = " + String.valueOf(itemId));
            Row row = new Row(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
            row.setId(itemId);
            itemIds.add(row);
        }

        db.close();

        return itemIds;
    }
    public ArrayList<Row> getAllRows(int difficulty){
        ArrayList itemIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_DIFFICULTY + " = " + difficulty + " ORDER BY " + COLUMN_NAME_DATE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

//        Log.d(tag, "In DBH getAllRows(difficulty)");

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper._ID));
            Row row = new Row(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
            row.setId(itemId);
            itemIds.add(row);
        }
        cursor.close();
        db.close();
        return itemIds;
    }
    public long getBestTimeWin(int difficulty){
        SQLiteDatabase database = this.getReadableDatabase();
        String bestTimeQuery = "SELECT " + COLUMN_NAME_TIME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_DIFFICULTY + " = " + difficulty + " AND " + COLUMN_NAME_RESULT + " = " + AI.AI_LOST + " ORDER BY " + COLUMN_NAME_DATE + " DESC LIMIT 1";
        Cursor cursor = database.rawQuery(bestTimeQuery, null);

        boolean moved = cursor.moveToNext();
        long bestTimeWin = (long)-1;
        if(moved) {
            bestTimeWin = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_TIME));
        }
        cursor.close();
        database.close();
        return bestTimeWin;
    }
    public boolean delete(long id){
        SQLiteDatabase database = this.getWritableDatabase();

        return database.delete(TABLE_NAME, DatabaseHelper._ID + " = " + id, null) > 0;
    }
    public boolean delete(Row row){
        SQLiteDatabase database = this.getWritableDatabase();
//        String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ID + " = " + String.valueOf(id);
//        database.execSQL(deleteQuery);
        return database.delete(TABLE_NAME, DatabaseHelper._ID + " = " + row.getId(), null) > 0;
    }

}
