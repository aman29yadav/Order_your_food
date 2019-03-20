package com.example.may.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.may.myproject.Common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // Toast.makeText(FoodList.this,"on create called",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
       // Toast.makeText(FoodList.this,"on create called 1",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_food_list);
       // Toast.makeText(FoodList.this,"on create called 2",Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
       // layoutManager = new LinearLayoutManager(this);
       // Toast.makeText(FoodList.this,"on create called 3",Toast.LENGTH_SHORT).show();
       // recyclerView.setLayoutManager(layoutManager);
      //  Toast.makeText(FoodList.this,"on create called 4",Toast.LENGTH_LONG).show();
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        if (getIntent() != null) {
           // Toast.makeText(FoodList.this,"on create called 4.5",Toast.LENGTH_SHORT).show();
            categoryId = getIntent().getStringExtra("CategoryId");
          //  Toast.makeText(FoodList.this,"category id="+categoryId,Toast.LENGTH_SHORT).show();

            if (!categoryId.isEmpty() && categoryId != null) {
           //     Toast.makeText(FoodList.this,"category ID="+categoryId,Toast.LENGTH_SHORT).show();
                if(Common.isConnectedToInternet(FoodList.this))
                loadListFood(categoryId);
                else{
                    Toast.makeText(FoodList.this,"Check Your Internet Connection !",Toast.LENGTH_LONG).show();
                    return;
                }
            }

        }
        //Toast.makeText(FoodList.this,"on create called 5",Toast.LENGTH_SHORT).show();
       // recyclerView.setAdapter(adapter);
    }

    private void loadListFood(String categoryId) {
       // Toast.makeText(FoodList.this,"category ID 1="+categoryId,Toast.LENGTH_SHORT).show();
            adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,
                    FoodViewHolder.class,
                    foodList.orderByChild("menuId").equalTo(categoryId)
            ) {
                @Override
                protected void populateViewHolder(FoodViewHolder viewHolder, Food model, final int position) {
                    viewHolder.food_name.setText(model.getName());
                    Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                    final Food local = model;

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent foodDetail = new Intent(FoodList.this,FoodDetails.class);
                            foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                            startActivity(foodDetail);
                        }
                    });


                }
            };
            recyclerView.setAdapter(adapter);
        }
}

