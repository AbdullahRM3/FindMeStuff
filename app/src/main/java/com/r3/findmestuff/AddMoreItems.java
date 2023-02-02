package com.r3.findmestuff;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddMoreItems extends AppCompatActivity {
ImageView iimage;
TextInputLayout add_item_name,add_description;
Button add_item_btn;
String Iname,Idescription,UserID;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseUser firebaseUser;
    FloatingActionButton fab;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_items);
        mRef = mDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        UserID = firebaseUser.getUid();
        iimage = findViewById(R.id.item_image);
        //hooks
        add_item_name =findViewById(R.id.Item_Name);
        add_description =findViewById(R.id.description);
        add_item_btn =findViewById(R.id.btn_add);














        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        iimage.setImageURI(uri);
                        // Use the uri to load the image
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                    }
                });
        fab = findViewById(R.id.item_image_select);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ImagePicker.Companion.with(AddMoreItems.this)
                    .crop()
                    .cropOval()
                    .maxResultSize(512,512,true)
                    .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                    .createIntentFromDialog((Function1)(new Function1(){
                        public Object invoke(Object var1){
                            this.invoke((Intent)var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Intent it){
                            Intrinsics.checkNotNullParameter(it,"it");
                            launcher.launch(it);
                        }
                    }));
            }
        });





    }




    private Boolean validateItemName() {
        String val = add_item_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            add_item_name.setError("Field cannot be empty");
            return false;
        }
        else {
            add_item_name.setError(null);
            add_item_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateDescription() {
        String val = add_description.getEditText().getText().toString();

        if (val.isEmpty()) {
            add_description.setError("Field cannot be empty");
            return false;
        }
        else {
            add_description.setError(null);
            add_description.setErrorEnabled(false);
            return true;
        }
    }

    public void AddItem(View v){


        if(!validateItemName() |!validateDescription())
        {




            return;
        }
            mDatabase = FirebaseDatabase.getInstance();
            mRef = mDatabase.getReference("Users").child(UserID).child("items");
            Iname = add_item_name.getEditText().getText().toString();
            Idescription = add_description.getEditText().getText().toString();




            //get all the values
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String Iuid = mRef.push().getKey();

                    ItemHelperClass IHelperClass = new ItemHelperClass(Iname,Idescription,Iuid);
                    mRef.child(Iuid).setValue( IHelperClass);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddMoreItems.this, "error", Toast.LENGTH_SHORT).show();
                }
            });



    }

}