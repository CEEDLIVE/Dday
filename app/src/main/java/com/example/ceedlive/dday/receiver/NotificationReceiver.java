package com.example.ceedlive.dday.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    /**
     * 인텐트를 받으면 onReceive() 메소드가 자동으로 호출 된다.
     * BroadcastReceiver 는 Intent 를 받았을 때의 처리를 onReceive 에서 구현합니다.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        Bundle bundle = intent.getExtras();
        String strDate = bundle.getString("date");

        Log.d("NotificationReceiver", "onReceive 호출");
        Log.d("strDate", strDate);

        if ( Intent.ACTION_TIME_TICK.equals( intent.getAction() ) ) {
            // 시간이 변경된 경우 해야 할 작업
            Log.d("ACTION_TIME_TICK", "시간이 변경된 경우 해야 할 작업");
        }

        if ( Intent.ACTION_DATE_CHANGED.equals( intent.getAction() ) ) {
            // 날짜가 변경된 경우 해야 할 작업
            Log.d("NotificationReceiver", "날짜가 변경된 경우 해야 할 작업");
        }

        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
