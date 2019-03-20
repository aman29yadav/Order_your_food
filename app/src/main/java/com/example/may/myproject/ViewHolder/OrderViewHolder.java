package com.example.may.myproject.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.may.myproject.Interface.ItemClickListener;
import com.example.may.myproject.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderTableNo;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderTableNo = itemView.findViewById(R.id.order_tableNo);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
     //  itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
