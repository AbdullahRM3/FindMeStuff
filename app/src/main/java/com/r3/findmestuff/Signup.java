package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    //variables
    Button reg_callLogin,btn_signup;
    ImageView reg_logo_image;
    TextView reg_logo_name, reg_slogan_name;
    TextInputLayout reg_username ,reg_password ,reg_name , reg_phone, reg_email;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Hooks
        reg_callLogin = findViewById(R.id.btn_new_login);
        btn_signup = findViewById(R.id.btn_signup);
        reg_logo_image = findViewById(R.id.reg_logo_image);
        reg_slogan_name = findViewById(R.id.reg_slogan_name);
        reg_logo_name = findViewById(R.id.reg_logo_name);
        reg_username = findViewById(R.id.reg_username);
        reg_password = findViewById(R.id.reg_password);
        reg_email = findViewById(R.id.reg_email);
        reg_name = findViewById(R.id.reg_name);
        reg_phone = findViewById(R.id.reg_PhoneNo);








        //saves data in firebase


    }

    private Boolean validateName() {
        String val = reg_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_name.setError("Field cannot be empty");
            return false;
        }
        else {
            reg_name.setError(null);
            reg_name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = reg_username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            reg_username.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 20) {
            reg_username.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            reg_username.setError("White Spaces are not allowed");
            return false;
        } else {
            reg_username.setError(null);
            reg_username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = reg_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            reg_email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            reg_email.setError("Invalid email address");
            return false;
        } else {
            reg_email.setError(null);
            reg_email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = reg_phone.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_phone.setError("Field cannot be empty");
            return false;
        } else {
            reg_phone.setError(null);
            reg_phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = reg_password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            reg_password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            reg_password.setError("Password is too weak");
            return false;
        } else {
            reg_password.setError(null);
            reg_password.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View v){


        if(!validateName() |!validateUsername() | !validatePhone() | !validateEmail() | !validatePassword())
        {
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        //get all the values
        String name = reg_name.getEditText().getText().toString();
        String username = reg_username.getEditText().getText().toString();
        String email = reg_email.getEditText().getText().toString();
        String phone = reg_phone.getEditText().getText().toString();
        String password = reg_password.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name,username,password,email,phone);
        reference.child(username).setValue(helperClass);


    }

    public void gotoLogin(View v){


                Intent intent = new Intent(Signup.this,Login.class);


                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(reg_logo_image,"logo_image");
                pairs[1] = new Pair<View,String>(reg_logo_name,"logo_image2");
                pairs[2] = new Pair<View,String>(reg_slogan_name,"logo_desc");
                pairs[3] = new Pair<View,String>(reg_username,"username_trans");
                pairs[4] = new Pair<View,String>(reg_password,"password_trans");
                pairs[5] = new Pair<View,String>(btn_signup,"button_trans");
                pairs[6] = new Pair<View,String>(reg_callLogin,"login_signup_trans");



                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this,pairs);
                startActivity(intent,options.toBundle());




    }

}