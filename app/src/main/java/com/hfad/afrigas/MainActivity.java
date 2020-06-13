package com.hfad.afrigas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hfad.afrigas.Model.Info;
import com.hfad.afrigas.database.DataContract.OrderEntry;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView infoBoxUp, gasPrice, adBox, infoBoxDown;
    private Button doneButton;
    private Spinner hostelSpinner;
    private Spinner quantitySpinner;
    private EditText phoneNo;
    private EditText blockNo;
    private EditText roomNo;
    private EditText customerName;
    private String orderSummary;
    private String[] hostelArray;
    private String[] quantityArray;
    private int price;
    private String orderDateString;
    private String orderTimeString;
    // private String orderDateTime;
    private String orderId;
    private String generalToken;
    private String deviceToken;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;
    private Info info;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Token = "tokenKey";
    public static final String GeneralToken = "genTokenKey";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AfriGas: Uber for Gas");
        setSupportActionBar(toolbar);

        RootRef = FirebaseDatabase.getInstance().getReference();
        loadingBar = new ProgressDialog(this);
        phoneNo = findViewById(R.id.phone_no);
        blockNo = findViewById(R.id.block_no);
        roomNo = findViewById(R.id.room_no);
        customerName = findViewById(R.id.name);
        infoBoxUp = findViewById(R.id.info_box_up);
        infoBoxDown = findViewById(R.id.info_box);
        adBox = findViewById(R.id.ad_box);
        gasPrice = findViewById(R.id.gasprice);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Token)) {
            deviceToken = sharedPreferences.getString(Token, "");
        } else {
            Random r = new Random();
            int randomNumbr = r.nextInt(100000);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            deviceToken = String.valueOf(randomNumbr);
            editor.putString(Token, deviceToken);
            FirebaseMessaging.getInstance().subscribeToTopic(deviceToken);

            generalToken = "userABC";
            editor.putString(GeneralToken, generalToken);
            FirebaseMessaging.getInstance().subscribeToTopic(generalToken);

            editor.commit();
        }


        Toast.makeText(MainActivity.this, deviceToken, Toast.LENGTH_SHORT).show();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.textView);
        ImageView profileImageView = headerView.findViewById(R.id.imageView);
        userNameTextView.setText("Afri Gas");*/


        hostelSpinner = findViewById(R.id.hostel_spinner);
        hostelArray = getResources().getStringArray(R.array.hostel_array);
        ArrayAdapter<String> hostelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hostelArray);
        hostelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hostelSpinner.setAdapter(hostelAdapter);

        quantitySpinner = findViewById(R.id.quantity_spinner);
        quantityArray = getResources().getStringArray(R.array.quantity_array);
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quantityArray);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);


        RootRef.child("Admin").child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    info = dataSnapshot.getValue(Info.class);
                    gasPrice.setText(info.getPrice());
                    adBox.setText(info.getAd());
                    infoBoxUp.setText(info.getShortInfo());
                    infoBoxDown.setText(info.getLongInfo());
                    loadingBar.dismiss();

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        doneButton = findViewById(R.id.done_Button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((blockNo.getText().toString().isEmpty()) || (roomNo.getText().toString().isEmpty()) ||
                        (phoneNo.getText().toString().isEmpty()) || (customerName.getText().toString().isEmpty())) {
                    Toast.makeText(MainActivity.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    phoneNo.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    blockNo.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    roomNo.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    customerName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    showConfirmOrderDialog();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        //Cancelling write operation
        RootRef.child("Orders").getDatabase().purgeOutstandingWrites();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the users action in the nav drawer
        switch (id) {
            case R.id.nav_order:
                Intent userOrder = new Intent(MainActivity.this, UserOrder.class);
                startActivity(userOrder);

                break;
            case R.id.nav_notificaton:
                Intent notification = new Intent(MainActivity.this, UserNotification.class);
                startActivity(notification);

                break;
            case R.id.nav_contact:
                Intent contact = new Intent(MainActivity.this, ContactUs2.class);
                startActivity(contact);

                break;
            case R.id.nav_about:
                Intent about = new Intent(MainActivity.this, About.class);
                startActivity(about);
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_feedback:
                //Change the feedback destination class
                Intent userFeedback = new Intent(MainActivity.this, UserFeedback.class);
                startActivity(userFeedback);

                break;

            case R.id.nav_admin_agent:
                //Change the feedback destination class
                Intent admin_agent = new Intent(MainActivity.this, ordersAdmin.class);
                startActivity(admin_agent);

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showConfirmOrderDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("please Confirm Your Order");
        String quantityChosed;
        quantityChosed = quantitySpinner.getSelectedItem().toString();
        String hostelChosed;
        hostelChosed = hostelSpinner.getSelectedItem().toString();
        /*price = 300;
        String totalPrice;
        switch (quantityChosed)
        {
            case "1.0Kg" :
                totalPrice = String.valueOf(price * 1);
                break;
            case "1.5Kg" :
                totalPrice = String.valueOf(price * 1.5);
                break;
            case "2.0Kg" :
                totalPrice = String.valueOf(price * 2);
                break;
            case "2.5Kg" :
                totalPrice = String.valueOf(price * 2.5);
                break;
            case "3.0Kg" :
                totalPrice = String.valueOf(price * 3);
                break;
            case "3.5Kg" :
                totalPrice = String.valueOf(price * 3.5);
                break;
            case "4.0Kg" :
                totalPrice = String.valueOf(price * 4);
                break;
            case "4.5Kg" :
                totalPrice = String.valueOf(price * 4.5);
                break;
            case "5.0Kg" :
                totalPrice = String.valueOf(price * 5);
                break;
            case "5.5Kg" :
                totalPrice = String.valueOf(price * 5.5);
                break;
            case "6.0Kg" :
                totalPrice = String.valueOf(price * 6);
                break;
            case "Others" :
                totalPrice = "Others";
                break;
            default:
                totalPrice = "Not specified";

        }*/

        orderSummary = (
                "Quantity = " + quantityChosed + "\n" +
                        "Hostel = " + hostelChosed + "\n" +
                        "Name = " + customerName.getText().toString().trim() + "\n" +
                        "Block No = " + blockNo.getText().toString().trim() + "\n" +
                        "Room No = " + roomNo.getText().toString().trim() + "\n" +
                        "Phone No = " + phoneNo.getText().toString().trim()
        );
        builder.setMessage(orderSummary);
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(MainActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                loadingBar.setTitle("Loading");
                loadingBar.setMessage("Please wait... ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                acceptOrder();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void acceptOrder() {
        Calendar orderCalender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        orderDateString = currentDate.format(orderCalender.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        orderTimeString = currentTime.format(orderCalender.getTime());
        String orderDateTime = orderDateString + orderTimeString;
        orderId = orderDateTime + (phoneNo.getText().toString().trim());

        HashMap<String, Object> userdataMap = new HashMap<>();

        userdataMap.put("orderId", orderId);
        userdataMap.put("orderTime", orderDateString);
        userdataMap.put("orderQuantity", quantitySpinner.getSelectedItem().toString());
        userdataMap.put("orderHostel", hostelSpinner.getSelectedItem().toString());
        userdataMap.put("customerName", customerName.getText().toString().trim());
        userdataMap.put("orderBlock", blockNo.getText().toString().trim());
        userdataMap.put("orderRoom", roomNo.getText().toString().trim());
        userdataMap.put("orderPhoneNo", phoneNo.getText().toString().trim());
        userdataMap.put("status", "PENDING");
        userdataMap.put("bFillImage", "null");
        userdataMap.put("aFillImage", "null");
        userdataMap.put("token", deviceToken);

        /*deviceToken = sharedPreferences.getString(Token, "null");
        if(deviceToken.equals("null")) {
            Random r = new Random();
            int randomNumbr = r.nextInt(100000);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            deviceToken = String.valueOf(randomNumbr);
            editor.putString(Token, deviceToken);
            FirebaseMessaging.getInstance().subscribeToTopic(deviceToken);
            userdataMap.put("token", deviceToken);
        } else {
            userdataMap.put("token", deviceToken);
        }*/

        // RootRef.child("Orders").child(orderId).updateChildren(userdataMap)
        RootRef.child("Orders").child(orderId).setValue(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Firebase Saving successfull", Toast.LENGTH_SHORT).show();
                            // loadingBar.dismiss();

                            // Now Saving to local database

                            ContentValues values = new ContentValues();
                            values.put(OrderEntry.COLUMN_ORDER_ID, orderId);
                            values.put(OrderEntry.COLUMN_ORDER_TIME, orderDateString);
                            values.put(OrderEntry.COLUMN_ORDER_QUANTITY, quantitySpinner.getSelectedItem().toString());
                            values.put(OrderEntry.COLUMN_ORDER_HOSTEL, hostelSpinner.getSelectedItem().toString());
                            values.put(OrderEntry.COLUMN_ORDER_NAME, customerName.getText().toString().trim());
                            values.put(OrderEntry.COLUMN_ORDER_BLOCK, blockNo.getText().toString().trim());
                            values.put(OrderEntry.COLUMN_ORDER_ROOM, roomNo.getText().toString().trim());
                            values.put(OrderEntry.COLUMN_ORDER_PHONE_NO, phoneNo.getText().toString().trim());

                            Uri newUri = getContentResolver().insert(OrderEntry.CONTENT_URI, values);

                            if (newUri == null) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Unable to save order to database, but saved to firebase",
                                        Toast.LENGTH_SHORT).show();

                                //Cancel loading bar here
                                // Cancel firebase save here

                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Save successfully to local database, go to order to view your order",
                                        Toast.LENGTH_SHORT).show();

                                Intent userOrder = new Intent(MainActivity.this, UserOrder.class);
                                startActivity(userOrder);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Firebase Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
