package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {
    TextInputLayout fullname, email, phone, password ;
    TextView fullnameLabel, usernameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        String user_username = intent.getStringExtra("username");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");
        String user_phone = intent.getStringExtra("phone");
        String user_password = intent.getStringExtra("password");

        fullnameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullname.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phone.getEditText().setText(user_phone);
        password.getEditText().setText(user_password);
    }
}