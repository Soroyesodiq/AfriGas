package com.hfad.afrigas.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hfad.afrigas.R;

public class FeedbacksViewHolder extends RecyclerView.ViewHolder {
    public TextView dateFeedback, phonenoFeedback, messageFeedback;

    public FeedbacksViewHolder(View itemView) {
        super(itemView);
        dateFeedback = itemView.findViewById(R.id.date_feedback);
        phonenoFeedback = itemView.findViewById(R.id.phoneno_feedback);
        messageFeedback = itemView.findViewById(R.id.message_feedback);
    }

}
