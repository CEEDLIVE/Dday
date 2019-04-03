package com.example.ceedlive.dday.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.dto.AnniversaryInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends BaseActivity {

    private Gson gson;

    private TextView tvToday, tvDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button btnDetailMerge;
    private EditText etTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();
        setEvent();
    }

    @Override
    protected void initialize() {
        etTitle = findViewById(R.id.detail_et_title);
        tvToday = findViewById(R.id.tvToday);
        tvDate = findViewById(R.id.detail_tv_date);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        btnDetailMerge = findViewById(R.id.btn_detail_merge);
    }

    private void setEvent() {

        mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub

                        //사용자가 입력한 값을 가져온뒤
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        //텍스트뷰의 값을 업데이트함
                        doUpdateTextViewDate();
                    }
                };

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailActivity.this, mDateSetListener, mYear,
                        mMonth, mDay).show();
            }
        });

        btnDetailMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doUpdateTextViewDate() {
        tvDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", mYear, mMonth + 1, mDay));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Activity가 종료되기 전에 저장한다.
        // SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

        // How to get all keys of SharedPreferences programmatically in Android?
        Map<String, ?> allEntries = sharedPreferences.getAll();
        String eachKey = "";
        int eachKeyNumber;
        int maxKeyNumber;
        List<Integer> keyNumberList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("detail map values", entry.getKey() + ": " + entry.getValue().toString());
            eachKey = entry.getKey();
            if (!"".equals(eachKey) && eachKey.startsWith("ceedlive.dday")) {
                // ceedlive 디데이 앱에서 사용하는 SharedPreferences
                eachKeyNumber = Integer.parseInt(eachKey.replace("ceedlive.dday", ""));
                keyNumberList.add(eachKeyNumber);
                continue;
            }
            keyNumberList.add(0);
            break;
        }

        // 리스트 역순으로 뒤집기
        Collections.reverse(keyNumberList);
        maxKeyNumber = keyNumberList.get(0);

        // 저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 사용자가 입력한 저장할 데이터
        final String date = tvDate.getText().toString();
        final String title = etTitle.getText().toString();

        AnniversaryInfo anniversaryInfo = new AnniversaryInfo();
        anniversaryInfo.setDate(date);
        anniversaryInfo.setTitle(title);

        // Gson 인스턴스 생성
        gson = new GsonBuilder().create();
        // JSON 으로 변환
        final String jsonStringAnniversaryInfo = gson.toJson(anniversaryInfo, AnniversaryInfo.class);

        // key, value를 이용하여 저장하는 형태
        editor.putString("ceedlive.dday" + maxKeyNumber + 1, jsonStringAnniversaryInfo);

        // 다양한 형태의 변수값을 저장할 수 있다.
//        editor.putString();
//        editor.putBoolean();
//        editor.putFloat();
//        editor.putLong();
//        editor.putInt();
//        editor.putStringSet();

        // 최종 커밋
        editor.commit();
    }
}
