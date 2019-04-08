package com.example.ceedlive.dday.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

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

        if (mIntent.getExtras() != null && mIntent.getExtras().containsKey("sharedPreferencesDataKey")) {
            mSharedPreferencesDataKey = mIntent.getStringExtra("sharedPreferencesDataKey");

            SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
            String jsonStringValue = sharedPreferences.getString(mSharedPreferencesDataKey, "");

            AnniversaryInfo anniversaryInfo = gson.fromJson(jsonStringValue, AnniversaryInfo.class);

            // Set Date
            String selectedDate = anniversaryInfo.getDate();
            String[] arrDate = selectedDate.split("/");
            String year = arrDate[0], month = arrDate[1], day = arrDate[2];

            mTargetCalendar.set(Calendar.YEAR, Integer.parseInt(year));
            mTargetCalendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            mTargetCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

            // Set Title
            mEtTitle.setText(anniversaryInfo.getTitle());

            // Set Description
            mEtDescription.setText(anniversaryInfo.getDescription());
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

                // SharedPreferences를 sFile이름, 기본모드로 설정
                SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

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

                AnniversaryInfo anniversaryInfo = new AnniversaryInfo();
                anniversaryInfo.setDate(date);
                anniversaryInfo.setTitle(title);
                anniversaryInfo.setDescription(description);
                anniversaryInfo.setUniqueKey(uniqueKey);
                anniversaryInfo.setDiffDays(mDiffDays);

                // JSON 으로 변환
                final String jsonStringAnniversaryInfo = gson.toJson(anniversaryInfo, AnniversaryInfo.class);

                // key/value pair 로 값을 저장하는 형태
                editor.putString(uniqueKey, jsonStringAnniversaryInfo);
                editor.apply();

                // ? editor.commit(); 최종 커밋
                // Consider using apply() instead; commit writes its data to persistent storage immediately, whereas apply will handle it in the background less... (Ctrl+F1)
                // Inspection info:Consider using apply() instead of commit on shared preferences. Whereas commit blocks and writes its data to persistent storage immediately, apply will handle it in the background.

                Intent intent = new Intent();
                intent.putExtra("newItem", jsonStringAnniversaryInfo);
                setResult(Activity.RESULT_OK, intent);

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

}
