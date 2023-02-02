package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedItems extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<ItemHelperClass> list;
DatabaseReference mRef;
AdapterItem adapterItem;
String UserID;
    FirebaseUser firebaseUser;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SavedItems.this,Dashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        recyclerView =findViewById(R.id.recyclerview);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        mRef= FirebaseDatabase.getInstance().getReference("Users").child(UserID).child("items");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterItem = new AdapterItem(this,list);
        recyclerView.setAdapter(adapterItem);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ItemHelperClass itemHelperClass = dataSnapshot.getValue(ItemHelperClass.class);
                    list.add(itemHelperClass);


                }
                adapterItem.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}