package com.r3.findmestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.core.view.View;

public class ItemScannedResult extends AppCompatActivity {
String dataString;
    String email = "";
    String phone = "";
    String name = "";
    String ItemName = "";
    String ItemDescription = "";
TextView OwnerName,OwnerItemName,OwnerItemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_scanned_result);
// Find the call and email buttons in the layout file
        OwnerName = findViewById(R.id.Owner_Name);
        OwnerItemName = findViewById(R.id.Owner_item_Name);
        OwnerItemDescription = findViewById(R.id.Owner_Item_Description);

        dataString =getIntent().getStringExtra("QR_RESULT");
        // Assuming you have already retrieved the data from the QR code and stored it in dataString variable
// Extract the email and phone from the dataString variable
        String[] data = dataString.split("\n");

        for (String item : data) {
            if (item.startsWith(" Email: ")) {
                email = item.substring(7);
            }if (item.startsWith(" Phone: ")) {
                phone = item.substring(8);
            }if (item.startsWith(" Name: ")) {
                name = item.substring(7);
            }if (item.startsWith(" Item Name: ")) {
                ItemName = item.substring(12);
            }if (item.startsWith(" ItemDescription: ")) {
                ItemDescription = item.substring(18);
            }
        }

        OwnerName.setText(name);
        OwnerItemName.setText(ItemName);
        OwnerItemDescription.setText(ItemDescription);




    }
    public void gotoCallOwner(android.view.View v){


        if (!phone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }




    }
    public void gotoEmailOwner(android.view.View v){


        if (!email.isEmpty()) {
/*            Intent iEmail = new Intent(Intent.ACTION_SENDTO);
            iEmail.putExtra(Intent.EXTRA_SUBJECT, "your Missing Item has been found");
            iEmail.putExtra(Intent.EXTRA_EMAIL, email);
            iEmail.putExtra(Intent.EXTRA_TEXT, "Hi Mr "+name +"I have found Your "+ ItemName);
            iEmail.setData(Uri.parse("mailto:" + email));
 */


/*            String subject = "your Missing Item has been found";
            String body = "Hi Mr "+name +"I have found Your "+ ItemName;

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent, "Send email"));*/

            String recipient = email;
            String subject = "your Missing Item has been found";
            String body = "Hi Mr "+name +" I have found Your "+ ItemName;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Send email"));
            }
        }




    }
    public void gotoSMSOwner(android.view.View v){


        if (!phone.isEmpty()) {
            Intent iMsg = new Intent(Intent.ACTION_SENDTO);
             iMsg.setData(Uri.parse("smsto:" + phone));
             iMsg.putExtra("sms_body","Hi Mr "+name +" I have found Your "+ ItemName);
            startActivity(iMsg);
        }




    }
}