package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {
    TextInputLayout fullname, email, phone, password ;
    TextView fullnameLabel, usernameLabel;
    String _USERNAME, _NAME,_PASSWORD,_EMAIL,_PHONE;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reference = FirebaseDatabase.getInstance().getReference("Users");

        //hooks
        fullname=findViewById(R.id.update_full_name);
        email=findViewById(R.id.update_email);
        password=findViewById(R.id.update_password);
        phone=findViewById(R.id.update_phone);
        fullnameLabel=findViewById(R.id.full_name);
        usernameLabel=findViewById(R.id.username_profile);

        //show all user data
        showAllUserdata();
    }

    private void showAllUserdata() {

        Intent intent = getIntent();
        _USERNAME = intent.getStringExtra("username");
        _NAME = intent.getStringExtra("name");
        _EMAIL = intent.getStringExtra("email");
        _PHONE = intent.getStringExtra("phone");
        _PASSWORD = intent.getStringExtra("password");

        fullnameLabel.setText(_NAME);
        usernameLabel.setText(_USERNAME);
        fullname.getEditText().setText(_NAME);
        email.getEditText().setText(_EMAIL);
        phone.getEditText().setText(_PHONE);
        password.getEditText().setText(_PASSWORD);
    }
    public void update(View v){
        if(isNameChanged()|| isPasswordChanged()|| isEmailChanged()|| isPhoneChanged()){
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_LONG).show();

        }
        else  Toast.makeText(this,"Data is same and cannot be updated",Toast.LENGTH_LONG).show();

    }
    private boolean isPasswordChanged(){
        if(!_PASSWORD.equals(password.getEditText().getText().toString()))
        {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD=password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }
    private boolean isNameChanged(){
        if(!_NAME.equals(fullname.getEditText().getText().toString())){
            reference.child(_USERNAME).child("name").setValue(fullname.getEditText().getText().toString());
            _NAME=fullname.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }
    private boolean isEmailChanged(){
        if(!_EMAIL.equals(email.getEditText().getText().toString())){
            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL=email.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }
    private boolean isPhoneChanged(){
        if(!_PHONE.equals(phone.getEditText().getText().toString())){
            reference.child(_USERNAME).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE=phone.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }
}
