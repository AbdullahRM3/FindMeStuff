package com.r3.findmestuff;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterItem extends FirebaseRecyclerAdapter<ItemHelperClass,AdapterItem.MyViewHolder>{


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
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ItemHelperClass model) {
        String UserID;
        DatabaseReference mRef;
        FirebaseUser firebaseUser;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        mRef= FirebaseDatabase.getInstance().getReference("Users").child(UserID).child("items");
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

                        mRef.child(getRef(position).getKey()).updateChildren(map).
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

                Button DownloadQR_item = v.findViewById(R.id.btn_Download);
                MultiFormatWriter writer= new MultiFormatWriter();
                try {
                    BitMatrix matrix= writer.encode(mRef.child(getRef(position).getKey()).toString(), BarcodeFormat.QR_CODE,600,600);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    Qrview.setImageBitmap(bitmap);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }

                dialogPlus.show();

                DownloadQR_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



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
                        mRef.child(getRef(position).getKey()).removeValue();
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
       /* holder.btn_Qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemName.getContext());
                builder.setTitle("generate QR or Barcode ");
                builder.setMessage("Choose Qr code or Barcode for your Item");
                builder.setPositiveButton("Qr Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.itemName.getContext(), "Qr Code Generated", Toast.LENGTH_SHORT).show();
                        mRef.child(getRef(position).getKey()).removeValue();
                        QrCodeGenerate();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.itemName.getContext(), "canceled", Toast.LENGTH_SHORT).show();
                    }

                });

                builder.show();
            }
        });*/
    }

    private void QrCodeGenerate() {

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
            Button btn_edit,btn_delete,btn_Qr;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName=itemView.findViewById(R.id.item_Name);
                itemPic=itemView.findViewById(R.id.item_pic);
                btn_delete=itemView.findViewById(R.id.btn_delete);
                btn_edit=itemView.findViewById(R.id.btn_edit);
                btn_Qr=itemView.findViewById(R.id.btn_Qr);

            }
        }


}

/*
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyViewHolder> {
    Context context;
    ArrayList<ItemHelperClass> list;

    public AdapterItem(Context context, ArrayList<ItemHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.card_view_items,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.MyViewHolder holder, int position) {
ItemHelperClass itemHelperClass = list.get(position);
        holder.itemName.setText(itemHelperClass.getIname());
        Glide.with(holder.itemPic.getContext())
                .load(itemHelperClass.getIUrl())
                .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.itemPic);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        CircleImageView itemPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_Name);
            itemPic=itemView.findViewById(R.id.item_pic);

        }
    }
}
*/
