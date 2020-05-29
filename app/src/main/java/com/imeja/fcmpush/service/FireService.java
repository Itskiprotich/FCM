package com.imeja.fcmpush.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.imeja.fcmpush.MainActivity;
import com.imeja.fcmpush.R;

import java.util.Map;


public class FireService extends FirebaseMessagingService {

    private static final String TAG = FireService.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e(TAG, "token " + s);
       // RealmUtils.setToken(s);
    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            sendNotification(getValue(remoteMessage.getData(), "title"),getValue(remoteMessage.getData(), "message"));


        }
    }


    private void sendNotification(String title, String body) {

        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(3000); //set your time in milliseconds
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.slogos)
                .setContentTitle(title)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.slogos))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction( R.drawable.ic_remove_red_eye_black_24dp,"View Now", pendingIntent)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());

    }
}
