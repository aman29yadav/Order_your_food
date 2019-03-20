package com.example.may.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.may.myproject.Common.Common;
import com.example.may.myproject.Model.Requests;
import com.example.may.myproject.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
     //   Toast.makeText(OrderStatus.this,"phone="+phone,Toast.LENGTH_SHORT).show();

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(Requests.class,R.layout.order_layout,OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Requests model, int position) {
                //Toast.makeText(OrderStatus.this,"msg1",Toast.LENGTH_SHORT).show();
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
              final String da =  sdf.format(d);
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderTableNo.setText(model.getTotal());
                viewHolder.txtOrderPhone.setText(da);
               // Toast.makeText(OrderStatus.this,"msg2",Toast.LENGTH_SHORT).show();


            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
       // Toast.makeText(OrderStatus.this,"msg3",Toast.LENGTH_SHORT).show();

       // Log.i("status","status"+status);
        if(status!=null) {
            if (status.equals("0"))
                return "Placed";
            else if (status.equals("1"))
                return "Yet To Serve";
            else
                return "Served";
        }
        return "Placed";
    }
}
