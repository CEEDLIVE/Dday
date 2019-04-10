package com.example.ceedlive.dday.util;

import android.content.Context;

import com.example.ceedlive.dday.R;

import java.util.Calendar;

public class CalendarUtil {

    public static String getDiffDays(Context context, Calendar mTargetCalendar, Calendar mBaseCalendar, int year, int month, int day) {
        // Calendar 두 날짜 간 차이 구하기
        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        // 밀리초(1000분의 1초) 단위로 두 날짜 간 차이를 변환 후 초 단위로 다시 변환
        long diffSec = (mTargetCalendar.getTimeInMillis() - mBaseCalendar.getTimeInMillis()) / 1000;
        // 1분(60초), 1시간(60분), 1일(24시간) 이므로 다음과 같이 나누어 1일 단위로 다시 변환
        long diffDays = diffSec / (60 * 60 * 24);

        int flag = diffDays > 0 ? 1 : diffDays < 0 ? -1 : 0;

        final String msg;

        switch (flag) {
            case 1:
                msg = context.getString(R.string.dday_valid_prefix) + Math.abs(diffDays);
                break;
            case 0:
                msg = context.getString(R.string.dday_today);
                break;
            case -1:
                msg = context.getString(R.string.dday_invalid_prefix) + Math.abs(diffDays);
                break;
            default:
                msg = "";
        }

        return msg;
    }

}
