package com.ceedlive.ceeday.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ceedlive.ceeday.Constant;
import com.ceedlive.ceeday.R;
import com.ceedlive.ceeday.activity.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CustomFireBaseMessagingService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;
    private NotificationChannel mNotificationChannel;
    private NotificationCompat.Builder mNotificationBuilder;
    private Resources mResources;

    private final String TAG = "CFBM";

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        mResources = getResources();
        Log.e(TAG, "id: " + FirebaseInstanceId.getInstance().getId());
    }

    @Override
    public void onNewToken(String s) {
        Log.e(TAG, "onNewToken: " + s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: " + remoteMessage);
        Log.e(TAG, "getMessageId: " + remoteMessage.getMessageId());
        Log.e(TAG, "getFrom: " + remoteMessage.getFrom());
        Log.e(TAG, "getMessageType: " + remoteMessage.getMessageType());
        Log.e(TAG, "getTo: " + remoteMessage.getTo());
        Log.e(TAG, "getData: " + remoteMessage.getData());

        // reference: https://medium.com/android-school/firebaseinstanceidservice-is-deprecated-50651f17a148
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e(TAG, "onSuccess > newToken: " + newToken);
            }
        });

        Map<String, String> pushDataMap = remoteMessage.getData();
        sendNotification(pushDataMap);
    }

    private void sendNotification(Map<String, String> dataMap) {
        Log.e(TAG, "sendNotification: " + dataMap);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationChannel = new NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            mNotificationChannel.setDescription("Channel description");
            mNotificationChannel.enableLights(true);
            mNotificationChannel.setLightColor(Color.RED);
            mNotificationChannel.enableVibration(true);
            mNotificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }

        mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constant.NOTIFICATION_CHANNEL_ID);

        mNotificationBuilder
                .setContentTitle(dataMap.get("title"))
                .setContentText(dataMap.get("msg"))
                .setTicker(dataMap.get("msg"))
                .setLargeIcon(BitmapFactory.decodeResource(mResources, R.mipmap.ic_launcher_round))
                .setSmallIcon(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP ?
                        R.drawable.ic_notification_star_white : R.mipmap.ic_launcher)
                .setBadgeIconType(R.drawable.ic_noti_star_checked)
//                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setOngoing(true)
                .setShowWhen(true)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.WHITE,1500,1500)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(contentIntent);

        // FIXME 알림 메시지 수량이 늘어나 그룹으로 묶이는 경우 그룹을 스와이프 하면 노티 삭제됨

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mNotificationBuilder
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        mNotificationManager.notify(0 /* ID of notification */, mNotificationBuilder.build());
    }
}
