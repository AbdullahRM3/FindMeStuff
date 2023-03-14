package com.r3.findmestuff;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterItem extends FirebaseRecyclerAdapter<ItemHelperClass,AdapterItem.MyViewHolder>{
    String UserID;
    DatabaseReference mRef;
    FirebaseUser firebaseUser;
    Bitmap bitmap;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public AdapterItem(@NonNull FirebaseRecyclerOptions<ItemHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position , @NonNull ItemHelperClass model) {

        String CHANNEL_ID = "LostItem";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        mRef= FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        holder.itemName.setText(model.getIname());

        Glide.with(holder.itemPic.getContext())
                .load(model.getIUrl())
                .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.itemPic);


        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_item_details))
                        .setExpanded(true,890)
                        .create();

                View v = dialogPlus.getHolderView();
               EditText ItemName = v.findViewById(R.id.update_Item_Name);
              EditText ItemDescription = v.findViewById(R.id.update_description);


                Button update_item = v.findViewById(R.id.btn_update_item);

               ItemName.setText(model.getIname());
               ItemDescription.setText(model.getIdescription());
                dialogPlus.show();

                update_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("iname",ItemName.getText().toString());
                        map.put("idescription",ItemDescription.getText().toString());

                        mRef.child("items").child(getRef(position).getKey()).updateChildren(map).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.itemName.getContext(), "Data Updated", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.itemName.getContext(), "Updated Failed", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });


            }
        });
        holder.btn_Qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.generate_qr))
                        .setExpanded(true,890)
                        .create();
                View v = dialogPlus.getHolderView();
               ImageView Qrview = v.findViewById(R.id.IV_Qr);

               mRef.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       UserHelperClass helperClass = snapshot.getValue(UserHelperClass.class);
                       String name = helperClass.getUsername();
                       String email = helperClass.getEmail();
                       String phone = helperClass.getPhone();

                       mRef.child("items").child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               ItemHelperClass IhelperClass = snapshot.getValue(ItemHelperClass.class);
                               String iname = IhelperClass.getIname();
                               String idescription = IhelperClass.getIdescription();

                               String dataString = " Name: "+ name + "\n Email: " + email + "\n Phone: "+ phone + "\n Item Name: " + iname + "\n ItemDescription: " + idescription;
                               MultiFormatWriter writer= new MultiFormatWriter();
                               try {
                                   BitMatrix matrix= writer.encode(dataString, BarcodeFormat.QR_CODE,600,600);
                                   BarcodeEncoder encoder = new BarcodeEncoder();
                                   bitmap = encoder.createBitmap(matrix);
                                   Qrview.setImageBitmap(bitmap);

                               }
                               catch (WriterException e){
                                   e.printStackTrace();
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });



                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                       Toast.makeText(holder.itemPic.getContext(), "error:"+ error, Toast.LENGTH_SHORT).show();
                   }
               });

                Button DownloadQR_item = v.findViewById(R.id.btn_Download);


                dialogPlus.show();

                DownloadQR_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       try {
                           // Get the path to the Pictures directory
                           String picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                           String fname = "my_image_" + System.currentTimeMillis() + ".jpg";
                           // Create a new file in the Pictures directory with a unique name
                           File imageFile = new File(picturesDir, fname);

                           // Create an output stream for the file
                           OutputStream outputStream = new FileOutputStream(imageFile);

                           // Compress the bitmap and write it to the output stream
                           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                           // Flush and close the output stream
                           outputStream.flush();
                           outputStream.close();

                           // Use the MediaStore API to insert the new image into the gallery
                           ContentResolver resolver = holder.itemName.getContext().getContentResolver();
                           ContentValues contentValues = new ContentValues();
                           contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fname);
                           contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                           contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                           Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                           // Open an output stream to write the image data to the content provider
                           OutputStream imageStream = resolver.openOutputStream(imageUri);
                           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
                           imageStream.close();

                           // Display a toast message to confirm the download
                           Toast.makeText(holder.itemName.getContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show();
                       }
                       catch (IOException e) {
                           e.printStackTrace();
                       }


                    }
                });


            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemName.getContext());
                builder.setTitle("Are you Sure??");
                builder.setMessage("deleted Items cant be undo");
                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.itemName.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                        mRef.child("items").child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.itemName.getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }

                });

                builder.show();
            }
        });
        holder.btn_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.lost_item_details))
                        .setExpanded(true,890)
                        .create();

                View v = dialogPlus.getHolderView();

                EditText LostItemTitle = v.findViewById(R.id.Lost_Item_Title);
                EditText LostDescription = v.findViewById(R.id.Lost_description);



                Button lost_item = v.findViewById(R.id.btn_lost_item);

                dialogPlus.show();

                lost_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("450039312998" + "@gcm.googleapis.com")
                                .setMessageId(Integer.toString(new Random().nextInt(1000)))
                                .addData("message", "YOUR_MESSAGE")
                                .build());
                        

                        mRef.child("items").child(getRef(position).getKey()).child("itemBoolean").setValue(true);
                        Toast.makeText(holder.btn_lost.getContext(), "Item has been lost.", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });


    }







    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_items,parent,false);

        return new MyViewHolder(v);

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView itemName;
            CircleImageView itemPic;
            Button btn_edit,btn_delete,btn_Qr,btn_lost;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName=itemView.findViewById(R.id.item_Name);
                itemPic=itemView.findViewById(R.id.item_pic);
                btn_delete=itemView.findViewById(R.id.btn_delete);
                btn_edit=itemView.findViewById(R.id.btn_edit);
                btn_Qr=itemView.findViewById(R.id.btn_Qr);
                btn_lost =itemView.findViewById(R.id.btn_lost);

            }
        }



}


