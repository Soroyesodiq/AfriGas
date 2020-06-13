package com.hfad.afrigas.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.hfad.afrigas.database.DataContract.OrderEntry;

public class DataProvider extends ContentProvider {
    public static final String LOG_TAG = DataProvider.class.getSimpleName();
    private DataDbHelper mDbHelper;

    private static final int ORDER = 100;

    private static final int ORDER_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_ORDERS, ORDER);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_ORDERS+"/#", ORDER_ID);
        }
    @Override
     public boolean onCreate() {
        mDbHelper = new DataDbHelper(getContext());
        return true;
     }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case ORDER:
                cursor = database.query(OrderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ORDER_ID:
                selection = OrderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(OrderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                return insertOrder(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertOrder(Uri uri, ContentValues values) {
        String orderId = values.getAsString(OrderEntry.COLUMN_ORDER_ID);
        if (orderId == null) {
            throw new IllegalArgumentException("Requires an id");
        }
        String orderTime = values.getAsString(OrderEntry.COLUMN_ORDER_TIME);
        if (orderTime == null) {
            throw new IllegalArgumentException("Requires a date");
        }
        String orderQuantity = values.getAsString(OrderEntry.COLUMN_ORDER_QUANTITY);
        if (orderQuantity == null) {
            throw new IllegalArgumentException("Requires a quantity");
        }
        String orderName = values.getAsString(OrderEntry.COLUMN_ORDER_NAME);
        if (orderName == null) {
            throw new IllegalArgumentException("Requires a name");
        }
        String orderHostel = values.getAsString(OrderEntry.COLUMN_ORDER_HOSTEL);
        if (orderHostel == null) {
            throw new IllegalArgumentException("Requires a hostel");
        }
        String orderBlock = values.getAsString(OrderEntry.COLUMN_ORDER_BLOCK);
        if (orderBlock == null) {
            throw new IllegalArgumentException("Requires a block");
        }
        String orderRoom = values.getAsString(OrderEntry.COLUMN_ORDER_ROOM);
        if (orderRoom == null) {
            throw new IllegalArgumentException("Requires a room");
        }
        String orderPhoneNo = values.getAsString(OrderEntry.COLUMN_ORDER_PHONE_NO);
        if (orderPhoneNo == null) {
            throw new IllegalArgumentException("Requires a phone no");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(OrderEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case ORDER_ID:
                selection = OrderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(OrderEntry.COLUMN_ORDER_ID)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_ID);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a id");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_TIME)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_TIME);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a time");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_QUANTITY)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_QUANTITY);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a quantity");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_NAME)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_NAME);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a name");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_HOSTEL)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_HOSTEL);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a hostel");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_BLOCK)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_BLOCK);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a block");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_ROOM)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_ROOM);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a room");
            }
        }
        if (values.containsKey(OrderEntry.COLUMN_ORDER_PHONE_NO)) {
            String title = values.getAsString(OrderEntry.COLUMN_ORDER_PHONE_NO);
            if (title == null) {
                throw new IllegalArgumentException("Order requires a phone no");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(OrderEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                rowsDeleted = database.delete(OrderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ORDER_ID:
                selection = OrderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(OrderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDER:
                return OrderEntry.CONTENT_LIST_TYPE;
            case ORDER_ID:
                return OrderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
