package com.hfad.afrigas;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.afrigas.database.DataContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class UserFeedback extends AppCompatActivity {

    private Button btnSubmitFeedbackButton;
    private EditText phoneNoButton, messageButton;
    private String feedbackDateString;
    private String feedbackId;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Send Feedback");

        loadingBar = new ProgressDialog(this);

        btnSubmitFeedbackButton = findViewById(R.id.btnSubmitFeedback);
        phoneNoButton = findViewById(R.id.feedbackPhoneNo);
        messageButton = findViewById(R.id.msg1);

        btnSubmitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((phoneNoButton.getText().toString().isEmpty()) || (messageButton.getText().toString().isEmpty())) {
                    Toast.makeText(UserFeedback.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Loading");
                    loadingBar.setMessage("Please wait... ");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    phoneNoButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    messageButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    SaveFeedback();
                }
            }
        });

    }

    private void SaveFeedback() {
        Calendar feedbackCalender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        feedbackDateString = currentDate.format(feedbackCalender.getTime());

        Random r = new Random();
        int randomNumbr = r.nextInt(10000);
        feedbackId = feedbackDateString + String.valueOf(randomNumbr);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
/*
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {*/

                //Saving to firebase
                HashMap<String, Object> userFeedbackMap = new HashMap<>();
                userFeedbackMap.put("Id", feedbackId);
                userFeedbackMap.put("Date", feedbackDateString);
                userFeedbackMap.put("PhoneNo", phoneNoButton.getText().toString().trim());
                userFeedbackMap.put("Message", messageButton.getText().toString().trim());

                RootRef.child("Feedbacks").child(feedbackId).updateChildren(userFeedbackMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    Toast.makeText(UserFeedback.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                                    Intent home = new Intent(UserFeedback.this, MainActivity.class);
                                    startActivity(home);
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(UserFeedback.this, "Unable to send feedback", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

           /* }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });*/
}

    public void goBackHome(View view) {
        Intent home = new Intent(UserFeedback.this, MainActivity.class);
        startActivity(home);
    }
}
