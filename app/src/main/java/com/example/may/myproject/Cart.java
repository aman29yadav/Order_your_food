package com.example.may.myproject;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.myproject.Common.Common;
import com.example.may.myproject.Database.Database;
import com.example.may.myproject.Model.MyResponse;
import com.example.may.myproject.Model.Notification;
import com.example.may.myproject.Model.Order;
import com.example.may.myproject.Model.Requests;
import com.example.may.myproject.Model.Sender;
import com.example.may.myproject.Model.Token;
import com.example.may.myproject.Remote.APIService;
import com.example.may.myproject.ViewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    APIService mService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init service
        mService = Common.getFCMService();

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(cart.size()>0) {
                  showAlertDialog();

              }
              else
                  Toast.makeText(Cart.this,"Cart is Empty!!!",Toast.LENGTH_SHORT).show();
            }

        });

        loadListFood();
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Table No.");
        alertDialog.setMessage("Enter Your Table Number :");

        final EditText edtTable = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtTable.setLayoutParams(lp);
        alertDialog.setView(edtTable);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Requests requests = new Requests(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtTable.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //Submit to firebase
                String order_number = String.valueOf(System.currentTimeMillis());
                request.child(order_number).setValue(requests);

                //Delete Cart;
                new Database(getBaseContext()).cleanCart();

                sendNotificationOrder(order_number);

               // Toast.makeText(Cart.this, "Thank you, Your Order Will be Served Soon", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendNotificationOrder(final String order_number) {
        Log.i("MSG", "MSGS 1");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);
        Log.i("MSG", "ORDER=" + data);
        data.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("MSG", "DATASNAPSHOT=" + dataSnapshot);
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Log.i("MSG", "MSGS 4");
                    Token serverToken = postSnapShot.getValue(Token.class);

                    //Create raw payload to send
                    Notification notification = new Notification("msg", "You have new order " + order_number);
                    Log.i("MSG", "serverToken=" + serverToken.getToken());
                    Sender content = new Sender(serverToken.getToken(), notification);
                    Log.i("MSG", "CONTENT = " + content);
                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    //Toast.makeText(Cart.this,"response="+response,Toast.LENGTH_LONG).show();
                                    Log.i("msg5", "response" + response);
                                    if (response.isSuccessful()) {
                                        if (response.body().success == 1) {
                                            Log.i("msg2", "response=" + response.body());
                                            // Log.i("msg7","response="+response.body().success);
                                           // Toast.makeText(Cart.this, "else response=" + response, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(Cart.this, "Thank You , Order will be serve soon", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Cart.this, "Failed!!!", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Log.i("msg6", "response=" + response.body());
                                        // Log.i("msg7","response="+response.body().success);
                                        Toast.makeText(Cart.this, "else response=" + response, Toast.LENGTH_SHORT).show();
                                        //finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("Error", t.getMessage());
                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        int total = 0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            //Locale locale = new Locale("en","IN");
            //  NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText("Rs. " + String.valueOf(total));
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteCart(int order) {
        //removing item at List<Order> by its position
        cart.remove(order);
        //deleting all old data from sqlite
        new Database(this).cleanCart();
        //updating new data form List<Order> to sqlite
        for(Order item:cart)
            new Database(this).addToCart(item);
        //refresh
        loadListFood();
    }
}