package com.hfad.afrigas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.afrigas.Model.Feedbacks;
import com.hfad.afrigas.Model.Products;
import com.hfad.afrigas.ViewHolder.FeedbacksViewHolder;
import com.hfad.afrigas.ViewHolder.ProductViewHolder;

public class FeedbackAdmin extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_admin);

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Users Feedbacks");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks");

        recyclerView = findViewById(R.id.recycler_feedback);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Feedbacks> options =
                new FirebaseRecyclerOptions.Builder<Feedbacks>()
                        .setQuery(ProductsRef, Feedbacks.class)
                        .build();


        FirebaseRecyclerAdapter<Feedbacks, FeedbacksViewHolder> adapter =
                new FirebaseRecyclerAdapter<Feedbacks, FeedbacksViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FeedbacksViewHolder holder, int position, @NonNull final Feedbacks model)
                    {
                        String modelDateFeedbacks = model.getDate();
                        String modelPhoneNoFeedbacks = model.getPhoneNo();
                        String modelMessageFeedbacks = model.getMessage();

                        holder.dateFeedback.setText(modelDateFeedbacks);
                        Toast.makeText(FeedbackAdmin.this, "in bind view", Toast.LENGTH_SHORT).show();
                        Toast.makeText(FeedbackAdmin.this, modelDateFeedbacks, Toast.LENGTH_SHORT).show();
                        holder.phonenoFeedback.setText(modelPhoneNoFeedbacks);
                        holder.messageFeedback.setText(modelMessageFeedbacks);

                        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FeedbackAdmin.this, eachUserOrder.class);
                                intent.putExtra("id", model.getOrderId());
                                startActivity(intent);
                            }
                        });*/
                       /* holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);*/
                    }

                    @NonNull
                    @Override
                    public FeedbacksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbacks_layout, parent, false);
                        FeedbacksViewHolder holder = new FeedbacksViewHolder(view);
                        return holder;

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
