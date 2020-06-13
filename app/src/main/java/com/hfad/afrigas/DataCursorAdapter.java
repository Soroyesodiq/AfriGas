package com.hfad.afrigas;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.hfad.afrigas.database.DataContract.OrderEntry;

public class DataCursorAdapter extends CursorAdapter {

    public DataCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView timeAndHostelTextView = view.findViewById(R.id.date_hostel);
        TextView quantityAndNameTextView = view.findViewById(R.id.gasQuantity_name);

        int timeColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_TIME);
        int quantityColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_QUANTITY);
        int nameColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_NAME);
        int hostelColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_HOSTEL);

        String gasTime = cursor.getString(timeColumnIndex);
        String gasQuantity = cursor.getString(quantityColumnIndex);
        String gasName = cursor.getString(nameColumnIndex);
        String gasHostel = cursor.getString(hostelColumnIndex);

        timeAndHostelTextView.setText(gasTime + ", " + gasHostel + " Hostel.");
        quantityAndNameTextView.setText(gasQuantity + " of Gas by " + gasName);

    }
}
