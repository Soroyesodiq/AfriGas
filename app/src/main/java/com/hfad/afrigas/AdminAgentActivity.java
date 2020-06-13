package com.hfad.afrigas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.afrigas.Model.Products;

public class AdminAgentActivity extends AppCompatActivity {
    private EditText adminLogin;
    private Button adminDone;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_agent);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Log In(Admin)");

        loadingBar = new ProgressDialog(this);
        adminLogin = findViewById(R.id.admin_login);
        adminDone = findViewById(R.id.admin_done);
        adminDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(adminLogin.getText())) {
                    Toast.makeText(AdminAgentActivity.this, "Please enter the password...", Toast.LENGTH_SHORT).show();
                } else {
                    //set loading bar
                    LoginAdmin();
                   // Toast.makeText(AdminAgentActivity.this, "You are good to go", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void LoginAdmin() {
        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Admin").child("Login").child("Password");
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    Toast.makeText(AdminAgentActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    if(adminLogin.getText().toString().equals(dataSnapshot.getValue().toString())) {
                        //open Admin panel here
                        loadingBar.dismiss();
                        Intent AdminPanel = new Intent(AdminAgentActivity.this, ordersAdmin.class);
                        startActivity(AdminPanel);
                    } else {
                        loadingBar.dismiss();
                        adminLogin.setError("Incorrect Password");
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAgentActivity.this, "Firebase error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
