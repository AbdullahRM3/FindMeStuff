package com.r3.findmestuff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    TextInputLayout username, phone, password ;
    TextView fullnameLabel, usernameLabel;
    String _USERNAME,_PASSWORD,_EMAIL,_PHONE, UserID;
    DatabaseReference mRef;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        UserID = firebaseUser.getUid();

        //hooks
        username=findViewById(R.id.update_full_name);
        password=findViewById(R.id.update_password);
        phone=findViewById(R.id.update_phone);
        fullnameLabel=findViewById(R.id.full_name);
        usernameLabel=findViewById(R.id.username_profile);


       showAllUserdata();
    }






    private void showAllUserdata() {




        mRef.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass helperClass =snapshot.getValue(UserHelperClass.class);

                if (helperClass != null){
                    _USERNAME = helperClass.username;
                    _EMAIL = helperClass.email;
                    _PASSWORD = helperClass.password;
                    _PHONE = helperClass.phone;

                    fullnameLabel.setText(_USERNAME);
                    usernameLabel.setText(_EMAIL);
                    username.getEditText().setText(_USERNAME);
                    phone.getEditText().setText(_PHONE);
                    password.getEditText().setText(_PASSWORD);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void update(View v){
        if(isUserNameChanged()|| isPasswordChanged()|| isPhoneChanged()){
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_LONG).show();

        }
        else  Toast.makeText(this,"Data is same and cannot be updated",Toast.LENGTH_LONG).show();

    }
    private boolean isPasswordChanged(){
        if(!_PASSWORD.equals(password.getEditText().getText().toString()))
        {
            mRef.child(UserID).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD=password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }
    private boolean isUserNameChanged(){
        if(!_USERNAME.equals(username.getEditText().getText().toString())){
            mRef.child(UserID).child("username").setValue(username.getEditText().getText().toString());
            _USERNAME=username.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }
    private boolean isPhoneChanged(){
        if(!_PHONE.equals(phone.getEditText().getText().toString())){
            mRef.child(UserID).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE=phone.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }
}
