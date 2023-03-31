package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
Button callSignUp,btn_login;
ImageView logo_image;
TextView logo_name, slogan_name;
 TextInputLayout email ,password;

private FirebaseAuth mAuth;


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
      email = findViewById(R.id.email);
      password = findViewById(R.id.password);


      mAuth = FirebaseAuth.getInstance();


    }
    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
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
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        else{
            isUser();
        }

    }


   private void isUser() {

       final String userEnteredEmail = email.getEditText().getText().toString().trim();
       final String userEnteredPassword = password.getEditText().getText().toString().trim();
       DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

       // Show progress bar
       ProgressDialog progressDialog = new ProgressDialog(this);
       progressDialog.setMessage("Logging in...");
       progressDialog.setCancelable(false);
       progressDialog.show();

       mAuth.signInWithEmailAndPassword(userEnteredEmail,userEnteredPassword)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       progressDialog.dismiss();
                       if(task.isSuccessful()){
                           if(mAuth.getCurrentUser().isEmailVerified())
                           {
                               Toast.makeText(Login.this,"login successful",Toast.LENGTH_SHORT).show();
                               //UpdateUI(task.getResult().getUser());
                               UpdateUI();
                           }
                           else{
                               Toast.makeText(Login.this,"pls verify account before login",Toast.LENGTH_SHORT).show();
                           }


                       }
                       else{
                           if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                               Toast.makeText(Login.this,"Invalid Password",Toast.LENGTH_SHORT).show();


                           }else if (task.getException() instanceof FirebaseAuthInvalidUserException){
                               Toast.makeText(Login.this,"User email Does not exist",Toast.LENGTH_SHORT).show();

                           }
                       }
                   }
               });
   }


    @Override
    protected void onStart() {
        super.onStart();
        UpdateUI();
    }

    public void UpdateUI() {

        FirebaseUser user =mAuth.getCurrentUser();
        if (user == null){

            return;
        }else{
            Intent intent = new Intent(Login.this,Dashboard.class);
            startActivity(intent);
            //insert the thing that will do after login
        }

    }

    public void gotoSignup(View v){


        Intent intent = new Intent(Login.this,Signup.class);


        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View,String>(logo_image,"logo_image");
        pairs[1] = new Pair<View,String>(logo_name,"logo_image2");
        pairs[2] = new Pair<View,String>(slogan_name,"logo_desc");
        pairs[3] = new Pair<View,String>(email,"username_trans");
        pairs[4] = new Pair<View,String>(password,"password_trans");
        pairs[5] = new Pair<View,String>(btn_login,"button_trans");
        pairs[6] = new Pair<View,String>(callSignUp,"login_signup_trans");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
        startActivity(intent,options.toBundle());

    }

    public void ForgotPasswordDialog(android.view.View view){

        final DialogPlus dialogPlus = DialogPlus.newDialog(Login.this)
                .setContentHolder(new ViewHolder(R.layout.reset_password))
                .setExpanded(true,400)
                .create();
        View v = dialogPlus.getHolderView();
        EditText emailPass = v.findViewById(R.id.email_pass);

        Button reset = v.findViewById(R.id.btn_reset);
        dialogPlus.show();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email =emailPass.getText().toString().trim();

                if(email.isEmpty()){

                    emailPass.setError("Email is Required");
                    emailPass.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailPass.setError("Please provide a valid email");
                    emailPass.requestFocus();
                    return;

                }
                ProgressDialog progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("sending password reset code");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();

                            Toast.makeText(Login.this,"Check your Email to reset password",Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"try again, Something went wrong",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

}