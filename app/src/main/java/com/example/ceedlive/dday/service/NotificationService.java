package com.example.ceedlive.dday.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.activity.MainActivity;

public class NotificationService extends Service {

    NotificationManager mNotificationManager;
    Thread mThread;
    Notification mNotification;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 백그라운드에서 실행되는 동작들이 들어가는 곳입니다.
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final myServiceHandler handler = new myServiceHandler();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        });
        mThread.start();
        return START_STICKY;

        // 상시동작
        // 시간기반 동작
        // Service 를 상속 받아 startService 서비스 시작
        // bindService 를 통해 서비스와 연결하여 커뮤니케이션해당 Service 는 START_STICKY 로 실행
        // reference: https://soundlly.github.io/2016/04/12/android-background-service-checklist/
    }

    // 서비스가 종료될 때 할 작업
    public void onDestroy() {
        // 서비스가 종료될 때 실행되는 함수가 들어갑니다.
        synchronized (mThread) {
            mIsRun = false;
        }
        mThread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(NotificationService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            mNotification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Content Title")
                    .setContentText("Content Text")
                    .setSmallIcon(R.drawable.ic_weekly_calendar)
                    .setTicker("알림!!!")
                    .setContentIntent(pendingIntent)
                    .build();

            // 소리추가
            mNotification.defaults = Notification.DEFAULT_SOUND;

            // 알림 소리를 한번만 내도록
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            // 확인하면 자동으로 알림이 제거 되도록
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;


            mNotificationManager.notify( 777 , mNotification);

            //토스트 띄우기
            Toast.makeText(NotificationService.this, "뜸?", Toast.LENGTH_LONG).show();
        }
    };

}
