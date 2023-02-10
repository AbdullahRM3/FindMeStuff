package com.r3.findmestuff;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddMoreItems extends AppCompatActivity {
ImageView add_iimage;
TextInputLayout add_item_name,add_description;
Button add_item_btn;
String Iname,Idescription,UserID,IUrl,Iuid;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    //StorageReference msRef= FirebaseStorage.getInstance().getReference("Item_images");
    Uri ImageUri;
    FirebaseUser firebaseUser;
    FloatingActionButton fab;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_items);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        add_iimage = findViewById(R.id.item_image);
        //hooks
        add_item_name =findViewById(R.id.Item_Name);
        add_description =findViewById(R.id.description);
        add_item_btn =findViewById(R.id.btn_add);














        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                         ImageUri=result.getData().getData();
                        add_iimage.setImageURI(ImageUri);
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


            FirebaseStorage Storage = FirebaseStorage.getInstance();
       final StorageReference msRef = Storage.getReference("images"+new Random().nextInt(50));

            Iname = add_item_name.getEditText().getText().toString();
            Idescription = add_description.getEditText().getText().toString();



            msRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    msRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            IUrl =uri.toString();
                            mDatabase = FirebaseDatabase.getInstance();
                            mRef = mDatabase.getReference("Users").child(UserID).child("items");
                            Iuid = mRef.push().getKey();
                            ItemHelperClass IHelperClass = new ItemHelperClass(Iname,Idescription,Iuid,IUrl);
                            mRef.child(Iuid).setValue( IHelperClass);
                            Toast.makeText(AddMoreItems.this, "Item Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });

            //get all the values




    }


}