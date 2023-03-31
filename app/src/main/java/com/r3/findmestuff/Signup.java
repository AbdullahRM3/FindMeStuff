package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    //variables
    Button reg_callLogin,btn_signup;
    ImageView reg_logo_image;
    TextView reg_logo_name, reg_slogan_name;
    TextInputLayout reg_username ,reg_password , reg_phone, reg_email;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;




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
        reg_phone = findViewById(R.id.reg_PhoneNo);

        mAuth= FirebaseAuth.getInstance();
        //saves data in firebase


    }

    private Boolean validateUsername() {
        String val = reg_username.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_username.setError("Field cannot be empty");
            return false;
        }
        else {
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


        if(!validateUsername() | !validatePhone() | !validateEmail() | !validatePassword())
        {
            return;
        }

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users");

        //get all the values

        String username = reg_username.getEditText().getText().toString();
        String email = reg_email.getEditText().getText().toString();
        String phone = reg_phone.getEditText().getText().toString();
        String password = reg_password.getEditText().getText().toString();

        // Show progress bar
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this,"Signup successful pls verify on email to login",Toast.LENGTH_SHORT).show();
                            String uid = authResult.getUser().getUid();
                            UserHelperClass helperClass = new UserHelperClass(username,password,email,phone);
                            mRef.child(uid).setValue(helperClass);
                        }
                        else{
                            Toast.makeText(Signup.this,"email not sent for verification",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progressDialog.dismiss();
                //Now add data to Realtime Database
                mAuth.signOut();
            }
        }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(Signup.this,"Email Already in Use",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }
        );
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