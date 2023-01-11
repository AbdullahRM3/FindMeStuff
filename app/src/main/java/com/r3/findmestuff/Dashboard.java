package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

    public void gotoLostItems(View v){


        Intent intent = new Intent(Dashboard.this,LostItems.class);


        startActivity(intent);




    }
}