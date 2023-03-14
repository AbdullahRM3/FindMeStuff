package com.r3.findmestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LostItems extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference mRef;
    AdapterLostItem adapterLostItem;
    String UserID;
    FirebaseUser firebaseUser;
    TextView noItemsText;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LostItems.this,Dashboard.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_items);
        recyclerView =findViewById(R.id.recyclerview1);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid();
        mRef= FirebaseDatabase.getInstance().getReference("Users").child(UserID).child("items");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = mRef.orderByChild("itemBoolean").equalTo(true);
        FirebaseRecyclerOptions<ItemHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ItemHelperClass>()
                        .setQuery(query, ItemHelperClass.class)
                        .build();
        adapterLostItem = new AdapterLostItem(options);
        recyclerView.setAdapter(adapterLostItem);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapterLostItem.startListening();
        // Check if any items are found

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapterLostItem.stopListening();
    }

}
