package com.r3.findmestuff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class UserProfile extends AppCompatActivity {
    TextInputLayout username, phone;
    TextView fullnameLabel, usernameLabel;
    String _USERNAME,_PASSWORD,_EMAIL,_PHONE, UserID;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    AuthCredential credential;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        UserID = firebaseUser.getUid();

        //hooks
        username=findViewById(R.id.update_full_name);
       // password=findViewById(R.id.update_password);
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void update(View v){
        if(isUserNameChanged()|| isPhoneChanged()){
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_LONG).show();

        }
        else  Toast.makeText(this,"Data is same and cannot be updated",Toast.LENGTH_LONG).show();

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
    public void Update_Password(View v){

        mAuth.sendPasswordResetEmail(_EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserProfile.this,"Check your Email to reset password",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UserProfile.this,"try again, Something went wrong",Toast.LENGTH_SHORT).show();

                }
            }
        });
/*        final DialogPlus dialogPlus = DialogPlus.newDialog(UserProfile.this)
                .setContentHolder(new ViewHolder(R.layout.reset_password))
                .setExpanded(true,400)
                .create();
        EditText emailPass = v.findViewById(R.id.email_pass);


        Button reset = v.findViewById(R.id.btn_reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =emailPass.getText().toString().trim();
                if(email.isEmpty()){

                    emailPass.setError("Email is Required");
                    emailPass.requestFocus();
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailPass.setError("Please provide a valid email");
                    emailPass.requestFocus();

                }

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserProfile.this,"Check your Email to reset password",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(UserProfile.this,"try again, Something went wrong",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        dialogPlus.show(); */
    }
    public void Delete_Account(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Are you Sure??");
        builder.setMessage("deleted accounts cant be recovered");
        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                credential = EmailAuthProvider
                        .getCredential(_EMAIL, _PASSWORD);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(UserProfile.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                    mRef.child(UserID).removeValue();
                                    mAuth.signOut();
                                    Intent intent = new Intent(UserProfile.this,Login.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });


            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UserProfile.this, "canceled", Toast.LENGTH_SHORT).show();
            }

        });

        builder.show();

    }

}
