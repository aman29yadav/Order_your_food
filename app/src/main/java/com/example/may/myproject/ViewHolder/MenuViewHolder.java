package com.example.may.myproject.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myproject.Interface.ItemClickListener;
import com.example.may.myproject.R;

import org.w3c.dom.Text;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);

    txtMenuName = itemView.findViewById(R.id.menu_name);
    imageView = itemView.findViewById(R.id.menu_image);

    itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
