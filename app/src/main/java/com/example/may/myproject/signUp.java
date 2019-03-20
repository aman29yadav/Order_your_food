package com.example.may.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.may.myproject.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    EditText phone,name,password;
    Button signUpbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone= findViewById(R.id.phnedt);
        name = findViewById(R.id.nameedt);
        password = findViewById(R.id.passedt);
        signUpbtn = findViewById(R.id.signupbtn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog dialog = new ProgressDialog(signUp.this);
                    dialog.setMessage("Please Wait...");
                    dialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                dialog.dismiss();
                             //   Toast.makeText(signUp.this, "User Already Exists", Toast.LENGTH_LONG).show();
                            } else {
                                dialog.dismiss();
                                User user = new User(name.getText().toString(), password.getText().toString());
                                table_user.child(phone.getText().toString()).setValue(user);
                                Toast.makeText(signUp.this, "Sign up successfully", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(signUp.this, signIn.class);
                                startActivity(i);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(signUp.this, "Some Technical Error!Try Later.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(signUp.this,"Check Your Internet Connection !",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
