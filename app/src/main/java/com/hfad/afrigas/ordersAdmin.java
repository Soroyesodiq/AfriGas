package com.hfad.afrigas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hfad.afrigas.Model.Products;
import com.hfad.afrigas.ViewHolder.ProductViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ordersAdmin extends AppCompatActivity implements NotifyDialog.NotifyDialogListener{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyCcmxvVAuzUKhyWLQQ9IxDvvRS7GpeLaH0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_admin);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Recent Orders");

        progressBar = findViewById(R.id.feed_loading);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        String dateHostel = model.getOrderTime() + ", " + model.getOrderHostel() + " Hostel.";
                        String gasQuantityName = model.getOrderQuantity() + " of Gas by " + model.getCustomerName();

                        holder.adminDateHostel.setText(dateHostel);
                        holder.adminGasQuantityName.setText(gasQuantityName);
                        holder.adminOrderStatus.setText(model.getStatus());
                        if (model.getStatus().equals("ACCEPTED")) {
                            holder.adminOrderStatus.setTextColor(getResources().getColor(R.color.green));
                        }
                        if (model.getStatus().equals("DELIVERED")) {
                            holder.adminOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ordersAdmin.this, eachUserOrder.class);
                                intent.putExtra("id", model.getOrderId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        progressBar.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent = new Intent(ordersAdmin.this, AdminInfo.class);
                startActivity(intent);
                return true;
            case R.id.action_feedbacks:
                Intent intent2 = new Intent(ordersAdmin.this, FeedbackAdmin.class);
                startActivity(intent2);
                return true;
            case R.id.action_notify:
                openDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.show(getSupportFragmentManager(), "notify dialog");
    }

    @Override
    public void applyTexts(String title, String content) {
        prepareNotification(title, content);
    }

    private void prepareNotification(String title, String content) {
        /*String myDeviceToken = FirebaseInstanceId.getInstance().getToken();
        myDeviceToken = myDeviceToken.substring(0,4);*/
       // TOPIC = "/topics/" + myDeviceToken; //topic has to match what the receiver subscribed to
        TOPIC = "/topics/userABC";
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE = content;
        //Toast.makeText(ordersAdmin.this, myDeviceToken, Toast.LENGTH_LONG).show();
        //FirebaseMessaging.getInstance().subscribeToTopic("userABC");
        //FirebaseMessaging.getInstance().subscribeToTopic(myDeviceToken);

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        Toast.makeText(ordersAdmin.this, "Prepare Notification", Toast.LENGTH_LONG).show();
            sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        Toast.makeText(ordersAdmin.this, "Send Notification success", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ordersAdmin.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
