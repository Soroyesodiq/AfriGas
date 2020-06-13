package com.hfad.afrigas;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.hfad.afrigas.Model.Info;


import java.util.HashMap;
import java.util.Map;

public class AdminInfo extends AppCompatActivity {

    private DatabaseReference RootRef;
    private EditText setPrice, setShortinfo, setLonginfo, setAd;
    private Button priceButton, adButton, shortinfoButton, longinfoButton;
    private Info info;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Update Info");

        loadingBar = new ProgressDialog(this);
        setPrice = findViewById(R.id.set_price_box);
        setShortinfo = findViewById(R.id.set_shortinfo_box);
        setLonginfo = findViewById(R.id.set_longinfo_box);
        setAd = findViewById(R.id.set_ad_box);
        priceButton = findViewById(R.id.done_price);
        adButton = findViewById(R.id.ad_done);
        shortinfoButton = findViewById(R.id.shortinfo_done);
        longinfoButton = findViewById(R.id.longinfo_done);

        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        RootRef.child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    info = dataSnapshot.getValue(Info.class);
                    setPrice.setText(info.getPrice());
                    setAd.setText(info.getAd());
                    setShortinfo.setText(info.getShortInfo());
                    setLonginfo.setText(info.getLongInfo());
                    loadingBar.dismiss();
                    Toast.makeText(AdminInfo.this, info.getShortInfo(), Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminInfo.this, "Error...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setPrice.getText().toString().isEmpty()) {
                    Toast.makeText(AdminInfo.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Loading");
                    loadingBar.setMessage("Please wait... ");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
                    RootRef.child("Info").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("Price", setPrice.getText().toString());
                                RootRef.child("Info").updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                } else {
                                                   loadingBar.dismiss();
                                                    Toast.makeText(AdminInfo.this, "Uploading unsuccessfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                               loadingBar.dismiss();
                                Toast.makeText(AdminInfo.this, "Data doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setAd.getText().toString().isEmpty()) {
                    Toast.makeText(AdminInfo.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Loading");
                    loadingBar.setMessage("Please wait... ");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
                    RootRef.child("Info").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("Ad", setAd.getText().toString());
                                RootRef.child("Info").updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                } else {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(AdminInfo.this, "Uploading unsuccessfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(AdminInfo.this, "Data doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        shortinfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setShortinfo.getText().toString().isEmpty()) {
                    Toast.makeText(AdminInfo.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Loading");
                    loadingBar.setMessage("Please wait... ");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
                    RootRef.child("Info").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("ShortInfo", setShortinfo.getText().toString());
                                RootRef.child("Info").updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                } else {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(AdminInfo.this, "Uploading unsuccessfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(AdminInfo.this, "Data doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        longinfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setLonginfo.getText().toString().isEmpty()) {
                    Toast.makeText(AdminInfo.this, "Kindly fill out empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Loading");
                    loadingBar.setMessage("Please wait... ");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
                    RootRef.child("Info").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("LongInfo", setLonginfo.getText().toString());
                                RootRef.child("Info").updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                } else {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(AdminInfo.this, "Uploading unsuccessfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(AdminInfo.this, "Data doesn't exist", Toast.LENGTH_SHORT).show();
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
}
