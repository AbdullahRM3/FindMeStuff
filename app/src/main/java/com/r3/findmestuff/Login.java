package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
Button callSignUp,btn_login;
ImageView logo_image;
TextView logo_name, slogan_name;
TextInputLayout username ,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //Hooks
      callSignUp = findViewById(R.id.btn_new_signup);
      btn_login = findViewById(R.id.btn_login);
      logo_image = findViewById(R.id.logo_image);
      slogan_name = findViewById(R.id.slogan_name);
      logo_name = findViewById(R.id.logo_name);
      username = findViewById(R.id.username);
      password = findViewById(R.id.password);


    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View v){

        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        }
        else{
            isUser();
        }

    }

    private void isUser() {

        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),UserProfile.class);

                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("username",usernameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("phone",phoneFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }

                }
                else{
                    username.setError("No Such User exists");
                    username.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void gotoSignup(View v){


        Intent intent = new Intent(Login.this,Signup.class);


        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View,String>(logo_image,"logo_image");
        pairs[1] = new Pair<View,String>(logo_name,"logo_image2");
        pairs[2] = new Pair<View,String>(slogan_name,"logo_desc");
        pairs[3] = new Pair<View,String>(username,"username_trans");
        pairs[4] = new Pair<View,String>(password,"password_trans");
        pairs[5] = new Pair<View,String>(btn_login,"button_trans");
        pairs[6] = new Pair<View,String>(callSignUp,"login_signup_trans");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
        startActivity(intent,options.toBundle());

    }

}