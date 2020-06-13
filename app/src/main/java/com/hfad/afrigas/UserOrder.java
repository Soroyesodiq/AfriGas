package com.hfad.afrigas;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.afrigas.database.DataContract.OrderEntry;

import java.util.Collections;

public class UserOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int DATABASE_LOADER = 0;
    DataCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

         getSupportActionBar().setHomeButtonEnabled(true);
         setTitle("My Orders");

        ListView dataListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        dataListView.setEmptyView(emptyView);
        //Collections.reverse(dataListView);


        mCursorAdapter = new DataCursorAdapter(this, null);
        dataListView.setAdapter(mCursorAdapter);


        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(UserOrder.this, eachUserOrder.class);
                Uri currentDataUri = ContentUris.withAppendedId(OrderEntry.CONTENT_URI, id);
                intent.setData(currentDataUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(DATABASE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                OrderEntry._ID,
                OrderEntry.COLUMN_ORDER_TIME,
                OrderEntry.COLUMN_ORDER_QUANTITY,
                OrderEntry.COLUMN_ORDER_HOSTEL,
                OrderEntry.COLUMN_ORDER_NAME
        };

        return new CursorLoader(this,
                OrderEntry.CONTENT_URI,
                projection,
                null,
                null,
                OrderEntry._ID +" desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAllOrder();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllOrder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all orders");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int rowsDeleted = getContentResolver().delete(OrderEntry.CONTENT_URI, null, null);
                if (rowsDeleted == 0) {
                    Toast.makeText(UserOrder.this, "Failed to delete all note", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserOrder.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
