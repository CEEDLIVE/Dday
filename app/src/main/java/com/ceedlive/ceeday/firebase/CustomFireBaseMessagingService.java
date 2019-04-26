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
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
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
        Log.e(TAG, "CustomFireBaseMessagingService onCreate");
        Log.e(TAG, "    id: " + FirebaseInstanceId.getInstance().getId());
    }

    // INFO
    // FirebaseInstanceIdService
    // This class is deprecated.
    // In favour of overriding onNewToken in FirebaseMessagingService. Once that has been implemented, this service can be safely removed.
    // This method is deprecated.
    // In favour of overriding onNewToken in FirebaseMessagingService. This method will be invoked on token changes even if onNewToken is also used.

    // Called when the system determines that the tokens need to be refreshed. The application should call getToken() and send the tokens to all application servers.

    // This will not be called very frequently, it is needed for key rotation and to handle Instance ID changes due to:

    // App deletes Instance ID
    // App is restored on a new device
    // User uninstalls/reinstall the app
    // User clears app data
    // The system will throttle the refresh event across all devices to avoid overloading application servers with token updates.

    // reference: https://firebase.google.com/docs/reference/android/com/google/firebase/iid/FirebaseInstanceIdService

    /*
    둘 다 포함된 메시지를 수신한 경우의 앱 동작

    앱이 백그라운드 상태인지, 포그라운드 상태인지에 따라 다르게 동작.
            특히, 수신 당시에 앱이 활성 상태였는지 여부가 영향을 미친다.

    백그라운드 상태인 경우 :
            (Notification) 알림 페이로드가 앱의 알림 목록에 수신됨
            (Data) 사용자가 알림을 탭(오픈)한 경우에만, 앱이 데이터 페이로드를 처리

    포그라운드 상태인 경우 :
            앱에서 페이로드가 둘 다 제공되는 메시지 개체를 수신
    */


    @Override
    public void onNewToken(String s) {
        Log.e(TAG, "CustomFireBaseMessagingService onNewToken: " + s);
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    /**
     * FCM 토큰 갱신
     * @param token 새로운 토큰
     */
    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
        // TODO 네트워크 관련 라이브러리를 사용하여 서버로 토큰을 보내는 로직 구현
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "CustomFireBaseMessagingService onMessageReceived: " + remoteMessage);
        Log.e(TAG, "    getMessageId: " + remoteMessage.getMessageId());
        Log.e(TAG, "    getFrom: " + remoteMessage.getFrom());
        Log.e(TAG, "    getMessageType: " + remoteMessage.getMessageType());
        Log.e(TAG, "    getTo: " + remoteMessage.getTo());
        Log.e(TAG, "    getData: " + remoteMessage.getData());
        Log.e(TAG, "    getData title: " + remoteMessage.getData().get("title"));
        Log.e(TAG, "    getData message: " + remoteMessage.getData().get("message"));

        // Manage data
        /* You can read more on notification here:
        https://developer.android.com/training/notify-user/build-notification.html
        https://www.youtube.com/watch?v=-iog_fmm6mE
        */

        // FirebaseMessagingService.onMessageReceived 메소드를 재정의하면
        // 수신된 RemoteMessage 객체를 기준으로 작업을 수행하고 메시지 데이터를 가져올 수 있습니다.
        // 모든 메시지는 10초 이내에 수신 처리 되어야 하며
        // 10초 이상의 실행 작업의 경우 Firebase Job Dispatcher를 사용하면 됩니다.
        // 출처: https://faith-developer.tistory.com/158 [개발 이야기]

        // reference: https://medium.com/android-school/firebaseinstanceidservice-is-deprecated-50651f17a148
        // FirebaseInstanceId.getInstance().getToken() deprecated

        // TEST
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e(TAG, "CustomFireBaseMessagingService onSuccess > newToken: " + newToken);
            }
        });

        // 데이터 메시지를 분기하여줍니다.
        // Check if message contains a data payload.
        if ( remoteMessage.getData().size() > 0 ) {
            Log.e(TAG, "    Message data payload: " + remoteMessage.getData());
            // 사용자에게 먼저 메시지를 보여줍니다.
            Map<String, String> pushDataMap = remoteMessage.getData();
            sendNotification(pushDataMap);

            // TODO 수신받은 데이터를 처리합니다.
            updateContent();

            /* TODO 수신받은 데이터가 크거나 시간이 20초 이상 소요된다면 WorkManager 혹은 Job Schduler를 사용합니다. */
            if (remoteMessage.getData().get("url") != null) {
                scheduleJob();
            }

            // TODO TEST
            setAlarmBadge();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "    Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void updateContent() {
        Log.e(TAG, "CustomFireBaseMessagingService updateContent");
    }

    /**
     * 앱에서 메시지를 처리하는 데 시간이 더 필요하면 Firebase Job Dispatcher를 사용
     */
    private void scheduleJob() {
        // [START dispatch_job]
        Log.e(TAG, "CustomFireBaseMessagingService scheduleJob");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(CustomFireBaseJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    private void sendNotification(Map<String, String> dataMap) {
        Log.e(TAG, "CustomFireBaseMessagingService sendNotification: " + dataMap);
        Intent intent = new Intent(CustomFireBaseMessagingService.this, MainActivity.class);
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
            mNotificationChannel.setShowBadge(true);// 여기서 뱃지 여부 결정.
            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }

        mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constant.NOTIFICATION_CHANNEL_ID);

        mNotificationBuilder
                .setContentTitle(dataMap.get("title"))
                .setContentText(dataMap.get("msg"))
                .setSubText("subtext")
                .setTicker("알람 간단한 설명")
                .setLargeIcon(BitmapFactory.decodeResource(mResources, R.mipmap.ic_launcher_round))
                .setSmallIcon(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP ?
                        R.mipmap.ic_launcher : R.mipmap.ic_launcher)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setShowWhen(true)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.WHITE,1500,1500)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentInfo("contentInfo")
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("긴 텍스트 내용\n하이하이\n하이하이2"))
                ;

        // setOngoing: 알림의 지속성
        // 알림 리스트에서 사용자가 그것을 클릭하거나 좌우로 드래그해도 사라지지 않음

        // FIXME 알림 메시지 수량이 늘어나 그룹으로 묶이는 경우 그룹을 스와이프 하면 노티 삭제됨

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mNotificationBuilder
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        mNotificationManager.notify(0 /* ID of notification */, mNotificationBuilder.build());
    }


    private void setAlarmBadge() {
        // 배지 서비스?
        // 디바이스, 제조사마다 제한이 있으니 확인해보고 적용을 시킨다.

    }
}
