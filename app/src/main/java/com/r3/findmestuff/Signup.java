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
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users");

                //get all the values
                String name = reg_name.getEditText().getText().toString();
                String username = reg_username.getEditText().getText().toString();
                String email = reg_email.getEditText().getText().toString();
                String phone = reg_phone.getEditText().getText().toString();
                String password = reg_password.getEditText().getText().toString();




                UserHelperClass helperClass = new UserHelperClass(name,password,username,email,phone);

                reference.child(username).setValue(helperClass);



            }
        });

        reg_callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}