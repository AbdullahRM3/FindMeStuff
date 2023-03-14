package com.r3.findmestuff;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationClass extends FirebaseMessagingService {
    private static final String TAG = "FCMService";
    private static String serverKey = "AAAAaMhu8mY:APA91bHHZQhML2H32agnoCrWt1gYGXz9oIKHaPX3LcJ4qU2-mNy9NsJqU1P6edTywRUiVwZ_a-5Og7fLSLa7pWCS0bj0VMHYTDSJCOI57tEMznUgxrzUm6Dn12qfFEgEyYUnpQwDo0VH";
    private static final String CHANNEL_ID = "LostItem";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.detective)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

}




