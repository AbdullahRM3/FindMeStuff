package com.r3.findmestuff;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;




public class AdapterLostItem extends FirebaseRecyclerAdapter<ItemHelperClass, AdapterLostItem.MyViewHolder> {
    String UserID;
    DatabaseReference mRef;
    FirebaseUser firebaseUser;

    public AdapterLostItem(@NonNull FirebaseRecyclerOptions<ItemHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull ItemHelperClass model) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        mRef= FirebaseDatabase.getInstance().getReference("Users").child(UserID).child("items");

        // Check if the itemBoolean is true
        if (model.isItemBoolean()) {
            holder.itemName.setText(model.getIname());
            holder.itemDescription.setText(model.getIdescription());

            Glide.with(holder.itemPic.getContext())
                    .load(model.getIUrl())
                    .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                    .circleCrop()
                    .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                    .into(holder.itemPic);

            holder.btn_found.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRef.child(getRef(position).getKey()).child("itemBoolean").setValue(false);
                    Toast.makeText(holder.btn_found.getContext(), "Item has been found.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_lost_items, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription;
        CircleImageView itemPic;
        Button btn_found;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_LName);
            itemDescription = itemView.findViewById(R.id.item_LDescription);
            itemPic = itemView.findViewById(R.id.itemlost_pic);
            btn_found = itemView.findViewById(R.id.btn_Found);
        }
    }
}


