package com.hfad.afrigas;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.afrigas.Model.Products;
import com.hfad.afrigas.database.DataContract.OrderEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class eachUserOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NotifyDialog.NotifyDialogListener{
    private TextView orderStatus;
    private Button cancelOrder, orderMessageUs;
    private TextView orderSummaryTextview;
    private ImageView beforeFill;
    private ImageView afterFill;
    private ImageView afterCamera;
    private ImageView beforeCamera;
    private LinearLayout cancelMessage;
    private Uri mCurrentOrderUri;
    private String orderSummary;
    private String orderId;
    private TextView beforeFillTextView;
    private TextView afterFillTextView;
    private Button beforeReload, afterReload;
    private Products products;
    private String firebaseStatus;
    private ProgressDialog loadingBar;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private Uri ImageUri2;
    private byte[] fileInBytes;
    private byte[] fileInBytes2;
    private String FirstImageUrl;
    private String SecondImageUrl;
    private String proofFirstSecond = "first";
    private StorageReference OrderImagesRef;
    private DatabaseReference RootRef;
    private String UserDeviceToken = "null";

    private static final int EXISTING_DATABASE_LOADER = 0;

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyCcmxvVAuzUKhyWLQQ9IxDvvRS7GpeLaH0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_user_order);
        loadingBar = new ProgressDialog(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Order Details");
        //Firebase Image Storage Instance
        OrderImagesRef = FirebaseStorage.getInstance().getReference().child("Proof Images");

        cancelOrder = findViewById(R.id.order_cancel_button);
        orderMessageUs = findViewById(R.id.order_message_us);
        cancelMessage = findViewById(R.id.cancel_message_layout);
        orderStatus = findViewById(R.id.order_status);
        beforeFillTextView = findViewById(R.id.bFillStatus);
        afterFillTextView = findViewById(R.id.aFillStatus);
        beforeFill = findViewById(R.id.before_fill);
        afterFill = findViewById(R.id.after_fill);
        beforeCamera = findViewById(R.id.before_camera);
        afterCamera = findViewById(R.id.after_camera);
        orderSummaryTextview = findViewById(R.id.order_summary);
        beforeReload = findViewById(R.id.before_reload);
        afterReload = findViewById(R.id.after_reload);


        beforeReload.setVisibility(View.GONE);
        afterReload.setVisibility(View.GONE);

        Intent intent = getIntent();
        mCurrentOrderUri = intent.getData();

        if(mCurrentOrderUri == null) {
            //Admin part should show in the activity
            orderId = getIntent().getStringExtra("id");
            //Set both buttons invisible
            /*cancelOrder.setVisibility(View.INVISIBLE);
            orderMessageUs.setVisibility(View.INVISIBLE);*/
            cancelMessage.setVisibility(View.GONE);
            OpenAdminPart();
        } else {
            //Remove later
            Toast.makeText(eachUserOrder.this, "Now in user aspect", Toast.LENGTH_SHORT).show();
            //User part should show in the activity
            beforeCamera.setVisibility(View.INVISIBLE);
            afterCamera.setVisibility(View.INVISIBLE);
            invalidateOptionsMenu();
            getLoaderManager().initLoader(EXISTING_DATABASE_LOADER, null, this);
        }

        beforeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderStatus.getText().equals("ORDER STATUS: LOADING...")) {
                    Toast.makeText(eachUserOrder.this, "Order still loading", Toast.LENGTH_SHORT).show();
                } else {
                    proofFirstSecond = "first";
                    OpenGallery();
                }

            }
        });

        afterCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderStatus.getText().equals("ORDER STATUS: LOADING...")) {
                    Toast.makeText(eachUserOrder.this, "Order still loading", Toast.LENGTH_SHORT).show();
                } else {
                    proofFirstSecond = "second";
                    OpenGallery();
                }
            }
        });

        beforeReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(eachUserOrder.this, "Reload working", Toast.LENGTH_SHORT).show();
                beforeFill.setImageResource(0);
                beforeFillTextView.setVisibility(View.VISIBLE);
                beforeReload.setVisibility(View.INVISIBLE);

                final long ONE_MEGABYTE = 1024 * 1024;
                OrderImagesRef.child(products.getbFillImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        beforeFill.setImageBitmap(Bitmap.createScaledBitmap(bitmap, beforeFill.getWidth(),500, false));
                        beforeFillTextView.setVisibility(View.INVISIBLE);
                        beforeReload.setVisibility(View.VISIBLE);
                        Toast.makeText(eachUserOrder.this, "Before image loaded", Toast.LENGTH_SHORT).show();

                    }
                });
                /*Picasso.get().invalidate(products.getbFillImage());
                beforeFillTextView.setVisibility(View.VISIBLE);
                beforeReload.setVisibility(View.INVISIBLE);
                Picasso.get().load(products.getbFillImage()).fit().into(beforeFill, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        beforeReload.setVisibility(View.VISIBLE);
                        beforeFillTextView.setVisibility(View.INVISIBLE);
                        Toast.makeText(eachUserOrder.this, "After image loaded", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Exception ex) {
                        beforeReload.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

        afterReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(eachUserOrder.this, "Reload working", Toast.LENGTH_SHORT).show();
                afterFill.setImageResource(0);
                afterFillTextView.setVisibility(View.VISIBLE);
                afterReload.setVisibility(View.INVISIBLE);

                final long ONE_MEGABYTE = 1024 * 1024;
                OrderImagesRef.child(products.getaFillImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        afterFill.setImageBitmap(Bitmap.createScaledBitmap(bitmap, afterFill.getWidth(),500, false));
                        afterFillTextView.setVisibility(View.INVISIBLE);
                        afterReload.setVisibility(View.VISIBLE);
                        Toast.makeText(eachUserOrder.this, "After image loaded", Toast.LENGTH_SHORT).show();

                    }
                });

               /* Picasso.get().invalidate(products.getaFillImage());
                afterFillTextView.setVisibility(View.VISIBLE);
                afterReload.setVisibility(View.INVISIBLE);
                Picasso.get().load(products.getaFillImage()).fit().into(afterFill, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        afterReload.setVisibility(View.VISIBLE);
                        afterFillTextView.setVisibility(View.INVISIBLE);
                        Toast.makeText(eachUserOrder.this, "After image loaded", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Exception ex) {
                        afterReload.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This can only be done by the user
                //Check if this will work in firebase
                //Set Status to cancelled in firebase and also change order status to canceeled
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(eachUserOrder.this);
                builder.setTitle("Are you sure you want to cancel?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        loadingBar.setTitle("Loading");
                        loadingBar.setMessage("Cancelling... ");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        /*final DatabaseReference RootRef;*/
                        RootRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                        RootRef.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists()) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("status", "CANCELLED");
                                    RootRef.child(orderId).updateChildren(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                       orderStatus.setText("ORDER STATUS: CANCELLED");
                                                        loadingBar.dismiss();
                                                    } else {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(eachUserOrder.this, "fAIL TO CANCEL", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(eachUserOrder.this, "This order doesn't exist in database", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        orderMessageUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open userfeedback activty
                Intent userFeedback = new Intent(eachUserOrder.this, UserFeedback.class);
                startActivity(userFeedback);
            }
        });

    }

    //Accept or decline orders here, only by an admin
    private void acceptDelineDelivered(final String status){

        loadingBar.setTitle("Loading");
        loadingBar.setMessage("please wait... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        /*final DatabaseReference RootRef;*/
        RootRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", status);
                    RootRef.child(orderId).updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        orderStatus.setText("ORDER STATUS: " + status);
                                        loadingBar.dismiss();
                                        if(UserDeviceToken.equals("null")) {
                                            Toast.makeText(eachUserOrder.this, "cant get destination token", Toast.LENGTH_SHORT).show();
                                        } else {
                                            prepareNotification("Dear Customer,", "ORDER " + status);
                                        }
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(eachUserOrder.this, "fAIL TO ACCEPT/DECLINE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            if(proofFirstSecond.equals("first")) {
                ImageUri = data.getData();

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //here you can choose quality factor in third parameter(ex. i choosen 25)
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                fileInBytes = baos.toByteArray();

                beforeFillTextView.setVisibility(View.INVISIBLE);
                beforeFill.getLayoutParams().height = 500;
                //beforeFill.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                Bitmap dBmp = BitmapFactory.decodeByteArray(fileInBytes, 0, fileInBytes.length);
                //beforeFill.setImageURI(ImageUri);
                beforeFill.setImageBitmap(Bitmap.createScaledBitmap(dBmp, beforeFill.getWidth(),500, false));
            } else {
                ImageUri2 = data.getData();

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //here you can choose quality factor in third parameter(ex. i choosen 25)
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                fileInBytes2 = baos.toByteArray();

                afterFillTextView.setVisibility(View.INVISIBLE);
                afterFill.getLayoutParams().height = 500;
                Bitmap dBmp = BitmapFactory.decodeByteArray(fileInBytes2, 0, fileInBytes2.length);
                //beforeFill.setImageURI(ImageUri);
                afterFill.setImageBitmap(Bitmap.createScaledBitmap(dBmp, afterFill.getWidth(),500, false));
               /* afterFill.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                afterFill.setImageURI(ImageUri2);*/
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_each_user_order, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentOrderUri != null) {
            MenuItem menuItem = menu.findItem(R.id.action_accept);
            menuItem.setVisible(false);
            MenuItem menuItem1 = menu.findItem(R.id.action_decline);
            menuItem1.setVisible(false);
            MenuItem menuItem2 = menu.findItem(R.id.action_send_proofs);
            menuItem2.setVisible(false);
            MenuItem menuItem3 = menu.findItem(R.id.action_send_notification);
            menuItem3.setVisible(false);
            MenuItem menuItem4 = menu.findItem(R.id.action_delivered);
            menuItem4.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accept:
                //Only possible by an admin
                //If cancel works then implete this
                acceptDelineDelivered("ACCEPTED");
                return true;
            case R.id.action_decline:
                //Only possible by an admin
                //If cancel works then implete this
                acceptDelineDelivered("DECLINED");
                return true;
            case R.id.action_delivered:
                //Only possible by an admin
                //If cancel works then implete this
                acceptDelineDelivered("DELIVERED");
                return true;
            case R.id.action_send_proofs:
                //Only possible by an admin
                //If cancel works then implete this
                proofFirstSecond = "first";
                if(ImageUri != null && ImageUri2 != null) {
                    UploadImages();
                } else {
                    Toast.makeText(eachUserOrder.this, "Please upload images first", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_send_notification:
                //Only possible by an admin
                if(UserDeviceToken.equals("null")) {
                    Toast.makeText(eachUserOrder.this, "cant get destination token", Toast.LENGTH_SHORT).show();
                } else {
                    openDialog();
                }
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
       // String myDeviceToken = FirebaseInstanceId.getInstance().getToken();
        //myDeviceToken = myDeviceToken.substring(0,4);
        TOPIC = "/topics/" + UserDeviceToken; //topic has to match what the receiver subscribed to
        //TOPIC = "/topics/userABC";
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE = content;
        Toast.makeText(eachUserOrder.this, TOPIC, Toast.LENGTH_LONG).show();
        //FirebaseMessaging.getInstance().subscribeToTopic(myDeviceToken);

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        Toast.makeText(eachUserOrder.this, "Prepare Notification", Toast.LENGTH_LONG).show();
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        Toast.makeText(eachUserOrder.this, "Send Notification success", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(eachUserOrder.this, "Request error", Toast.LENGTH_LONG).show();
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


    private void UploadImages() {
        loadingBar.setTitle("Uploading");
        loadingBar.setMessage("Please wait... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final StorageReference filePath = OrderImagesRef.child(ImageUri.getLastPathSegment() + orderId + ".jpg");
        final String storagePath1 = ImageUri.getLastPathSegment() + orderId + ".jpg";
        Toast.makeText(eachUserOrder.this, "Storage ref " + filePath, Toast.LENGTH_SHORT).show();
        //final UploadTask uploadTask = filePath.putFile(ImageUri);
        final UploadTask uploadTask = filePath.putBytes(fileInBytes);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(eachUserOrder.this, "First Upload Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(eachUserOrder.this, "First Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        FirstImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirstImageUrl = task.getResult().toString();

                            Toast.makeText(eachUserOrder.this, "got the First image Url Successfully...", Toast.LENGTH_SHORT).show();

                            //Save the second image to storage

                            final StorageReference filePath2 = OrderImagesRef.child(ImageUri2.getLastPathSegment() + orderId + ".jpg");
                            final String storagePath2 = ImageUri2.getLastPathSegment() + orderId + ".jpg";
                            //final UploadTask uploadTask2 = filePath2.putFile(ImageUri2);
                            final UploadTask uploadTask2 = filePath2.putBytes(fileInBytes2);
                            uploadTask2.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    String message = e.toString();
                                    Toast.makeText(eachUserOrder.this, "Second upload Error: " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) //Fear this may cause problem
                                {
                                    Toast.makeText(eachUserOrder.this, "Second Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                                    Task<Uri> urlTask2 = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task2) throws Exception
                                        {
                                            if (!task2.isSuccessful())
                                            {
                                                throw task2.getException();
                                            }

                                            SecondImageUrl = filePath2.getDownloadUrl().toString();
                                            return filePath2.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task2)
                                        {
                                            if (task2.isSuccessful())
                                            {
                                                SecondImageUrl = task2.getResult().toString();

                                                Toast.makeText(eachUserOrder.this, "got the second image Url Successfully...", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(eachUserOrder.this, "Now updating firebase...", Toast.LENGTH_SHORT).show();

                                                //SaveProductInfoToDatabase();
                                                RootRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                                                RootRef.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot)
                                                    {
                                                        if (dataSnapshot.exists()) {
                                                            Map<String, Object> map = new HashMap<>();
                                                            /*map.put("bFillImage", FirstImageUrl);
                                                            map.put("aFillImage", SecondImageUrl);*/
                                                            map.put("bFillImage", storagePath1);
                                                            map.put("aFillImage", storagePath2);
                                                            RootRef.child(orderId).updateChildren(map)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                Toast.makeText(eachUserOrder.this, "Succesfully added image url to database", Toast.LENGTH_SHORT).show();
                                                                                loadingBar.dismiss();
                                                                                prepareNotification("Dear Customer", "Your proof have been sent");
                                                                            } else {
                                                                                /*loadingBar.dismiss();
                                                                                Toast.makeText(eachUserOrder.this, "fAIL TO ACCEPT/DECLINE", Toast.LENGTH_SHORT).show();*/
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            loadingBar.dismiss();
                                                            Toast.makeText(eachUserOrder.this, "This order doesn't exist in database, Fail to update url in database", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    private void OpenAdminPart() {
        FirebaseData();
    }

    private void FirebaseData() {
        Toast.makeText(eachUserOrder.this, "Now in Firebase", Toast.LENGTH_SHORT).show();
        /*DatabaseReference RootRef;*/
        RootRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        RootRef.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    Toast.makeText(eachUserOrder.this, "dataSnapshot exists", Toast.LENGTH_SHORT).show();
                   products = dataSnapshot.getValue(Products.class);

                    orderStatus.setText("ORDER STATUS: " + products.getStatus());

                    if(products.getbFillImage().equals("null")) {
                        beforeFillTextView.setText("Proof not yet sent");
                        afterFillTextView.setText("proof not yet sent");
                    } else {
                        //Set to image in firebase
                        beforeFill.getLayoutParams().height = 500;

                        final long ONE_MEGABYTE = 1024 * 1024;

                        //download file as a byte array
                        OrderImagesRef.child(products.getbFillImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               // beforeFill.setImageBitmap(bitmap);
                                beforeFill.setImageBitmap(Bitmap.createScaledBitmap(bitmap, beforeFill.getWidth(),500, false));
                                Toast.makeText(eachUserOrder.this, "Before image loaded", Toast.LENGTH_SHORT).show();
                                beforeFillTextView.setVisibility(View.INVISIBLE);
                                //Set to image in firebase
                                //afterFillTextView.setText("TESTING");
                                afterFill.getLayoutParams().height = 500;

                                OrderImagesRef.child(products.getaFillImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        afterFill.setImageBitmap(Bitmap.createScaledBitmap(bitmap, afterFill.getWidth(),500, false));
                                        afterFillTextView.setVisibility(View.INVISIBLE);
                                        beforeReload.setVisibility(View.VISIBLE);
                                        afterReload.setVisibility(View.VISIBLE);
                                        Toast.makeText(eachUserOrder.this, "After image loaded", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });

                        /*Picasso.get().load(products.getbFillImage()).fit().into(beforeFill, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(eachUserOrder.this, "Before image loaded", Toast.LENGTH_SHORT).show();
                                beforeFillTextView.setVisibility(View.INVISIBLE);
                                    //Set to image in firebase
                                    //afterFillTextView.setText("TESTING");
                                    afterFill.getLayoutParams().height = 500;
                                    Picasso.get().load(products.getaFillImage()).fit().into(afterFill, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            afterFillTextView.setVisibility(View.INVISIBLE);
                                            beforeReload.setVisibility(View.VISIBLE);
                                            afterReload.setVisibility(View.VISIBLE);
                                            Toast.makeText(eachUserOrder.this, "After image loaded", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onError(Exception ex) {

                                        }
                                    });

                            }

                            @Override
                            public void onError(Exception ex) {

                            }
                        });*/

                    }

                    if(mCurrentOrderUri == null) {
                        orderSummary = (
                                "Order Date = " + products.getOrderTime() + "\n" +
                                        "Quantity = " + products.getOrderQuantity() + "\n" +
                                        "Name = " + products.getCustomerName() + "\n" +
                                        "Hostel Name = " + products.getOrderHostel() + "\n" +
                                        "Block No = " + products.getOrderBlock() + "\n" +
                                        "Room No = " + products.getOrderRoom() + "\n" +
                                        "Phone No = " + products.getOrderPhoneNo() + "\n" +
                                        "Device Token = " + products.getToken()
                        );

                        orderSummaryTextview.setText(orderSummary);
                        UserDeviceToken = products.getToken();
                    }
                } else {
                    orderStatus.setText("ORDER STATUS: DELETED");
                    beforeFillTextView.setText("Order doesn't exist or deleted");
                    afterFillTextView.setText("Order doesn't exist or deleted");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                OrderEntry._ID,
                OrderEntry.COLUMN_ORDER_ID,
                OrderEntry.COLUMN_ORDER_NAME,
                OrderEntry.COLUMN_ORDER_QUANTITY,
                OrderEntry.COLUMN_ORDER_HOSTEL,
                OrderEntry.COLUMN_ORDER_BLOCK,
                OrderEntry.COLUMN_ORDER_ROOM,
                OrderEntry.COLUMN_ORDER_PHONE_NO,
                OrderEntry.COLUMN_ORDER_TIME
        };

        return new CursorLoader(this,
                mCurrentOrderUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_ID);
            orderId = cursor.getString(idColumnIndex);

            int nameColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_NAME);
            final String orderName = cursor.getString(nameColumnIndex);

            int quantityColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_QUANTITY);
            final String orderQuantity = cursor.getString(quantityColumnIndex);

            int hostelColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_HOSTEL);
            final String orderHostel = cursor.getString(hostelColumnIndex);

            int blockColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_BLOCK);
            final String orderBlock = cursor.getString(blockColumnIndex);

            int roomColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_ROOM);
            final String orderRoom = cursor.getString(roomColumnIndex);

            int phoneNoColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_PHONE_NO);
            final String orderPhoneNo = cursor.getString(phoneNoColumnIndex);

            int dateColumnIndex = cursor.getColumnIndex(OrderEntry.COLUMN_ORDER_TIME);
            final String orderDate = cursor.getString(dateColumnIndex);

            orderSummary = (
                    "Order Date = " + orderDate + "\n" +
                            "Quantity = " + orderQuantity + "\n" +
                            "Name = " + orderName + "\n" +
                            "Hostel Name = " + orderHostel + "\n" +
                            "Block No = " + orderBlock + "\n" +
                            "Room No = " + orderRoom + "\n" +
                            "Phone No = " + orderPhoneNo
            );

            orderSummaryTextview.setText(orderSummary);

           FirebaseData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }
}
