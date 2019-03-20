package com.example.may.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.may.myproject.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class signIn extends AppCompatActivity {
EditText ed1,ed2;
Button signin;
CheckBox chkRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        signin = findViewById(R.id.signinbtn);
        chkRemember = findViewById(R.id.chkRemember);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(chkRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY,ed1.getText().toString());
                        Paper.book().write(Common.PWD_KEY,ed2.getText().toString());

                    }

                    final ProgressDialog dialog = new ProgressDialog(signIn.this);
                    dialog.setMessage("Please Wait...");
                    dialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialog.dismiss();
                            if (dataSnapshot.child(ed1.getText().toString()).exists()) {


                                User user = dataSnapshot.child(ed1.getText().toString()).getValue(User.class);
                                 user.setPhone(ed1.getText().toString());
                                if (user.getPassword().equals(ed2.getText().toString())) {
                                    Toast.makeText(signIn.this, "sign in successfully", Toast.LENGTH_LONG).show();

                                    Intent homeIntent = new Intent(signIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(signIn.this, "sign in Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(signIn.this, "User not exist", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(signIn.this,"Check Your Internet Connection !",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
