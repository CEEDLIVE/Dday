package com.example.ceedlive.dday.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ceedlive.dday.receiver.PackageEventReceiver;

public class PackageEventService extends Service {

    Thread mThread;
    PackageEventReceiver mPackageEventReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // do Something
            }
        });
        mThread.start();

        return START_STICKY;
    }
}
