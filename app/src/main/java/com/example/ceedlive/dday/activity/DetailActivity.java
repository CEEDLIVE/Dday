package com.example.ceedlive.dday.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.dto.DdayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends BaseActivity {

    private TextView mTvToday, mTvDate;
    private int mYear, mMonth, mDay;
    private Button mBtnSave;
    private EditText mEtTitle, mEtDescription;
    private CheckBox mCheckBoxAddNoti;

    private String mSharedPreferencesDataKey;

    private Calendar mTargetCalendar, mBaseCalendar;
    private Intent mIntent;
    private String mDiffDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();
        setEvent();
    }

    @Override
    protected void initialize() {

        mIntent = getIntent();

        // 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        mTargetCalendar = new GregorianCalendar();
        mBaseCalendar = new GregorianCalendar();

        mTvToday = findViewById(R.id.detail_tv_today);
        mTvDate = findViewById(R.id.detail_tv_date);
        mEtTitle = findViewById(R.id.detail_et_title);
        mEtDescription = findViewById(R.id.detail_et_description);

        mCheckBoxAddNoti = findViewById(R.id.detail_checkbox_add_noti);

        if (mIntent.getExtras() != null && mIntent.getExtras().containsKey(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES)) {
            mSharedPreferencesDataKey = mIntent.getStringExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES);

            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            // TODO SharedPreferences 더 알아보기
            // 첫번째 인자 name 은 해당 SharedPreferences 의 이름입니다.
            // 특정 이름으로 생성할수 있고 해당 이름으로 xml 파일이 생성된다고 생각하시면 됩니다.

            String jsonStringValue = sharedPreferences.getString(mSharedPreferencesDataKey, "");

            DdayItem ddayItem = gson.fromJson(jsonStringValue, DdayItem.class);

            // Set Date
            String selectedDate = ddayItem.getDate();
            String[] arrDate = selectedDate.split("/");
            String year = arrDate[0], month = arrDate[1], day = arrDate[2];

            mTargetCalendar.set(Calendar.YEAR, Integer.parseInt(year));
            mTargetCalendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            mTargetCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

            // Set Title
            mEtTitle.setText(ddayItem.getTitle());

            // Set Description
            mEtDescription.setText(ddayItem.getDescription());
        }

        mYear = mTargetCalendar.get(Calendar.YEAR);
        mMonth = mTargetCalendar.get(Calendar.MONTH);
        mDay = mTargetCalendar.get(Calendar.DAY_OF_MONTH);

        mBtnSave = findViewById(R.id.detail_btn_save);

        // 텍스트뷰의 값을 업데이트함
        doUpdateText(mYear, mMonth, mDay);
    }

    private void setEvent() {
        onClickDate();
        onClickSave();
    }

    private void onClickDate() {
        // 날짜 텍스트뷰 클릭 시 이벤트
        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        DetailActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 사용자가 입력한 값을 가져온뒤
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                // 텍스트뷰의 값을 업데이트함
                                doUpdateText(mYear, mMonth, mDay);
                            }
                        }, mYear, mMonth, mDay).show();
            }
        });
    }

    /**
     * 저장 버튼 클릭 시 이벤트
     */
    private void onClickSave() {
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 사용자가 입력한 저장할 데이터
                final String date = mTvDate.getText().toString();
                final String title = mEtTitle.getText().toString();
                final String description = mEtDescription.getText().toString();

                // 데이터 유효성 검사
                if ( "".equals( title.trim() ) ) {
                    showDialog("경고", "디데이 제목을 입력하세요.");
                    return;
                }

                // SharedPreferences 를 ceedliveAppDday 이름, 기본모드로 설정
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

                // How to get all keys of SharedPreferences programmatically in Android?
                // reference: https://stackoverflow.com/questions/22089411/how-to-get-all-keys-of-sharedpreferences-programmatically-in-android
                Map<String, ?> allEntries = sharedPreferences.getAll();
                String eachKey = "";
                int eachKeyNumber;
                int maxKeyNumber;
                List<Integer> keyNumberList = new ArrayList<>();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    eachKey = entry.getKey();
                    if (!"".equals(eachKey) && eachKey.startsWith(Constant.SHARED_PREFERENCES_KEY_PREFIX)) {
                        // ceedlive 디데이 앱에서 사용하는 SharedPreferences
                        eachKeyNumber = Integer.parseInt(eachKey.replace(Constant.SHARED_PREFERENCES_KEY_PREFIX, ""));
                        keyNumberList.add(eachKeyNumber);
                        continue;
                    }
                    keyNumberList.clear();
                    keyNumberList.add(0);
                    break;
                }

                // IndexOutOfBoundsException 방지
                if (keyNumberList.isEmpty()) {
                    keyNumberList.add(0);
                }

                // 리스트 역순으로 뒤집기
                Collections.sort(keyNumberList);
                Collections.reverse(keyNumberList);
                maxKeyNumber = keyNumberList.get(0);

                // 저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();

                final String uniqueKey = mSharedPreferencesDataKey == null ? Constant.SHARED_PREFERENCES_KEY_PREFIX + (maxKeyNumber + 1) : mSharedPreferencesDataKey;

                DdayItem ddayItem = new DdayItem();
                ddayItem.setDate(date);
                ddayItem.setTitle(title);
                ddayItem.setDescription(description);
                ddayItem.setUniqueKey(uniqueKey);
                ddayItem.setDiffDays(mDiffDays);

                // JSON 으로 변환
                final String jsonStringAnniversaryInfo = gson.toJson(ddayItem, DdayItem.class);

                // key/value pair 로 값을 저장하는 형태
                editor.putString(uniqueKey, jsonStringAnniversaryInfo);
                editor.apply();

                // ? editor.commit(); 최종 커밋
                // Consider using apply() instead; commit writes its data to persistent storage immediately, whereas apply will handle it in the background less... (Ctrl+F1)
                // Inspection info:Consider using apply() instead of commit on shared preferences. Whereas commit blocks and writes its data to persistent storage immediately, apply will handle it in the background.

                Intent intent = new Intent();
                intent.putExtra("newItem", jsonStringAnniversaryInfo);
                setResult(Activity.RESULT_OK, intent);

                if ( mCheckBoxAddNoti.isChecked() ) {
                    NotificationDday(ddayItem);
                }

                finish();
            }
        });
    }

    /**
     * 다이얼로그 출력
     */
    private void showDialog(String title, String message) {
        // 다이얼로그
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 다이얼로그 값/옵션 세팅
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다.
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // reference: https://mainia.tistory.com/2017
        // reference: https://m.blog.naver.com/PostView.nhn?blogId=sgepyh2916&logNo=221176134263&proxyReferer=https%3A%2F%2Fwww.google.com%2F
    }

    /**
     * 두 날짜 간 차이 구하기
     * @param year
     * @param month
     * @param day
     * @return
     */
    private String getDiffDays(int year, int month, int day) {
        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        // 밀리초(1000분의 1초) 단위로 두 날짜 간 차이를 변환 후 초 단위로 다시 변환
        long diffSec = (mTargetCalendar.getTimeInMillis() - mBaseCalendar.getTimeInMillis()) / 1000;
        // 1분(60초), 1시간(60분), 1일(24시간) 이므로 다음과 같이 나누어 1일 단위로 다시 변환
        long diffDays = diffSec / (60 * 60 * 24);

        int flag = diffDays > 0 ? 1 : diffDays < 0 ? -1 : 0;

        String msg = "";

        switch (flag) {
            case 1:
                msg = getString(R.string.dday_valid_prefix) + Math.abs(diffDays);
                break;
            case 0:
                msg = getString(R.string.dday_today);
                break;
            case -1:
                msg = getString(R.string.dday_invalid_prefix) + Math.abs(diffDays);
                break;
            default:
                msg = "";
        }

        return msg;
    }

    /**
     *
     */
    private void doUpdateText(int year, int month, int day) {
        mTvDate.setText(String.format(Locale.getDefault(),
                Constant.CALENDAR_STRING_FORMAT_SLASH, mYear, mMonth + 1, mDay));

        mDiffDays = getDiffDays(year, month, day);
        mTvToday.setText(mDiffDays);
    }

    public void NotificationDday(DdayItem ddayItem) {

        Resources res = getResources();

        int requestCode = Integer.parseInt( ddayItem.getUniqueKey().replaceAll(Constant.SHARED_PREFERENCES_KEY_PREFIX, "") );

        Intent notificationIntent = new Intent(this, DetailActivity.class);
        notificationIntent.putExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES, ddayItem.getUniqueKey()); //전달할 값
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, requestCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // FIXME 2019-04-09 Tue
        // Task 연동을 통해서 Github 와 연동 (Jetbrains의 IDE 사용 시 적극 권장) 테스트용 주석입니다.
        // Issue 기반 Branch 생성 및 커밋 테스트용 주석입니다.

        // TODO 개별 PendingIntent 생성 더 알아보기
        // 핵심은 getActivity 파라메터중 두번째 requestCode 와 마지막 flags 이다.
        // 흔히 requestCode = 0, flags = FLAG_UPDATE_CURRENT 를 사용한다.
        // 개별적인 작업을 하기 위해서는 pendingIntent를 생성할때마다 requestCode를 다르게 할당하고
        // 서로의 충돌을 피하기 위해서 flags는 FLAG_CANCEL_CURRENT 로 호출해야 한다.

        // PendingIntent
        // 인텐트를 포함하는 인텐트, 사용하는 목적은 현재 앱이 아닌 외부의 앱(노티피케이션, 알람 등)이 현재 내가 개발한 앱을 열 수 있도록 허락할 수 있는 인텐트
        // 펜딩 인텐트 안에는 실제 데이터를 가지고 열 액티비티를 저장한 인텐트를 갖고 있는 것과 같다.

        // 내가 개발한 앱 안에서 A라는 액티비티에서 B라는 액티비티를 열려면 Intent intent = new Intent(A.this, B.class) 로 하여 startActivity(intent) 를 함.
        // 외부에서는 이 intent 를 포함하고 있는 PendingIntent 를 선언하여 intent 를 품게 한 뒤 사용하게 하는 것이다.

        NotificationCompat.Builder notificationBuilder;

        // TODO Notification 개념 더 알아보기
        // 별로 중요하지 않은 알림은 소리나 진동없이 왔으면 좋겠고 중요하다고 생각하는 알림은 잠금화면에서도 알려준다면?!
        // 이럴때 유용한게 알림채널(Notification Channel)입니다.
        // Notification Channel을 통해 Notification을 여러가지 용도로 나누어서 관리할 수 있게 만들어 줍니다.
        // 사용자가 직접 각 채널별로 알림중요도나 기타 설정을 변경할 수도 있습니다.
        // 오레오에서부터는 이 Notification Channel을 필수로 만들어 주어야 합니다.
        // 오레오에서 Notification Channel을 만들어 주지 않으면 알림이 오지 않습니다.

        // 해당 기기의 OS 버전이 오레오 이상일때 Notification Channel 을 만들어주고 필요한 설정을 해준뒤
        // NotificationManager의 createNotificationChannel()을 호출해주면 됩니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // TODO 의미 다시 한번 살펴보기
        // 채널은 한번만 만들면 되기때문에 Notification이 올때마다 만들어줄 필요가 없습니다.
        // Application Class에서 만들어 줘 되고 SharedPreference를 이용해서 한번 만든적이 있다면 그다음부터는 만들지 않도록 해주어도 됩니다.

        notificationBuilder = new NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANNEL_ID);

        notificationBuilder
                .setContentTitle(ddayItem.getTitle())
                .setContentText(ddayItem.getDate())
                .setTicker(ddayItem.getTitle())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(mPendingIntent)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Integer.parseInt(ddayItem.getUniqueKey().replaceAll(Constant.SHARED_PREFERENCES_KEY_PREFIX, "")), notificationBuilder.build());
    }

}
