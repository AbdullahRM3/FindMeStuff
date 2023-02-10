package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
//ArrayList<ItemHelperClass> list;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //list = new ArrayList<>();

        FirebaseRecyclerOptions<ItemHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ItemHelperClass>()
                        .setQuery(mRef, ItemHelperClass.class)
                        .build();
        adapterItem = new AdapterItem(options);
        recyclerView.setAdapter(adapterItem);
 /*       mRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });*/

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapterItem.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapterItem.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtsearch(String str){
        FirebaseRecyclerOptions<ItemHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ItemHelperClass>()
                        .setQuery(mRef.orderByChild("iname").startAt(str).endAt(str+"~"), ItemHelperClass.class)
                        .build();
        adapterItem = new AdapterItem(options);
        adapterItem.startListening();
        recyclerView.setAdapter(adapterItem);


    }
}