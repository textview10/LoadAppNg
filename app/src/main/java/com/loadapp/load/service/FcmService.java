package com.loadapp.load.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loadapp.load.R;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.login.LauncherActivity;

import org.jetbrains.annotations.NotNull;

public class FcmService extends FirebaseMessagingService {

    private static final String TAG = "FcmService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, " create .");
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);


    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Constant.mFirebaseToken = s;
        Constant.isNewToken = true;
        Log.e(TAG, " on new token = " +  Constant.mFirebaseToken);
    }

    private int id = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            id++;
            sendNotifyMessage(id, FcmService.this);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotifyMessage(int id, Context context) {
        NotificationUtils.notify(id, new Utils.Consumer<NotificationCompat.Builder>() {
            @Override
            public void accept(NotificationCompat.Builder builder) {
                Intent intent = new Intent(context, LauncherActivity.class);
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("title")
                        .setContentText("content text: test")
                        .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(true);
            }
        });
    }
}
