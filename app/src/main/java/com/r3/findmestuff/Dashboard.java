package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();





    }


    public void gotoqrcodescanner(View v){


        Intent intent = new Intent(Dashboard.this,QrCodeScanner.class);


        startActivity(intent);




    }

    public void gotoAddMoreItems(View v){


        Intent intent = new Intent(Dashboard.this,AddMoreItems.class);


        startActivity(intent);




    }

    public void gotoSavedItems(View v){


        Intent intent = new Intent(Dashboard.this,SavedItems.class);


        startActivity(intent);




    }
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void SignOutUser(View v){

       mAuth.signOut();
        Intent intent = new Intent(Dashboard.this,Login.class);


        startActivity(intent);




    }
    public void gotoUserProfile(View v){


        Intent intent = new Intent(Dashboard.this,UserProfile.class);


        startActivity(intent);




    }

    public void gotoLostItems(View v){


        Intent intent = new Intent(Dashboard.this,LostItems.class);


        startActivity(intent);




    }
}