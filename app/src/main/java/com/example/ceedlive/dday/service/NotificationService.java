package com.example.ceedlive.dday.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.activity.DetailActivity;
import com.example.ceedlive.dday.activity.MainActivity;
import com.example.ceedlive.dday.data.DdayItem;
import com.example.ceedlive.dday.util.CalendarUtil;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationService extends Service {

    NotificationManager mNotificationManager;
    NotificationChannel mNotificationChannel;
    Thread mThread;
    NotificationCompat.Builder mNotificationBuilder;
    Gson mGson = new Gson();

    boolean mIsRun = true;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    // Service와 Thread가 사용될 시점을 생각해 보자.
    // Thread는 앱이 사용자와 상호작용하는 과정에서 UI Thread가 Block 되지 않기 위한 작업등을 처리하기 위한 Foreground 작업에 적합하고
    // Service는 앱이 사용자와 상호작용하지 않아도 계속 수행되어야 하는 Background 작업에 적합하다고 볼 수 있다.
    // 물론 Service 내부에서 Thread가 사용되어야 하지만 큰 틀에서 봤을 때 위와 같은 개념으로 나눌 수 있을 것이다.

    /**
     * 백그라운드에서 실행되는 동작들이 들어가는 곳입니다.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // 백그라운드에서 실행되는 동작들이 들어가는 곳입니다.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final InnerNotificationServiceHandler handler = new InnerNotificationServiceHandler();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                handler.sendEmptyMessage(0);

                // 서비스 호출 후 안드로이드 기본 메뉴 터치하여 모든 앱 닫기 실행 시 앱이 죽는 버그 발생

                if ( intent.getExtras() != null && intent.getExtras().containsKey(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES) ) {
                    String mSharedPreferencesDataKey = intent.getStringExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES);

                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    // TODO SharedPreferences 더 알아보기
                    // 첫번째 인자 name 은 해당 SharedPreferences 의 이름입니다.
                    // 특정 이름으로 생성할수 있고 해당 이름으로 xml 파일이 생성된다고 생각하시면 됩니다.

                    String jsonStringValue = sharedPreferences.getString(mSharedPreferencesDataKey, "");
                    DdayItem ddayItem = mGson.fromJson(jsonStringValue, DdayItem.class);

                    Message message = handler.obtainMessage();
                    message.obj = ddayItem;

                    handler.sendMessage(message);
                }
            }
        });
        mThread.start();

        // START_STICKY -> START_REDELIVER_INTENT 로 변경
        // reference: https://www.androidpub.com/831853
        // reference: https://hashcode.co.kr/questions/1082/%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%EC%84%9C-start_sticky%EC%99%80-start_not_sticky%EC%9D%98-%EC%B0%A8%EC%9D%B4%EB%8A%94-%EB%AD%94%EA%B0%80%EC%9A%94
        return START_REDELIVER_INTENT;

        // 상시동작
        // 시간기반 동작
        // Service 를 상속 받아 startService 서비스 시작
        // bindService 를 통해 서비스와 연결하여 커뮤니케이션 해당 Service 는 START_STICKY 로 실행
        // reference: https://soundlly.github.io/2016/04/12/android-background-service-checklist/
    }

    /**
     * 서비스가 종료될 때 할 작업
     */
    public void onDestroy() {
        // 서비스가 종료될 때 실행되는 함수가 들어갑니다.
        synchronized (mThread) {
            mIsRun = false;
        }
        mThread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class InnerNotificationServiceHandler extends Handler {

        // 핸들러 메시지큐에 있는 작업을 처리 ( 실제 처리 메소드)
        @Override
        public void handleMessage(android.os.Message msg) {

            DdayItem ddayItem = (DdayItem) msg.obj;
            int requestCode = Integer.parseInt( ddayItem.getUniqueKey().replaceAll(Constant.SHARED_PREFERENCES_KEY_PREFIX, "") );
            int notificationId = requestCode;

            Intent intent = new Intent(NotificationService.this, DetailActivity.class);
            intent.putExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES, ddayItem.getUniqueKey()); //전달할 값
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            // TODO Notification 개념 더 알아보기
            // 별로 중요하지 않은 알림은 소리나 진동없이 왔으면 좋겠고 중요하다고 생각하는 알림은 잠금화면에서도 알려준다면?!
            // 이럴때 유용한게 알림채널(Notification Channel)입니다.
            // Notification Channel을 통해 Notification을 여러가지 용도로 나누어서 관리할 수 있게 만들어 줍니다.
            // 사용자가 직접 각 채널별로 알림중요도나 기타 설정을 변경할 수도 있습니다.
            // 오레오에서부터는 이 Notification Channel을 필수로 만들어 주어야 합니다.
            // 오레오에서 Notification Channel을 만들어 주지 않으면 알림이 오지 않습니다.

            // 해당 기기의 OS 버전이 오레오 이상일때 Notification Channel 을 만들어주고 필요한 설정을 해준뒤
            // NotificationManager의 createNotificationChannel()을 호출해주면 됩니다.

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
                    .setSmallIcon(R.drawable.ic_weekly_calendar)
                    .setContentTitle(ddayItem.getTitle())
                    .setContentText(ddayItem.getDescription())
                    .setBadgeIconType(R.drawable.ic_weekly_calendar)
                    .setOngoing(true)
                    .setShowWhen(true)
                    .setAutoCancel(false)
                    .setTicker("알림!!!")
                    .setContentIntent(pendingIntent);

            // FIXME 알림 메시지 수량이 늘어나 그룹으로 묶이는 경우 그룹을 스와이프 하면 노티 삭제됨

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mNotificationBuilder
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);
            }

            mNotificationManager.notify(notificationId, mNotificationBuilder.build());

            // Set Date
            String selectedDate = ddayItem.getDate();
            String[] arrDate = selectedDate.split("/");

            String strYear = arrDate[0];
            String strMonth = arrDate[1];
            String strDay = arrDate[2];

            int year = Integer.parseInt(strYear);
            int month = Integer.parseInt(strMonth);
            int day = Integer.parseInt(strDay);

            Calendar mTargetCalendar = new GregorianCalendar();
            Calendar mBaseCalendar = new GregorianCalendar();

            String diffDays = CalendarUtil.getDiffDays(NotificationService.this, mTargetCalendar, mBaseCalendar, year, month - 1, day);

            try {
                Thread.sleep(10000);
                mNotificationBuilder.setContentText(diffDays);
                mNotificationManager.notify(notificationId, mNotificationBuilder.build());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // NotificationManager
            // notify(int id, Notification notification): 알림을 발생시킨다. id는 알림을 구분하는 식별자, 존재하는 알림의 id를 사용하면 알림이 update된다.
            // cancel(int id): 주어지는 id에 해당하는 알림을 취소한다.
            // cancelAll(): 현재 발생된 모든 알림을 취소한다.

            // FIXME TEST 토스트 띄우기
//            Toast.makeText(NotificationService.this, "뜸?", Toast.LENGTH_LONG).show();
        }
    }

}
