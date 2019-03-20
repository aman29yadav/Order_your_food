package com.example.may.myproject;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.may.myproject.Common.Common;
import com.example.may.myproject.Database.Database;
import com.example.may.myproject.Model.Order;
import com.example.may.myproject.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    //FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    Button cmntbtn,btnCart;
    RatingBar ratingBar;

    String foodId="";
    Food currentFood;

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTabl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);


        Log.i("msg","Inside Food details");

        //firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");
        ratingTabl = database.getReference("Rating");
        //InitView
        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        cmntbtn = findViewById(R.id.cmntbtn);
        ratingBar = findViewById(R.id.ratingBar);

        cmntbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount())
                );
                Toast.makeText(FoodDetails.this,"Added To Cart",Toast.LENGTH_SHORT).show();

            }
        });
        food_description = findViewById(R.id.food_description);
        food_price = findViewById(R.id.food_price);
        food_name = findViewById(R.id.food_name1);
        food_image = findViewById(R.id.img_food);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



        if(getIntent()!=null){
            foodId = getIntent().getStringExtra("FoodId");
           // Toast.makeText(FoodDetails.this,"foodId="+foodId,Toast.LENGTH_LONG).show();
            Log.i("fId","foodid="+foodId);
            if(!foodId.isEmpty()){

                if(Common.isConnectedToInternet(getBaseContext())) {
                    getDetailFood(foodId);
                    getratingFood(foodId);
                }
                else{
                    Toast.makeText(FoodDetails.this,"Check Your Internet Connection !",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }


    }

    private void getratingFood(String foodId) {
        Query foodRating = ratingTabl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
              if(count!=0){
                  float average = sum/count;
                  ratingBar.setRating(average);
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Good","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please Give your Feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this)
                .show();
    }

    private void getDetailFood(String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText("Rs."+currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FoodDetails.this,"Food loading cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        //get rating and upload to firebase

        final Rating rating = new Rating(Common.currentUser.getPhone(),
                foodId,String.valueOf(i),s);
        ratingTabl.child(Common.currentUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {

                    //removing old values
                   ratingTabl.child(Common.currentUser.getPhone()).removeValue();
                    //Update new Value
                    ratingTabl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else{
                    //update new value
                    ratingTabl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Log.i("MSG","Feedback done");
                Toast.makeText(FoodDetails.this,"Thanks for Feedback!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
