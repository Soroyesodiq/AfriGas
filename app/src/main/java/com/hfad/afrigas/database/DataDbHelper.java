package com.hfad.afrigas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hfad.afrigas.database.DataContract.OrderEntry;

public class DataDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "order.db";
    private static final int DATABASE_VERSION = 1;

    public DataDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ORDER_TABLE = "CREATE TABLE " + OrderEntry.TABLE_NAME + " ("
                + OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
               // + OrderEntry.COLUMN_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s', 'now')), "
                + OrderEntry.COLUMN_ORDER_ID + " TEXT, "
                + OrderEntry.COLUMN_ORDER_TIME + " TEXT, "
                + OrderEntry.COLUMN_ORDER_QUANTITY + " TEXT, "
                + OrderEntry.COLUMN_ORDER_NAME + " TEXT, "
                + OrderEntry.COLUMN_ORDER_HOSTEL + " TEXT, "
                + OrderEntry.COLUMN_ORDER_BLOCK + " TEXT, "
                + OrderEntry.COLUMN_ORDER_ROOM + " TEXT, "
                + OrderEntry.COLUMN_ORDER_PHONE_NO + " TEXT" + ");";
        db.execSQL(SQL_CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
