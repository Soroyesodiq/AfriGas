package com.hfad.afrigas.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.afrigas.Interface.ItemClickListner;
import com.hfad.afrigas.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView adminDateHostel, adminGasQuantityName, adminOrderStatus;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);

        adminDateHostel = itemView.findViewById(R.id.admin_date_hostel);
        adminGasQuantityName = itemView.findViewById(R.id.admin_gasQuantity_name);
        adminOrderStatus = itemView.findViewById(R.id.admin_order_status);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

